package io.github.icodegarden.nutrient.redis.concurrent.lock;

import io.github.icodegarden.nutrient.redis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisPoolRedisReentrantLockTests extends RedisReentrantLockTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
	}
}
