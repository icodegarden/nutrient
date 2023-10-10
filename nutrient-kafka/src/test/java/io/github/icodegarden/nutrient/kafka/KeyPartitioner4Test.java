package io.github.icodegarden.nutrient.kafka;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class KeyPartitioner4Test implements Partitioner {
	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		Integer numPartitions = cluster.partitionCountForTopic(topic);
		try {
			return Math.abs(Integer.parseInt((String) key) % numPartitions);
		} catch (Exception e) {
			return Math.abs(key.hashCode() % numPartitions);
		}
	}

	@Override
	public void close() {
	}
}