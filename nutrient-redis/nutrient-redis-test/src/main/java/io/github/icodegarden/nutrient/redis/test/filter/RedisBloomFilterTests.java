package io.github.icodegarden.nutrient.redis.test.filter;

import org.junit.jupiter.api.BeforeEach;

import io.github.icodegarden.nutrient.lang.filter.AbstractBloomFilter;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.filter.RedisBloomFilter;
import io.github.icodegarden.nutrient.test.filter.AbstractBloomFilterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public abstract class RedisBloomFilterTests extends AbstractBloomFilterTests {

	protected abstract RedisExecutor newRedisExecutor();
	
	@BeforeEach
	void init() {
//		RedisExecutor redisExecutor = new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
		RedisExecutor redisExecutor = newRedisExecutor();
		redisExecutor.del("RedisBloomFilterTests".getBytes());
	}

	@Override
	protected AbstractBloomFilter newBloomFilter(int count) {
//		return new RedisBloomFilter("RedisBloomFilterTests", new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool()));
		return new RedisBloomFilter("RedisBloomFilterTests", newRedisExecutor());
	}

}
