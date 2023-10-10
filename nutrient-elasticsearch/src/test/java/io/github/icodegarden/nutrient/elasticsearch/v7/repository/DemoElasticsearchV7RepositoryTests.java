package io.github.icodegarden.nutrient.elasticsearch.v7.repository;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;

import io.github.icodegarden.nutrient.elasticsearch.ElasticsearchClientConfig;
import io.github.icodegarden.nutrient.elasticsearch.query.ElasticsearchQuery;
import io.github.icodegarden.nutrient.elasticsearch.repository.DemoPO;
import io.github.icodegarden.nutrient.elasticsearch.v7.ElasticsearchClientV7Builder;
import io.github.icodegarden.nutrient.lang.query.NextQuerySupportPage;

/**
 * 测试时要注释es8的依赖<br>
 * 如果报json异常，要再引一下
<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.12.5</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.datatype</groupId>
			<artifactId>jackson-datatype-jsr310</artifactId>
			<version>2.12.5</version>
		</dependency>
 * 
 * @author Fangfang.Xu
 *
 */
public class DemoElasticsearchV7RepositoryTests {

	ElasticsearchClientConfig elasticsearchClientConfig = new ElasticsearchClientConfig("http://172.22.122.21:9200");
	{
		elasticsearchClientConfig.setUsername("elastic");
		elasticsearchClientConfig.setPassword("elastic");
	}
	RestHighLevelClient client = ElasticsearchClientV7Builder.buildRestHighLevelClient(elasticsearchClientConfig);
	DemoElasticsearchV7Repository dao = new DemoElasticsearchV7Repository(client);

	DemoPO newDemoPO() {
		DemoPO po = new DemoPO();
//		po.setId(System.currentTimeMillis() + "");
		po.setVin("vin" + po.getId());
		po.setModel("CX11");
		po.setVehicleModelId(100L);
		return po;
	}

	@Test
	public void add() throws Exception {
		DemoPO po = newDemoPO();
		dao.add(po);
		
		Assertions.assertThat(po.getId()).isNotNull();
		System.out.println(po.getId());
	}

	@Test
	public void addBatch() throws Exception {
		DemoPO po1 = newDemoPO();
		DemoPO po2 = newDemoPO();
		dao.addBatch(Arrays.asList(po1, po2));

		Assertions.assertThat(po1.getId()).isNotNull();
		Assertions.assertThat(po2.getId()).isNotNull();
		System.out.println(po1.getId());
		System.out.println(po2.getId());
	}

	@Test
	public void update() throws Exception {
		DemoPO po = newDemoPO();
		dao.add(po);

		DemoPO update = new DemoPO();
		update.setId(po.getId());
		update.setVin("vin000");
		dao.update(update);

		DemoPO findOne = dao.findOne(po.getId(), null);
		Assertions.assertThat(findOne.getVin()).isEqualTo("vin000");
		Assertions.assertThat(findOne.getModel()).isEqualTo(po.getModel());// 不涉及更新
	}

	@Test
	public void findAll() throws Exception {
		DemoPO po1 = newDemoPO();
		po1.setModel("VB11");
		dao.add(po1);
		DemoPO po2 = newDemoPO();
		po2.setModel("VB11");
		dao.add(po2);
		DemoPO po3 = newDemoPO();
		po3.setModel("VB11");
		dao.add(po3);

		Thread.sleep(1500);// 等待索引刷新

		ElasticsearchQuery<Object> query = new ElasticsearchQuery<>();
		query.term("model", "vb11");// 大小写与mapping设置normalizer有关，这里使用自动mapping需要小写
		query.setPage(1);
		query.setSize(2);
		query.setOrderBy("vehicleModelId desc");// 由于自动mapping，会把model等字段设置为text，text是不能用于排序的

//		PageHelper.startPage(1, 2);//可用于测试是否count

		NextQuerySupportPage<DemoPO> page = dao.findAll(query);
		Assertions.assertThat(page.size()).isEqualTo(2);
		Assertions.assertThat(page.hasNextPage()).isTrue();
		Assertions.assertThat(page.getSearchAfter()).isNotNull();
		System.out.println(page.getTotalCount());
		
		page.forEach(po -> {
			Assertions.assertThat(po.getId()).isNotNull();
			System.out.println(po.getId());
		});
	}

	@Test
	public void findOne() throws Exception {
		DemoPO po = newDemoPO();
		dao.add(po);

		DemoPO findOne = dao.findOne(po.getId(), null);
		Assertions.assertThat(findOne).isNotNull();
		Assertions.assertThat(findOne.getModel()).isNotNull();
		
		Assertions.assertThat(findOne.getId()).isNotNull();
		System.out.println(findOne.getId());
	}

	@Test
	public void findByIds() throws Exception {
		DemoPO po1 = newDemoPO();
		dao.add(po1);
		DemoPO po2 = newDemoPO();
		dao.add(po2);

		List<DemoPO> list = dao.findByIds(Arrays.asList(po1.getId(), po2.getId()), null);
		Assertions.assertThat(list.size()).isEqualTo(2);
		
		list.forEach(po -> {
			Assertions.assertThat(po.getId()).isNotNull();
			System.out.println(po.getId());
		});
	}

	@Test
	public void count() throws Exception {
		findAll();

		ElasticsearchQuery<Object> query = new ElasticsearchQuery<>();
		query.term("model", "vb11");// 大小写与mapping设置normalizer有关，这里使用自动mapping需要小写
		long count = dao.count(query);

		Assertions.assertThat(count).isGreaterThan(0);
	}

	@Test
	public void delete() throws Exception {
		DemoPO po = newDemoPO();
		dao.add(po);

		dao.delete(po.getId());
		DemoPO findOne = dao.findOne(po.getId(), null);
		Assertions.assertThat(findOne).isNull();
	}

	@Test
	public void deleteBatch() throws Exception {
		DemoPO po1 = newDemoPO();
		dao.add(po1);
		DemoPO po2 = newDemoPO();
		dao.add(po2);

		dao.deleteBatch(Arrays.asList(po1.getId(), po2.getId()));

		DemoPO findOne = dao.findOne(po1.getId(), null);
		Assertions.assertThat(findOne).isNull();
		findOne = dao.findOne(po2.getId(), null);
		Assertions.assertThat(findOne).isNull();
	}
}
