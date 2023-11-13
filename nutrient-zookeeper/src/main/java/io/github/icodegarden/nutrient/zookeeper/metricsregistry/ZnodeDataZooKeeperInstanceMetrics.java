package io.github.icodegarden.nutrient.zookeeper.metricsregistry;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics;
import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics.Dimension;
import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics.DimensionName;
import io.github.icodegarden.nutrient.lang.metricsregistry.RegisteredInstance;
import io.github.icodegarden.nutrient.lang.serialization.Deserializer;
import io.github.icodegarden.nutrient.lang.serialization.Hessian2Deserializer;
import io.github.icodegarden.nutrient.lang.serialization.Hessian2Serializer;
import io.github.icodegarden.nutrient.lang.serialization.SerializationException;
import io.github.icodegarden.nutrient.lang.serialization.Serializer;
import io.github.icodegarden.nutrient.lang.util.JsonUtils;
import io.github.icodegarden.nutrient.zookeeper.ACLs;
import io.github.icodegarden.nutrient.zookeeper.ZooKeeperHolder;
import io.github.icodegarden.nutrient.zookeeper.exception.ExceedExpectedZooKeeperException;
import io.github.icodegarden.nutrient.zookeeper.exception.InvalidDataSizeZooKeeperException;
import io.github.icodegarden.nutrient.zookeeper.exception.ZooKeeperException;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ZnodeDataZooKeeperInstanceMetrics implements ZooKeeperInstanceMetrics<Metrics> {
	private static final Logger log = LoggerFactory.getLogger(ZnodeDataZooKeeperInstanceMetrics.class);

	private ZooKeeperHolder zooKeeperHolder;
	private final String root;

	private Serializer<Object> serializer = new Hessian2Serializer();
	private Deserializer<Object> deserializer = new Hessian2Deserializer();

	/**
	 * 
	 * @param zooKeeperHolder
	 * @param root               例如注册的root是/beecomb/masters ， 那么这里应该是/beecomb
	 * @param registeredSupplier instanceName
	 */
	public ZnodeDataZooKeeperInstanceMetrics(ZooKeeperHolder zooKeeperHolder, String root)
			throws IllegalArgumentException {
		if (zooKeeperHolder == null) {
			throw new IllegalArgumentException("param zooKeeperHolder must not null");
		}
		if (root == null || root.isEmpty()) {
			throw new IllegalArgumentException("param root must not empty");
		}
		if (!root.startsWith("/")) {
			throw new IllegalArgumentException("param root must start with /");
		}
		this.zooKeeperHolder = zooKeeperHolder;
		this.root = root;
	}

	@Override
	public <T extends RegisteredInstance> void setMetrics(T instance, Metrics metrics) throws ZooKeeperException {
		if (instance == null) {
			log.warn("registered insatnce not found on updateMetrics, cancel updateMetrics");
			return;
		}

		String path = MetricsServiceNamePath.ensureServiceNamePath(zooKeeperHolder, root, instance.getServiceName());

		String nodeName = path + "/" + instance.getInstanceName();

		if (log.isDebugEnabled()) {
			log.debug("set metrics to zookeeper, znode:{}, metrics:{}", nodeName, metrics);
		}

		byte[] data = serializer.serialize(metrics);
		if (data.length >= InvalidDataSizeZooKeeperException.MAX_DATA_SIZE) {
			throw new InvalidDataSizeZooKeeperException(data.length);
		}
		try {
			zooKeeperHolder.getConnectedZK().setData(nodeName, data, -1);
		} catch (KeeperException.NoNodeException ignore) {
			try {
				zooKeeperHolder.getConnectedZK().create(nodeName, data, ACLs.AUTH_ALL_ACL, CreateMode.EPHEMERAL);
			} catch (KeeperException | InterruptedException e) {
				throw new ExceedExpectedZooKeeperException(
						String.format("ex on updateMetrics after NoNodeException, znode [%s]", nodeName), e);
			}
			// log.warn("znode not found on updateMetrics, expect znode:{}", nodeName);
			// continue code ...
		} catch (KeeperException | InterruptedException e) {
			throw new ExceedExpectedZooKeeperException(String.format("ex on updateMetrics znode [%s]", nodeName), e);
		}
	}

	@Override
	public <T extends RegisteredInstance> Metrics getMetrics(T instance) throws ZooKeeperException {
		if (instance == null) {
			return null;
		}
		String path = MetricsServiceNamePath.ensureServiceNamePath(zooKeeperHolder, root, instance.getServiceName());

		String nodeName = path + "/" + instance.getInstanceName();
		try {
			byte[] data = zooKeeperHolder.getConnectedZK().getData(nodeName, false, null);
			Metrics metrics = buildMetrics(instance.getInstanceName(), data);
			return metrics;
		} catch (KeeperException.NoNodeException ignore) {
			return null;
		} catch (KeeperException | InterruptedException e) {
			throw new ExceedExpectedZooKeeperException(String.format("ex on getMetrics znode [%s]", nodeName), e);
		}
	}

	@Override
	public List<Metrics> listNamedObjects(String serviceName) throws IllegalArgumentException, ZooKeeperException {
		if (serviceName == null || serviceName.isEmpty() || serviceName.startsWith("/")) {
			throw new IllegalArgumentException("param name must not empty and not start with /");
		}
		String path = MetricsServiceNamePath.ensureServiceNamePath(zooKeeperHolder, root, serviceName);

		List<String> children;
		try {
			children = zooKeeperHolder.getConnectedZK().getChildren(path, false);
		} catch (KeeperException.NoNodeException e) {
			log.warn("znode not found on listMetrics, expect znode:{}", path);
			return Collections.emptyList();
		} catch (KeeperException | InterruptedException e) {
			throw new ExceedExpectedZooKeeperException(String.format("ex on list instances where path [%s]", path), e);
		}

		List<Metrics> instances = children.stream().map(child -> {
			String nodeName = path + "/" + child;
			try {
				byte[] data = zooKeeperHolder.getConnectedZK().getData(nodeName, false, null);
				Metrics metrics = buildMetrics(child, data);
				return metrics;
			} catch (KeeperException | InterruptedException e) {
				/**
				 * 跳过出错的实例
				 */
				log.error("WARNING ex on getData for listMetrics where znode:{}", nodeName, e);
				return null;
//					throw new ZooKeeperExceedExpectedException(
//							String.format("ex on getData for listMetrics where znode [%s]", nodeName), e);
			}
		}).filter(m -> m != null).collect(Collectors.toList());

		return instances;
	}

	private Metrics buildMetrics(String instanceName, byte[] data) {
		Metrics metrics;
		try {
			metrics = (Metrics) deserializer.deserialize(data);
		} catch (Exception e) {
			/**
			 * 序列化不兼容，虚构
			 */
			List<Dimension> dimensions = new LinkedList<Dimension>();
			/*
			 * cpu使用率，1.0表示100%
			 */
			double cpuMax = 1.0;
			int cpuWeight = 0;// 权重0表示不使用
			Dimension cpuD = new Metrics.Dimension(DimensionName.Cpu, cpuMax, 0, cpuWeight);
			dimensions.add(cpuD);

			/*
			 * 单位MB
			 */
			double memoryMax = 1 * 1024 * 1024 * 1024;
			int memoryWeight = 0;// 权重0表示不使用
			Dimension memoryD = new Metrics.Dimension(DimensionName.Memory, memoryMax, 0, memoryWeight);
			dimensions.add(memoryD);

			/*
			 * jobs
			 */
			Dimension jobsD = new Metrics.Dimension(DimensionName.Jobs, 1000, 0, 1);
			dimensions.add(jobsD);

			Map<String, Serializable> descMap = new HashMap<String, Serializable>();
			descMap.put("cpuCores", 1);
			descMap.put("physicalMemory", memoryMax);

			metrics = new Metrics(dimensions);
			metrics.setDesc(JsonUtils.serialize(descMap));
		}

		metrics.setInstanceName(instanceName);
		return metrics;
	}

	@Override
	public <T extends RegisteredInstance> void removeMetrics(T instance) throws ZooKeeperException {
		String znode = MetricsServiceNamePath.buildServiceNamePath(root, instance.getServiceName()) + "/"
				+ instance.getInstanceName();

		try {
			Stat stat = zooKeeperHolder.getConnectedZK().exists(znode, false);
			if (stat != null) {
				try {
					zooKeeperHolder.getConnectedZK().delete(znode, stat.getVersion());
				} catch (KeeperException.NoNodeException ignore) {
				}
			}
		} catch (KeeperException | InterruptedException ignore) {
			throw new ExceedExpectedZooKeeperException(String.format("ex on removeMetrics znode [%s]", znode), ignore);
		}
	}

	@Override
	public void close() throws IOException {
		/**
		 * 没有需要处理的
		 */
	}
}