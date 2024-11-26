package io.github.icodegarden.nutrient.elasticsearch.latest.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.ScriptBuilders;
import co.elastic.clients.elasticsearch._types.ScriptLanguage;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.MgetRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest.Builder;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.json.JsonData;
import io.github.icodegarden.nutrient.elasticsearch.query.ElasticsearchQuery;
import io.github.icodegarden.nutrient.lang.IdObject;
import io.github.icodegarden.nutrient.lang.util.JsonUtils;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class GenericElasticsearchRepository<PO extends IdObject<String>, U extends IdObject<String>, Q extends ElasticsearchQuery<W>, W, DO extends IdObject<String>>
		extends ElasticsearchRepository<PO, U, Q, W, DO> {

	public GenericElasticsearchRepository(ElasticsearchClient client, String index) {
		this(client, index, null);
	}

	public GenericElasticsearchRepository(ElasticsearchClient client, String index, String idFieldName) {
		this(client, index, null, false);
	}

	public GenericElasticsearchRepository(ElasticsearchClient client, String index, String idFieldName,
			boolean aliasOfMultiIndex) {
		super(client, index, idFieldName, aliasOfMultiIndex);
	}

	@Override
	protected IndexRequest.Builder<PO> buildIndexRequestBuilderOnAdd(PO po) {
		return new IndexRequest.Builder<PO>().index(getIndex()).id(po.getId()).document(po);
	}

	@Override
	protected BulkRequest.Builder buildBulkRequestBuilderOnAddBatch(Collection<PO> pos) {
		List<BulkOperation> operations = pos.stream().map(po -> {
			return new BulkOperation.Builder().index(b -> b.index(getIndex()).id(po.getId()).document(po)).build();
		}).collect(Collectors.toList());

		return new BulkRequest.Builder().operations(operations);
	}

	@Override
	protected UpdateRequest.Builder<U, U> buildUpdateRequestBuilderOnUpdate(U update) {
		return new UpdateRequest.Builder<U, U>().index(getIndex()).id(update.getId()).doc(update);
	}

	@Override
	protected BulkRequest.Builder buildBulkRequestBuilderOnUpdateBatch(Collection<U> updates) {
		List<BulkOperation> operations = updates.stream().map(update -> {
			return new BulkOperation.Builder()
					.update(b -> b.index(getIndex()).id(update.getId()).action(b2 -> b2.doc(update))).build();
		}).collect(Collectors.toList());

		return new BulkRequest.Builder().operations(operations);
	}

	@Override
	protected Builder buildUpdateByQueryRequestBuilderOnUpdateByQuery(U update, Q query) {
		String json = JsonUtils.serialize(update);
		Map<String, Object> map = JsonUtils.deserialize(json, Map.class);
		Map<String, JsonData> params = map.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> JsonData.of(entry.getValue())));

		String source = params.entrySet().stream().map(entry -> {
			return new StringBuilder(64).append("ctx._source.").append(entry.getKey()).append(" = params.")
					.append(entry.getValue()).toString();
		}).collect(Collectors.joining(";"));

		InlineScript inlineScript = ScriptBuilders.inline()//
				.lang(ScriptLanguage.Painless)//
				.source(source).options(Collections.emptyMap()).params(params).build();

		Script script = new Script.Builder().inline(inlineScript).build();

		co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder queryBuilder = buildQueryBuilder(query);
		UpdateByQueryRequest.Builder result = new UpdateByQueryRequest.Builder()//
				.index(getIndex())//
				.query(queryBuilder.build())//
				.script(script);
		return result;
	}

	@Override
	protected SearchRequest.Builder buildSearchRequestBuilderOnFindAll(Q query) {
		// TODO 应该根据Q query实现查询条件，还有其他方法也类似
		return new SearchRequest.Builder().index(getIndex());
	}

	@Override
	protected CountRequest.Builder buildCountRequestBuilderOnCount(Q query) {
		return new CountRequest.Builder().index(getIndex());
	}

	@Override
	protected GetRequest.Builder buildGetRequestBuilderOnFindOne(String id, W with) {
		return new GetRequest.Builder().index(getIndex()).id(id);
	}

	@Override
	protected MgetRequest.Builder buildMgetRequestBuilderOnFindByIds(List<String> ids, W with) {
		return new MgetRequest.Builder().index(getIndex()).ids(ids);
	}

	@Override
	protected DeleteRequest.Builder buildDeleteRequestBuilderOnDelete(String id) {
		return new DeleteRequest.Builder().index(getIndex()).id(id);
	}

	@Override
	protected BulkRequest.Builder buildBulkRequestBuilderOnDeleteBatch(Collection<String> ids) {
		List<BulkOperation> operations = ids.stream().map(id -> {
			return new BulkOperation.Builder().delete(b -> b.index(getIndex()).id(id)).build();
		}).collect(Collectors.toList());

		return new BulkRequest.Builder().operations(operations);
	}

	@Override
	protected DeleteByQueryRequest.Builder buildDeleteByQueryRequestBuilderOnDeleteByQuery(Q query) {
		co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder queryBuilder = buildQueryBuilder(query);
		DeleteByQueryRequest.Builder result = new DeleteByQueryRequest.Builder().index(getIndex())
				.query(queryBuilder.build());
		return result;
	}

	@Override
	protected String extractSearchAfter(DO obj) {
		return obj.getId();
	}
}
