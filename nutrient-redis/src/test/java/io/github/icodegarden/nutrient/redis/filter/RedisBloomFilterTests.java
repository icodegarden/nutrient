package io.github.icodegarden.nutrient.redis.filter;

import org.junit.jupiter.api.BeforeEach;

import io.github.icodegarden.nutrient.lang.filter.AbstractBloomFilter;
import io.github.icodegarden.nutrient.redis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.filter.RedisBloomFilter;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;
import io.github.icodegarden.nutrient.test.filter.AbstractBloomFilterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public class RedisBloomFilterTests extends AbstractBloomFilterTests {

	@BeforeEach
	void init() {
		RedisExecutor redisExecutor = new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
		redisExecutor.del("RedisBloomFilterTests".getBytes());
	}

	@Override
	protected AbstractBloomFilter newBloomFilter(int count) {
		return new RedisBloomFilter("RedisBloomFilterTests",
				new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool()));
	}

}
