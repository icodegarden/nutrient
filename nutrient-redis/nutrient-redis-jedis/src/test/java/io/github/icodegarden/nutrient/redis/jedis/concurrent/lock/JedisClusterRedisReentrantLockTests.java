package io.github.icodegarden.nutrient.redis.jedis.concurrent.lock;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.concurrent.lock.RedisReentrantLockTests;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutorTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisClusterRedisReentrantLockTests extends RedisReentrantLockTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisClusterRedisExecutor(JedisClusterRedisExecutorTests.newJedisCluster());
	}
}
