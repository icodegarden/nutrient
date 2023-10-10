package io.github.icodegarden.nutrient.test.limiter;

import io.github.icodegarden.nutrient.lang.limiter.CounterRateLimiter;
import io.github.icodegarden.nutrient.lang.limiter.RateLimiter;
import io.github.icodegarden.nutrient.test.limiter.AbstractCounterRateLimiterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class CounterRateLimiterTests extends AbstractCounterRateLimiterTests {

	@Override
	protected RateLimiter newCounterRateLimiter(int count, long interval) {
		return new CounterRateLimiter(4, 1000);
	}
}
