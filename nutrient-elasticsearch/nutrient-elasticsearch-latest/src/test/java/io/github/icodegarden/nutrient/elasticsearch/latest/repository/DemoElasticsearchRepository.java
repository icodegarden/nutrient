package io.github.icodegarden.nutrient.elasticsearch.latest.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import io.github.icodegarden.nutrient.elasticsearch.latest.repository.GenericElasticsearchRepository;
import io.github.icodegarden.nutrient.elasticsearch.query.ElasticsearchQuery;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class DemoElasticsearchRepository
		extends GenericElasticsearchRepository<DemoPO, DemoPO, ElasticsearchQuery<Object>, Object, DemoPO> {

	public DemoElasticsearchRepository(ElasticsearchClient client) {
		super(client, "demo-index");
	}

}
