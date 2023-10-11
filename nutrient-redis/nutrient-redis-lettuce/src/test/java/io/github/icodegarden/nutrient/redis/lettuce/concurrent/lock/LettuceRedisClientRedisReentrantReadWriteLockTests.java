package io.github.icodegarden.nutrient.redis.lettuce.concurrent.lock;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.concurrent.lock.RedisReentrantReadWriteLockTests;
import io.github.icodegarden.nutrient.redis.lettuce.LettuceRedisClientRedisExecutor;
import io.github.icodegarden.nutrient.redis.lettuce.LettuceRedisClientRedisExecutorTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class LettuceRedisClientRedisReentrantReadWriteLockTests extends RedisReentrantReadWriteLockTests {

	/**
	 * lettuce的客户端是基于netty，一般只需要1个client即可，建多了会占很多资源导致selector打不开
	 */
	private static RedisExecutor redisExecutor = new LettuceRedisClientRedisExecutor(LettuceRedisClientRedisExecutorTests.newRedisClient());
	
	@Override
	protected RedisExecutor newClearKeysRedisExecutor() {
		return new LettuceRedisClientRedisExecutor(LettuceRedisClientRedisExecutorTests.newRedisClient());
	}
	
	@Override
	protected RedisExecutor newRedisExecutor() {
		return redisExecutor;
	}
}
