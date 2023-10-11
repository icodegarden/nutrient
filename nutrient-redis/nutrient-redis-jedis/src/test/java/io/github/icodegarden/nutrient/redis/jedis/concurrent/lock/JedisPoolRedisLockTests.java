package io.github.icodegarden.nutrient.redis.jedis.concurrent.lock;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.concurrent.lock.RedisLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisPoolRedisLockTests extends RedisLockTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
	}
}
