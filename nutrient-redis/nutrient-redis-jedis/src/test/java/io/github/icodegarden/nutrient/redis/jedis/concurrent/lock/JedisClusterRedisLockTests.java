package io.github.icodegarden.nutrient.redis.jedis.concurrent.lock;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.concurrent.lock.RedisLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisClusterRedisLockTests extends RedisLockTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisClusterRedisExecutor(JedisClusterRedisExecutorTests.newJedisCluster());
	}
}
