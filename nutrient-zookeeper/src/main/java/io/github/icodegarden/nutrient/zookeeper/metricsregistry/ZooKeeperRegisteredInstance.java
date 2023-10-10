package io.github.icodegarden.nutrient.zookeeper.metricsregistry;

import io.github.icodegarden.nutrient.lang.metricsregistry.RegisteredInstance;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface ZooKeeperRegisteredInstance extends RegisteredInstance {

	String getZnode();
}
