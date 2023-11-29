package io.github.icodegarden.nutrient.zookeeper.metricsregistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.icodegarden.nutrient.zookeeper.ZooKeeperHolder;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class MetricsServiceNamePath {

	private static final Map<String, String> NAME_PATH_MAP = new ConcurrentHashMap<String, String>();

	static String ensureServiceNamePath(ZooKeeperHolder zooKeeperHolder, String root, String serviceName) {
		String path = buildServiceNamePath(root, serviceName);
		zooKeeperHolder.ensureRootNode(path);
		return path;
	}

	static String buildServiceNamePath(String root, String serviceName) {
		String path = NAME_PATH_MAP.get(serviceName);
		if (path == null) {
			path = root + "/" + serviceName + "/metrics";
			NAME_PATH_MAP.put(serviceName, path);
		}
		return path;
	}
}
