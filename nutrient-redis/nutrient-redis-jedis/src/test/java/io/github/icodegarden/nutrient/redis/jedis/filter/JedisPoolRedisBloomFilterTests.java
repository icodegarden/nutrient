package io.github.icodegarden.nutrient.redis.jedis.filter;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.test.filter.RedisBloomFilterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JedisPoolRedisBloomFilterTests extends RedisBloomFilterTests {

	protected RedisExecutor newRedisExecutor() {
		return new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
	}
}
