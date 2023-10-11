package io.github.icodegarden.nutrient.redis.lettuce.concurrent.lock;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.lettuce.LettuceRedisClientRedisExecutor;
import io.github.icodegarden.nutrient.redis.lettuce.LettuceRedisClientRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.concurrent.lock.RedisLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class LettuceRedisClientRedisLockTests extends RedisLockTests {

	/**
	 * lettuce的客户端是基于netty，一般只需要1个client即可，建多了会占很多资源导致selector打不开
	 */
	private static RedisExecutor redisExecutor = new LettuceRedisClientRedisExecutor(LettuceRedisClientRedisExecutorTests.newRedisClient());
	
	protected RedisExecutor newRedisExecutor() {
		return redisExecutor;
	}
}
