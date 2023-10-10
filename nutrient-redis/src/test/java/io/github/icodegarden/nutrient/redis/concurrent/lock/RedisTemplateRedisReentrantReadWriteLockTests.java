package io.github.icodegarden.nutrient.redis.concurrent.lock;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.RedisTemplateRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.spring.RedisTemplateRedisExecutor;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class RedisTemplateRedisReentrantReadWriteLockTests extends RedisReentrantReadWriteLockTests {

	/**
	 * lettuce的客户端是基于netty，一般只需要1个client即可，建多了会占很多资源导致selector打不开
	 */
	private static RedisExecutor redisExecutor = new RedisTemplateRedisExecutor(RedisTemplateRedisExecutorTests.newRedisTemplate());
	
	protected RedisExecutor newRedisExecutor() {
		return redisExecutor;
	}
}