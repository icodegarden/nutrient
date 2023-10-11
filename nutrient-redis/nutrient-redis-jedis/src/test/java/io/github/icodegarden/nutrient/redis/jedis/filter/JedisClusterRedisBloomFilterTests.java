package io.github.icodegarden.nutrient.redis.jedis.filter;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.filter.RedisBloomFilterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisClusterRedisBloomFilterTests extends RedisBloomFilterTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisClusterRedisExecutor(JedisClusterRedisExecutorTests.newJedisCluster());
	}
}
