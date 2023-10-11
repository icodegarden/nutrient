package io.github.icodegarden.nutrient.redis.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.RedisExecutorTests;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @author Fangfang.Xu
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public class JedisPoolRedisExecutorTests extends RedisExecutorTests {

	@Override
	protected RedisExecutor newInstance() {
		return new JedisPoolRedisExecutor(newJedisPool());
	}

	public static JedisPool newJedisPool() {
		return new JedisPool(new GenericObjectPoolConfig(), "192.168.184.129", 6379, 10000, null);
	}
}
