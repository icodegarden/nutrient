package io.github.icodegarden.nutrient.redis.jedis.limiter;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisClusterRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.limiter.RedisCounterRateLimiterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisClusterRedisCounterRateLimiterTests extends RedisCounterRateLimiterTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisClusterRedisExecutor(JedisClusterRedisExecutorTests.newJedisCluster());
	}
}
