package io.github.icodegarden.nutrient.redis.spring.filter;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.filter.RedisBloomFilterTests;
import io.github.icodegarden.nutrient.redis.spring.RedisTemplateRedisExecutor;
import io.github.icodegarden.nutrient.redis.spring.RedisTemplateRedisExecutorTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class RedisTemplateRedisBloomFilterTests extends RedisBloomFilterTests {

	/**
	 * lettuce的客户端是基于netty，一般只需要1个client即可，建多了会占很多资源导致selector打不开
	 */
	private static RedisExecutor redisExecutor = new RedisTemplateRedisExecutor(RedisTemplateRedisExecutorTests.newRedisTemplate());
	
	protected RedisExecutor newRedisExecutor() {
		return redisExecutor;
	}
}