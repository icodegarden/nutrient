package io.github.icodegarden.nutrient.redis.lettuce.sequence;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.lettuce.LettuceRedisClusterClientRedisExecutor;
import io.github.icodegarden.nutrient.redis.lettuce.LettuceRedisClusterClientRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.sequence.RedisSequenceManagerTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class LettuceRedisClusterClientRedisSequenceManagerTests extends RedisSequenceManagerTests {

	/**
	 * lettuce的客户端是基于netty，一般只需要1个client即可，建多了会占很多资源导致selector打不开
	 */
	private static RedisExecutor redisExecutor = new LettuceRedisClusterClientRedisExecutor(
			LettuceRedisClusterClientRedisExecutorTests.newRedisClusterClient());
	
	protected RedisExecutor newRedisExecutor() {
		return redisExecutor;
	}
}
