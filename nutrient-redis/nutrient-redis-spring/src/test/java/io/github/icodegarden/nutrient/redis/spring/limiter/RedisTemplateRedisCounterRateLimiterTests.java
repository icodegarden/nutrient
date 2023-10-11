package io.github.icodegarden.nutrient.redis.spring.limiter;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.spring.RedisTemplateRedisExecutor;
import io.github.icodegarden.nutrient.redis.spring.RedisTemplateRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.limiter.RedisCounterRateLimiterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class RedisTemplateRedisCounterRateLimiterTests extends RedisCounterRateLimiterTests {

	/**
	 * lettuce的客户端是基于netty，一般只需要1个client即可，建多了会占很多资源导致selector打不开
	 */
	private static RedisExecutor redisExecutor = new RedisTemplateRedisExecutor(RedisTemplateRedisExecutorTests.newRedisTemplate());
	static {
		redisExecutor.get("".getBytes());//先执行一下初始化连接，以免造成首次慢影响测试结果
	}
	
	protected RedisExecutor newRedisExecutor() {
		return redisExecutor;
	}
}