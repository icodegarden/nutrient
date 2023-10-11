package io.github.icodegarden.nutrient.redis.spring.concurrent.lock;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.spring.RedisTemplateRedisExecutor;
import io.github.icodegarden.nutrient.redis.spring.RedisTemplateRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.concurrent.lock.RedisReentrantLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class RedisTemplateRedisReentrantLockTests extends RedisReentrantLockTests {

	/**
	 * lettuce的客户端是基于netty，一般只需要1个client即可，建多了会占很多资源导致selector打不开
	 */
	private static RedisExecutor redisExecutor = new RedisTemplateRedisExecutor(RedisTemplateRedisExecutorTests.newRedisTemplate());
	
	protected RedisExecutor newRedisExecutor() {
		return redisExecutor;
	}
}