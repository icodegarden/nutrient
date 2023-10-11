package io.github.icodegarden.nutrient.elasticsearch.v7.query;

import java.util.List;

import org.elasticsearch.client.RestHighLevelClient;

import io.github.icodegarden.nutrient.elasticsearch.query.ElasticsearchQuery;
import io.github.icodegarden.nutrient.lang.query.TableDataCountPO;
import io.github.icodegarden.nutrient.lang.query.TableDataCountStorage;
import io.github.icodegarden.nutrient.lang.util.SystemUtils;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ElasticsearchV7TableDataCountStorage implements TableDataCountStorage {

	private static final String TABLENAME = "table_data_count";

	private TableDataCountElasticsearchV7Repository repository;

	public ElasticsearchV7TableDataCountStorage(RestHighLevelClient client) {
		repository = new TableDataCountElasticsearchV7Repository(client, TABLENAME);
	}

	@Override
	public void add(TableDataCountPO po) {
		po.setId(po.getTableName());// 表名作为id，否则自动生成
		repository.add(po);
	}

	@Override
	public int updateCount(String tableName, long count) {
		TableDataCountPO update = new TableDataCountPO();
		update.setId(tableName);
		update.setDataCount(count);
		update.setUpdatedAt(SystemUtils.now());
		return repository.update(update);
	}

	@Override
	public List<TableDataCountPO> findAll() {
		ElasticsearchQuery<Object> query = new ElasticsearchQuery<Object>();
		query.setSize(10000);
		return repository.findAll(query);
	}

	int delete(String id) {
		return repository.delete(id);
	}
}