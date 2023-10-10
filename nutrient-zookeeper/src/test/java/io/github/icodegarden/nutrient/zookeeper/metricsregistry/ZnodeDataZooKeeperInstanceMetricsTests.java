package io.github.icodegarden.nutrient.zookeeper.metricsregistry;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics;
import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics.Dimension;
import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics.DimensionName;
import io.github.icodegarden.nutrient.zookeeper.CommonZookeeperBuilder;
import io.github.icodegarden.nutrient.zookeeper.metricsregistry.ZnodeDataZooKeeperInstanceMetrics;
import io.github.icodegarden.nutrient.zookeeper.metricsregistry.ZooKeeperInstanceMetrics;
import io.github.icodegarden.nutrient.zookeeper.metricsregistry.ZooKeeperInstanceRegistry;
import io.github.icodegarden.nutrient.zookeeper.metricsregistry.ZooKeeperRegisteredInstance;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class ZnodeDataZooKeeperInstanceMetricsTests extends CommonZookeeperBuilder {

	String root = "/xff";
	int port = 9999;
	ZooKeeperInstanceRegistry zooKeeperInstanceRegister;
	ZooKeeperInstanceMetrics zooKeeperInstanceMetrics;

	@BeforeEach
	void init() {
		zooKeeperInstanceRegister = new ZooKeeperInstanceRegistry(zkh, root, "worker", port);
		zooKeeperInstanceMetrics = new ZnodeDataZooKeeperInstanceMetrics(zkh, root);
	}

	@Test
	void setMetrics() throws Exception {
		ZooKeeperRegisteredInstance result = zooKeeperInstanceRegister.registerIfNot();

		Metrics metrics = new Metrics(Arrays.asList(new Dimension(Metrics.DimensionName.Jobs, 4, 2)));
		zooKeeperInstanceMetrics.setMetrics(result, metrics);

		zooKeeperInstanceRegister.deregister();
		zooKeeperInstanceMetrics.removeMetrics(result);
	}
	
	@Test
	void getMetrics() throws Exception {
		ZooKeeperRegisteredInstance result = zooKeeperInstanceRegister.registerIfNot();
		
		Metrics metrics = zooKeeperInstanceMetrics.getMetrics(result);
		assertThat(metrics).isNull();
		
		//--------------------------------------
		metrics = new Metrics(Arrays.asList(new Dimension(Metrics.DimensionName.Jobs, 4, 2)));
		zooKeeperInstanceMetrics.setMetrics(result, metrics);
		metrics = zooKeeperInstanceMetrics.getMetrics(result);
		assertThat(metrics).isNotNull();
		assertThat(metrics.getDimension(DimensionName.Jobs).getMax()).isEqualTo(4);
		assertThat(metrics.getDimension(DimensionName.Jobs).getUsed()).isEqualTo(2);
		
		//--------------------------------------
		zooKeeperInstanceMetrics.removeMetrics(result);
		metrics = zooKeeperInstanceMetrics.getMetrics(result);
		assertThat(metrics).isNull();
	}

	@Test
	void listMetrics() throws Exception {
		//初始没有----------------------------------
		List<Metrics> listMetrics = zooKeeperInstanceMetrics.listMetrics("worker");
		assertThat(listMetrics).isEmpty();

		//虽然注册了实例但是没有----------------------------------
		ZooKeeperRegisteredInstance result = zooKeeperInstanceRegister.registerIfNot();
		listMetrics = zooKeeperInstanceMetrics.listMetrics("worker");
		assertThat(listMetrics).isEmpty();

		//设置后有了
		Metrics metrics = new Metrics(Arrays.asList(new Dimension(DimensionName.Jobs, 4, 0)));
		zooKeeperInstanceMetrics.setMetrics(result, metrics);
		listMetrics = zooKeeperInstanceMetrics.listMetrics("worker");
		assertThat(listMetrics).hasSize(1);
		assertThat(listMetrics.get(0).getDimension(DimensionName.Jobs).getUsed()).isEqualTo(0);
		assertThat(listMetrics.get(0).getInstanceName()).isEqualTo(result.getInstanceName());

		//验证更新
		metrics.setDimension(new Dimension(Metrics.DimensionName.Jobs, 4, 2));
		zooKeeperInstanceMetrics.setMetrics(result, metrics);
		listMetrics = zooKeeperInstanceMetrics.listMetrics("worker");
		assertThat(listMetrics.get(0).getDimension(DimensionName.Jobs).getUsed()).isEqualTo(2);
		assertThat(listMetrics.get(0).getInstanceName()).isEqualTo(result.getInstanceName());
	}

}
