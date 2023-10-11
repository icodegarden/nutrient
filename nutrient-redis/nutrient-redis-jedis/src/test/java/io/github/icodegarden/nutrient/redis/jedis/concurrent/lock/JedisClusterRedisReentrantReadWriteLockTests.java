package io.github.icodegarden.nutrient.redis.jedis.concurrent.lock;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.concurrent.lock.RedisReentrantReadWriteLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisClusterRedisReentrantReadWriteLockTests extends RedisReentrantReadWriteLockTests {

	@Override
	protected RedisExecutor newClearKeysRedisExecutor() {
		return new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
	}
	
	protected RedisExecutor newRedisExecutor() {
		return new JedisClusterRedisExecutor(JedisClusterRedisExecutorTests.newJedisCluster());
	}
}
