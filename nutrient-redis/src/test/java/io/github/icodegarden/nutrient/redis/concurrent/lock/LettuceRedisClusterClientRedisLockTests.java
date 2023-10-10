package io.github.icodegarden.nutrient.redis.concurrent.lock;

import io.github.icodegarden.nutrient.redis.LettuceRedisClusterClientRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.lettuce.LettuceRedisClusterClientRedisExecutor;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class LettuceRedisClusterClientRedisLockTests extends RedisLockTests {

	/**
	 * lettuce的客户端是基于netty，一般只需要1个client即可，建多了会占很多资源导致selector打不开
	 */
	private static RedisExecutor redisExecutor = new LettuceRedisClusterClientRedisExecutor(
			LettuceRedisClusterClientRedisExecutorTests.newRedisClusterClient());
	
	protected RedisExecutor newRedisExecutor() {
		return redisExecutor;
	}
}
