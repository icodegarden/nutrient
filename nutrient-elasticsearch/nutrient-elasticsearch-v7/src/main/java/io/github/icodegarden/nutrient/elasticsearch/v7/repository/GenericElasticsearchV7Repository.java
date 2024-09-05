package io.github.icodegarden.nutrient.elasticsearch.v7.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;

import io.github.icodegarden.nutrient.elasticsearch.query.ElasticsearchQuery;
import io.github.icodegarden.nutrient.lang.IdObject;
import io.github.icodegarden.nutrient.lang.tuple.Tuple2;
import io.github.icodegarden.nutrient.lang.tuple.Tuples;
import io.github.icodegarden.nutrient.lang.util.JsonUtils;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class GenericElasticsearchV7Repository<PO extends IdObject<String>, U extends IdObject<String>, Q extends ElasticsearchQuery<W>, W, DO extends IdObject<String>>
		extends ElasticsearchV7Repository<PO, U, Q, W, DO> {

	public GenericElasticsearchV7Repository(RestHighLevelClient client, String index) {
		this(client, index, null);
	}

	public GenericElasticsearchV7Repository(RestHighLevelClient client, String index, String idFieldName) {
		this(client, index, null, false);
	}

	public GenericElasticsearchV7Repository(RestHighLevelClient client, String index, String idFieldName,
			boolean aliasOfMultiIndex) {
		super(client, index, idFieldName, aliasOfMultiIndex);
	}

	@Override
	protected Tuple2<IndexRequest, RequestOptions> buildIndexRequestOnAdd(PO po) {
		IndexRequest indexRequest = new IndexRequest(getIndex());
		indexRequest.id(po.getId());// 没配置id则会自动生成
		indexRequest.source(toSource(po));
		return Tuples.of(indexRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<BulkRequest, RequestOptions> buildBulkRequestOnAddBatch(Collection<PO> pos) {
		BulkRequest bulkRequest = new BulkRequest();

		List<DocWriteRequest<?>> operations = pos.stream().map(po -> {
			IndexRequest indexRequest = new IndexRequest(getIndex());
			indexRequest.id(po.getId());// 没配置id则会自动生成
			indexRequest.source(toSource(po));
			return indexRequest;
		}).collect(Collectors.toList());

		bulkRequest.add(operations);

		return Tuples.of(bulkRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<UpdateRequest, RequestOptions> buildUpdateRequestOnUpdate(U update) {
		UpdateRequest request = new UpdateRequest(getIndex(), update.getId());
		request.doc(toSource(update));

		return Tuples.of(request, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<BulkRequest, RequestOptions> buildBulkRequestOnUpdateBatch(Collection<U> updates) {
		BulkRequest bulkRequest = new BulkRequest();

		List<DocWriteRequest<?>> operations = updates.stream().map(update -> {
			UpdateRequest request = new UpdateRequest(getIndex(), update.getId());
			request.doc(toSource(update));
			return request;
		}).collect(Collectors.toList());

		bulkRequest.add(operations);

		return Tuples.of(bulkRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<SearchRequest, RequestOptions> buildSearchRequestOnFindAll(Q query) {
		SearchRequest searchRequest = new SearchRequest().indices(getIndex());
		return Tuples.of(searchRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<CountRequest, RequestOptions> buildCountRequestOnCount(Q query) {
		CountRequest countRequest = new CountRequest().indices(getIndex());
		return Tuples.of(countRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<GetRequest, RequestOptions> buildGetRequestOnFindOne(String id, W with) {
		GetRequest getRequest = new GetRequest(getIndex(), id);
		return Tuples.of(getRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<MultiGetRequest, RequestOptions> buildMultiGetRequestOnFindByIds(List<String> ids, W with) {
		MultiGetRequest multiGetRequest = new MultiGetRequest();
		for (String id : ids) {
			multiGetRequest.add(getIndex(), id);
		}

		return Tuples.of(multiGetRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<DeleteRequest, RequestOptions> buildDeleteRequestOnDelete(String id) {
		DeleteRequest deleteRequest = new DeleteRequest(getIndex(), id);
		return Tuples.of(deleteRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<BulkRequest, RequestOptions> buildBulkRequestOnDeleteBatch(Collection<String> ids) {
		BulkRequest bulkRequest = new BulkRequest();

		List<DocWriteRequest<?>> operations = ids.stream().map(id -> {
			DeleteRequest deleteRequest = new DeleteRequest(getIndex(), id);
			return deleteRequest;
		}).collect(Collectors.toList());

		bulkRequest.add(operations);
		return Tuples.of(bulkRequest, RequestOptions.DEFAULT);
	}

	@Override
	protected Tuple2<DeleteByQueryRequest, RequestOptions> buildDeleteByQueryRequest(Q query) {
		throw new UnsupportedOperationException("Please Impl buildDeleteByQueryRequest.");
	}

	protected Map<String, Object> toSource(Object obj) {
		Map<String, Object> source;// 需要这样的泛型
		if (obj instanceof Map) {
			source = (Map) obj;
		} else {
			String json = JsonUtils.serialize(obj);
			source = JsonUtils.deserialize(json, Map.class);
		}
		return source;
	}

	@Override
	protected String extractSearchAfter(DO obj) {
		return obj.getId();
	}

	@Override
	protected DO extractResult(SearchHit hit) {
		String json = hit.getSourceAsString();
		DO one = JsonUtils.deserialize(json, getClassDO());
		if (one.getId() == null) {
			one.setId(hit.getId());
		}
		return one;
	}

	@Override
	protected DO extractResult(GetResponse getResponse) {
		String json = getResponse.getSourceAsString();
		DO one = JsonUtils.deserialize(json, getClassDO());
		if (one.getId() == null) {
			one.setId(getResponse.getId());
		}
		return one;
	}
}
