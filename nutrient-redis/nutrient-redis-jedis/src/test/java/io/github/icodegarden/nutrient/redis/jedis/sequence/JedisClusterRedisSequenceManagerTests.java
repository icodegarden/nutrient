package io.github.icodegarden.nutrient.redis.jedis.sequence;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.sequence.RedisSequenceManagerTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisClusterRedisSequenceManagerTests extends RedisSequenceManagerTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisClusterRedisExecutor(JedisClusterRedisExecutorTests.newJedisCluster());
	}
}
