package io.github.icodegarden.nutrient.elasticsearch.latest.query;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import io.github.icodegarden.nutrient.elasticsearch.latest.repository.GenericElasticsearchRepository;
import io.github.icodegarden.nutrient.elasticsearch.query.ElasticsearchQuery;
import io.github.icodegarden.nutrient.lang.query.TableDataCountPO;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class TableDataCountElasticsearchRepository extends
		GenericElasticsearchRepository<TableDataCountPO, TableDataCountPO, ElasticsearchQuery<Object>, Object, TableDataCountPO> {

	public TableDataCountElasticsearchRepository(ElasticsearchClient client, String index) {
		super(client, index);
	}

}
