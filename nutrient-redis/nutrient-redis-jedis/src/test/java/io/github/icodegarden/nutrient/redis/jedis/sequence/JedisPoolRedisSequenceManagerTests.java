package io.github.icodegarden.nutrient.redis.jedis.sequence;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.sequence.RedisSequenceManagerTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisPoolRedisSequenceManagerTests extends RedisSequenceManagerTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
	}
}
