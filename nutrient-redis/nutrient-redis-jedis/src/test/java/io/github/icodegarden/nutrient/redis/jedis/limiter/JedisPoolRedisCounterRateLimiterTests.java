package io.github.icodegarden.nutrient.redis.jedis.limiter;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.limiter.RedisCounterRateLimiterTests;

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
