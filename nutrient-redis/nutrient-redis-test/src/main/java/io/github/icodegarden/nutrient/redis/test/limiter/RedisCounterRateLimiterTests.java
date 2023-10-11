package io.github.icodegarden.nutrient.redis.test.limiter;

import io.github.icodegarden.nutrient.lang.limiter.RateLimiter;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.limiter.RedisCounterRateLimiter;
import io.github.icodegarden.nutrient.test.limiter.AbstractCounterRateLimiterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class RedisCounterRateLimiterTests extends AbstractCounterRateLimiterTests {

	@Override
	protected RateLimiter newCounterRateLimiter(int count, long interval) {
		return new RedisCounterRateLimiter(newRedisExecutor(), "RedisCounterRateLimiter", count, interval);
	}

	protected abstract RedisExecutor newRedisExecutor();
}
