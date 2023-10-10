package io.github.icodegarden.nutrient.redis.limiter;

import io.github.icodegarden.nutrient.redis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisPoolRedisCounterRateLimiterTests extends RedisCounterRateLimiterTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
	}
}
