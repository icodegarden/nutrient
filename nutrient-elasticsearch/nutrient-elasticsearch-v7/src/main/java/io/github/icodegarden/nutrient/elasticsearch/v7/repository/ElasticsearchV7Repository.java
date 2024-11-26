package io.github.icodegarden.nutrient.elasticsearch.v7.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import io.github.icodegarden.nutrient.elasticsearch.query.ElasticsearchQuery;
import io.github.icodegarden.nutrient.elasticsearch.repository.ElasticsearchRepositorySupport;
import io.github.icodegarden.nutrient.elasticsearch.v7.BulkResponseHasErrorV7Exception;
import io.github.icodegarden.nutrient.lang.IdObject;
import io.github.icodegarden.nutrient.lang.query.NextQuerySupportArrayList;
import io.github.icodegarden.nutrient.lang.query.NextQuerySupportList;
import io.github.icodegarden.nutrient.lang.query.NextQuerySupportPage;
import io.github.icodegarden.nutrient.lang.tuple.Tuple2;
import io.github.icodegarden.nutrient.lang.util.JsonUtils;
import io.github.icodegarden.nutrient.lang.util.LogUtils;
import io.github.icodegarden.nutrient.lang.util.PageUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Fangfang.Xu
 *
 */
@Slf4j
public abstract class ElasticsearchV7Repository<PO, U, Q extends ElasticsearchQuery<W>, W, DO>
		extends ElasticsearchRepositorySupport<PO, U, Q, W, DO> {

	protected final RestHighLevelClient client;

	public ElasticsearchV7Repository(RestHighLevelClient client, String index) {
		this(client, index, null);
	}

	public ElasticsearchV7Repository(RestHighLevelClient client, String index, String idFieldName) {
		this(client, index, null, false);
	}

	public ElasticsearchV7Repository(RestHighLevelClient client, String index, String idFieldName,
			boolean aliasOfMultiIndex) {
		super(index, idFieldName, aliasOfMultiIndex);
		this.client = client;
	}

	@Override
	public void add(PO po) {
		validate(po);

		Tuple2<IndexRequest, RequestOptions> tuple = buildIndexRequestOnAdd(po);
		try {
			IndexResponse indexResponse = client.index(tuple.getT1(), tuple.getT2());
			// response:IndexResponse[index=vehicle_assignment,type=_doc,id=1,version=7,result=updated,seqNo=6,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]
			if (indexResponse.getShardInfo().getSuccessful() < 1) {
				throw new IllegalStateException(
						"add failed, successful shards:" + indexResponse.getShardInfo().getSuccessful());
			}
			IdObject.setIdIfNecessary(indexResponse.getId(), po);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void addBatch(Collection<PO> pos) {
		if (CollectionUtils.isEmpty(pos)) {
			return;
		}

		Tuple2<BulkRequest, RequestOptions> tuple = buildBulkRequestOnAddBatch(pos);
		try {
			BulkResponse response = client.bulk(tuple.getT1(), tuple.getT2());
			if (response.hasFailures()) {
				throw new BulkResponseHasErrorV7Exception("addBatch Bulk had errors", response);
			}

			List<BulkItemResponse> items = Arrays.asList(response.getItems());
			if (pos.size() == items.size()) {
				Iterator<PO> it1 = pos.iterator();
				Iterator<BulkItemResponse> it2 = items.iterator();
				while (it1.hasNext()) {
					PO po = it1.next();
					BulkItemResponse item = it2.next();
					IdObject.setIdIfNecessary(item.getId(), po);
				}
			} else {
				LogUtils.warnIfEnabled(log,
						() -> log.warn("can not setId after addBatch, size not eq, pos size:{}, responseItems size:{}",
								pos.size(), items.size()));
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int update(U update) {
		try {
			doUpdate(getIndex(), update);
		} catch (ElasticsearchStatusException e) {
			LogUtils.warnIfEnabled(log, () -> log.warn("Elasticsearch update status is {}, index:{}, update:{}",
					e.status().getStatus(), getIndex(), update));

			/**
			 * 理用这个可以得到id
			 */
			Tuple2<UpdateRequest, RequestOptions> tuple = buildUpdateRequestOnUpdate(update);
			UpdateRequest updateRequest = tuple.getT1();
			String id = updateRequest.id();
			/**
			 * alias的次最新，去更新(次最新)的index(结果无论失败)，通常是可以的，因为这种场景是最新的刚出来，次最新里的暂时还需要写操作，而不会是次次最新里的数据
			 */
			doOnRealIndexIf404(getIndex(), id, e, realIndex -> doUpdate(realIndex, update));
		}
		return 1;
	}

	/**
	 * Bulk方式
	 */
	@Override
	public int updateBatch(Collection<U> updates) {
		if (CollectionUtils.isEmpty(updates)) {
			return 0;
		}

		Tuple2<BulkRequest, RequestOptions> tuple = buildBulkRequestOnUpdateBatch(updates);
		try {
			BulkResponse response = client.bulk(tuple.getT1(), tuple.getT2());
			if (response.hasFailures()) {
				throw new BulkResponseHasErrorV7Exception("updateBatch Bulk had errors", response);
			}
			return updates.size();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int updateByQuery(U update, Q query) {
		Tuple2<UpdateByQueryRequest, RequestOptions> tuple = buildUpdateByQueryRequest(update, query);
		UpdateByQueryRequest updateByQueryRequest = tuple.getT1();
		try {
			BulkByScrollResponse response = client.updateByQuery(updateByQueryRequest, tuple.getT2());
			return (int) response.getUpdated();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public NextQuerySupportPage<DO> findAll(Q query) {
		try {
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			/**
			 * 
			 */
			QueryBuilder queryBuilder = buildQueryBuilder(query);
			searchSourceBuilder.query(queryBuilder);

			searchSourceBuilder.from((query.getPage() - 1) * query.getSize());
			searchSourceBuilder.size(query.getSize() + 1);// more 1
			/**
			 * sort
			 */
			if (StringUtils.hasText(query.getOrderBy())) {
				String[] split1 = query.getOrderBy().split(",");
				for (String s : split1) {
					String[] split2 = s.split(" ");
					searchSourceBuilder.sort(split2[0].trim(),
							split2[1].trim().equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
				}
			}
			if (query.getSearchAfters() != null) {
				searchSourceBuilder.searchAfter(query.getSearchAfters().toArray());
			}
			searchSourceBuilder.timeout(new TimeValue(getReadTimeoutMillis(), TimeUnit.MILLISECONDS));

			boolean isCount = PageUtils.isCount();
			if (!isCount) {
				// 是否进行count，count需要消耗一点性能
				searchSourceBuilder.trackTotalHits(false);
			}

			Tuple2<SearchRequest, RequestOptions> tuple = buildSearchRequestOnFindAll(query);
			SearchRequest searchRequest = tuple.getT1();
			searchRequest.source(searchSourceBuilder);

			SearchResponse searchResponse = client.search(searchRequest, tuple.getT2());
			SearchHits hits = searchResponse.getHits();
			long total = 0;
			/**
			 * 是否count
			 */
			if (isCount) {
				total = hits.getTotalHits().value;// 按count
			} else {
				total = hits.getHits().length;// 按查询结果数组长度
			}
			NextQuerySupportList<DO> nextQuerySupportList;
			if (total == 0) {
				nextQuerySupportList = NextQuerySupportArrayList.empty();
			} else {
				LinkedList<DO> list = new LinkedList<DO>();
				AtomicReference<Boolean> hasNextPage = new AtomicReference<Boolean>(false);
				int i = 0;
				for (SearchHit hit : hits) {
					if (i != query.getSize()) {// not last one
						DO result = extractResult(hit);

						IdObject.setIdIfNecessary(hit.getId(), result);

						list.add(result);
					} else {// more 1,last
						hasNextPage.set(true);
						break;
					}

					i++;
				}

				nextQuerySupportList = NextQuerySupportArrayList.newSupportSearchAfter(list,
						last -> hasNextPage.get() ? extractSearchAfter(last) : null);
			}

			NextQuerySupportPage<DO> page = new NextQuerySupportPage<DO>(query.getPage(), query.getSize(), total,
					isCount, query.getOrderBy(), nextQuerySupportList);
			return page;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			PageUtils.clearPage();
		}
	}

	@Override
	public long count(Q query) {
		QueryBuilder queryBuilder = buildQueryBuilder(query);

		Tuple2<CountRequest, RequestOptions> tuple = buildCountRequestOnCount(query);
		CountRequest countRequest = tuple.getT1();
		countRequest.query(queryBuilder);
		try {
			CountResponse countResponse = client.count(countRequest, tuple.getT2());
			return countResponse.getCount();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 先根据id查index<br>
	 * 如果没有则会404，接着若是aliasOfMultiIndex，则使用id term查，如果还是没有就确实不存在
	 */
	@Override
	public DO findOne(String id, W with) {
		Tuple2<GetRequest, RequestOptions> tuple = buildGetRequestOnFindOne(id, with);
		GetRequest getRequest = tuple.getT1();
		try {
			GetResponse getResponse = client.get(getRequest, tuple.getT2());
			if (!getResponse.isExists()) {
				return null;
			}
			DO result = extractResult(getResponse);

			IdObject.setIdIfNecessary(getResponse.getId(), result);

			return result;
		} catch (ElasticsearchStatusException e) {
			LogUtils.warnIfEnabled(log, () -> log.warn("Elasticsearch findOne status is {}, index:{}, id:{}",
					e.status().getStatus(), getIndex(), id));

			/**
			 * 404
			 */
			if (RestStatus.NOT_FOUND.equals(e.status())) {
				if (isAliasOfMultiIndex()) {
					/**
					 * 如果source中没有定义id字段：不支持通过id查询（因为无法确定real index，pre也不一定准确）
					 */
					SearchHit hit = findOneIfAliasOfMultiIndex(id);
					return hit == null ? null : extractResult(hit);
				}

				/**
				 * 单索引，id确实不存在
				 */
				return null;
			}

//			前置化查询
//			/**
//			 * 400：别名of多索引情况下，get /{id} 一定是400
//			 */
//			if (RestStatus.BAD_REQUEST.equals(e.status())
//					&& e.getDetailedMessage().contains("has more than one indices associated")) {
//				/**
//				 * source中没有定义id字段：不支持通过id查询（因为无法确定real index，pre也不一定准确）
//				 */
//				if (idFieldName == null) {
//					throw e;
//				}
//				SearchCondition condition = new SearchCondition();
//				condition.term(idFieldName, id);
//				List<T> list = list(condition, ret);
//				return list.isEmpty() ? null : list.get(0);
//			}
			throw e;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public List<DO> findByIds(List<String> ids, W with) {
		Tuple2<MultiGetRequest, RequestOptions> tuple = buildMultiGetRequestOnFindByIds(ids, with);
		MultiGetRequest multiGetRequest = tuple.getT1();
		try {
			MultiGetResponse multiGetResponse = client.mget(multiGetRequest, tuple.getT2());

			MultiGetItemResponse[] responses = multiGetResponse.getResponses();
			if (responses == null || responses.length == 0) {
				return Collections.emptyList();
			}

			List<DO> list = Arrays.asList(responses).stream().map(response -> {
				GetResponse getResponse = response.getResponse();

				DO result = extractResult(getResponse);

				IdObject.setIdIfNecessary(getResponse.getId(), result);

				return result;
			}).collect(Collectors.toList());
			return list;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int delete(String id) {
		try {
			doDelete(getIndex(), id);
		} catch (ElasticsearchStatusException e) {
			LogUtils.warnIfEnabled(log, () -> log.warn("Elasticsearch delete status is {}, index:{}, id:{}",
					e.status().getStatus(), getIndex(), id));

			doOnRealIndexIf404(getIndex(), id, e, realIndex -> doDelete(realIndex, id));
		}
		return 1;
	}

	@Override
	public int deleteBatch(Collection<String> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return 0;
		}

		Tuple2<BulkRequest, RequestOptions> tuple = buildBulkRequestOnDeleteBatch(ids);
		BulkRequest bulkRequest = tuple.getT1();
		try {
			BulkResponse response = client.bulk(bulkRequest, tuple.getT2());
			if (response.hasFailures()) {
				throw new BulkResponseHasErrorV7Exception("deleteBatch Bulk had errors", response);
			}
			return ids.size();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int deleteByQuery(Q query) {
		Tuple2<DeleteByQueryRequest, RequestOptions> tuple = buildDeleteByQueryRequest(query);
		DeleteByQueryRequest deleteByQueryRequest = tuple.getT1();
		try {
			BulkByScrollResponse response = client.deleteByQuery(deleteByQueryRequest, tuple.getT2());
			return (int) response.getDeleted();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void increment(String id, String fieldName, long value) {
		UpdateRequest updateRequest = new UpdateRequest(getIndex(), id);

		HashMap<String, Object> params = new HashMap<String, Object>(1);
		params.put("value", value);
		Script script = new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG, //
				new StringBuilder(64).append("ctx._source.").append(fieldName).append(" += params.value;").toString()//
				, Collections.emptyMap(), params);
		updateRequest.script(script);

		try {
			client.update(updateRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * @param consumer<T> T 真实索引
	 */
	private void doOnRealIndexIf404(String index, String id, ElasticsearchStatusException e,
			Consumer<String> consumer) {
		/**
		 * 404：#数据确实不存在；#别名of多索引情况下，使用id进行更新或删除(删除时的404不是es
		 * client报出来的，是得到response后解析并throw的ElasticsearchStatusException)，
		 * id对应到最新的索引是ok的，如果是老索引则404
		 */
		if (RestStatus.NOT_FOUND.equals(e.status())) {
			/**
			 * index不是aliasOfMultiIndex，则数据确实不存在
			 */
			if (!isAliasOfMultiIndex()) {
				throw e;
			}
			/**
			 * source中有id字段：精确查询该数据的index去更新
			 */
			SearchHit hit = findOneIfAliasOfMultiIndex(id);
			if (hit == null) {
				/**
				 * 还是没有数据
				 */
				throw new IllegalArgumentException("doc really Not Found with id:" + id, e);
			}
			String realIndex = hit.getIndex();
			consumer.accept(realIndex);
			return;
		}
		/**
		 * 其他错误
		 */
		throw e;
	}

	protected abstract Tuple2<IndexRequest, RequestOptions> buildIndexRequestOnAdd(PO po);

	protected abstract Tuple2<BulkRequest, RequestOptions> buildBulkRequestOnAddBatch(Collection<PO> pos);

	protected abstract Tuple2<UpdateRequest, RequestOptions> buildUpdateRequestOnUpdate(U update);

	protected abstract Tuple2<BulkRequest, RequestOptions> buildBulkRequestOnUpdateBatch(Collection<U> updates);

	protected abstract Tuple2<UpdateByQueryRequest, RequestOptions> buildUpdateByQueryRequest(U update, Q query);

	protected abstract Tuple2<SearchRequest, RequestOptions> buildSearchRequestOnFindAll(Q query);

	protected abstract Tuple2<CountRequest, RequestOptions> buildCountRequestOnCount(Q query);

	protected abstract Tuple2<GetRequest, RequestOptions> buildGetRequestOnFindOne(String id, W with);

	protected abstract Tuple2<MultiGetRequest, RequestOptions> buildMultiGetRequestOnFindByIds(List<String> ids,
			W with);

	protected abstract Tuple2<DeleteRequest, RequestOptions> buildDeleteRequestOnDelete(String id);

	protected abstract Tuple2<BulkRequest, RequestOptions> buildBulkRequestOnDeleteBatch(Collection<String> ids);

	protected abstract Tuple2<DeleteByQueryRequest, RequestOptions> buildDeleteByQueryRequest(Q query);

	protected DO extractResult(SearchHit hit) {
		String json = hit.getSourceAsString();
		return JsonUtils.deserialize(json, getClassDO());
	}

	protected DO extractResult(GetResponse getResponse) {
		String json = getResponse.getSourceAsString();
		return JsonUtils.deserialize(json, getClassDO());
	}

	/**
	 * 不满足时自行覆盖
	 * 
	 * @param query
	 * @return
	 */
	protected BoolQueryBuilder buildQueryBuilder(Q query) {
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		if (query.getTerms() != null) {
			for (Entry<String, Object> entry : query.getTerms().entrySet()) {
				if (entry.getValue() != null) {
					boolQueryBuilder.filter(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
				}
			}
		}

		if (query.getMatches() != null) {
			for (Entry<String, Object> entry : query.getMatches().entrySet()) {
				if (entry.getValue() != null) {
					boolQueryBuilder.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
				}
			}
		}

		if (query.getMultiMatches() != null) {
			for (Entry<Object, List<String>> entry : query.getMultiMatches().entrySet()) {
				if (entry.getValue() != null) {
					boolQueryBuilder.must(QueryBuilders.multiMatchQuery(entry.getKey(),
							entry.getValue().toArray(new String[entry.getValue().size()])));
				}
			}
		}

		if (query.getRangeFroms() != null) {
			for (Entry<String, Object> entry : query.getRangeFroms().entrySet()) {
				if (entry.getValue() != null) {
					boolQueryBuilder.filter(QueryBuilders.rangeQuery(entry.getKey()).from(entry.getValue(), true));
				}
			}
		}

		if (query.getRangeTos() != null) {
			for (Entry<String, Object> entry : query.getRangeTos().entrySet()) {
				if (entry.getValue() != null) {
					boolQueryBuilder.filter(QueryBuilders.rangeQuery(entry.getKey()).to(entry.getValue(), true));
				}
			}
		}

		if (query.getWildcards() != null) {
			for (Entry<String, Object> entry : query.getWildcards().entrySet()) {
				if (entry.getValue() != null) {
//					boolQueryBuilder
//							.must(QueryBuilders.wildcardQuery(entry.getKey(), String.format("*%s*", entry.getValue())));
					boolQueryBuilder
							.must(QueryBuilders.wildcardQuery(entry.getKey(), String.format("%s*", entry.getValue())));// 最左匹配提升性能
				}
			}
		}

		if (query.getExists() != null) {
			for (String fname : query.getExists()) {
				boolQueryBuilder.filter(QueryBuilders.existsQuery(fname));
			}
		}

		if (query.getNotExists() != null) {
			for (String fname : query.getNotExists()) {
				boolQueryBuilder.mustNot(QueryBuilders.existsQuery(fname));
			}
		}

		return boolQueryBuilder;
	}

	private void doUpdate(String index, U update) throws ElasticsearchStatusException {
		Tuple2<UpdateRequest, RequestOptions> tuple = buildUpdateRequestOnUpdate(update);
		UpdateRequest updateRequest = tuple.getT1();
		updateRequest.index(index);
		try {
			UpdateResponse updateResponse = client.update(updateRequest, tuple.getT2());
			if (updateResponse.getShardInfo().getFailed() > 0) {
				throw new IllegalStateException(
						"update failed, failed shards:" + updateResponse.getShardInfo().getFailed());
			}
		} catch (ElasticsearchStatusException e) {
			if (RestStatus.CONFLICT.equals(e.status())) {
				/**
				 * 通过id的更新，几乎不应该存在并发更新
				 */
				throw new IllegalStateException("conflict on update", e);
			}
			throw e;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 文档可能存在于非最新索引，可以使用term查询还处于可读的索引（hot、warm、cold）
	 * 
	 * @param id
	 * @return
	 */
	private SearchHit findOneIfAliasOfMultiIndex(String id) {
		if (isAliasOfMultiIndex()) {
			LogUtils.infoIfEnabled(log, () -> log.info("findOneIfAliasOfMultiIndex index:{}, id:{}", getIndex(), id));

			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
			boolQueryBuilder.filter(QueryBuilders.termQuery(getIdFieldName(), id));
			sourceBuilder.query(boolQueryBuilder);
			sourceBuilder.size(1);
			sourceBuilder.timeout(new TimeValue(getReadTimeoutMillis(), TimeUnit.MILLISECONDS));

			SearchRequest searchRequest = new SearchRequest();
			searchRequest.indices(getIndex());
			searchRequest.source(sourceBuilder);

			try {
				SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
				SearchHits hits = searchResponse.getHits();
				long total = hits.getTotalHits().value;
				return total == 0 ? null : hits.iterator().next();
			} catch (IOException e2) {
				throw new IllegalStateException(e2);
			}
		}
		return null;
	}

	private void doDelete(String index, String id) throws ElasticsearchStatusException {
		Tuple2<DeleteRequest, RequestOptions> tuple = buildDeleteRequestOnDelete(id);
		DeleteRequest deleteRequest = tuple.getT1();
		deleteRequest.index(index);
		try {
			DeleteResponse deleteResponse = client.delete(deleteRequest, tuple.getT2());
			/**
			 * 删除时如果数据不存在，以前的版本不会报ElasticsearchStatusException 404，即使报了这里做冗余也不会错的
			 */
			if (deleteResponse.getResult().equals(org.elasticsearch.action.DocWriteResponse.Result.NOT_FOUND)) {
				throw new ElasticsearchStatusException("result not found", RestStatus.NOT_FOUND);
			}
			if (deleteResponse.getShardInfo().getSuccessful() < 1) {
				throw new IllegalStateException(
						"delete failed, successful shards:" + deleteResponse.getShardInfo().getSuccessful());
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
