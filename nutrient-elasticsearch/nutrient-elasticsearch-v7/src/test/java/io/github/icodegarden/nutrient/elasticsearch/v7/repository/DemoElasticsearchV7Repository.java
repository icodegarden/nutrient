package io.github.icodegarden.nutrient.elasticsearch.v7.repository;

import org.elasticsearch.client.RestHighLevelClient;

import io.github.icodegarden.nutrient.elasticsearch.query.ElasticsearchQuery;

/**
 * 只需声明泛型的类型
 * @author Fangfang.Xu
 *
 */
public class DemoElasticsearchV7Repository
		extends GenericElasticsearchV7Repository<DemoPO, DemoPO, ElasticsearchQuery<Object>, Object, DemoPO> {

	public DemoElasticsearchV7Repository(RestHighLevelClient client) {
		super(client, "demo-index");
	}

}
