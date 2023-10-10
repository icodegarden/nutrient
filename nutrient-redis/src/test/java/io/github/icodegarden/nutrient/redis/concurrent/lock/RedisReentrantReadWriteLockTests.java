package io.github.icodegarden.nutrient.redis.concurrent.lock;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedReentrantReadWriteLock;
import io.github.icodegarden.nutrient.redis.JedisPoolRedisExecutorTests;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.concurrent.lock.RedisReentrantReadWriteLock;
import io.github.icodegarden.nutrient.redis.jedis.JedisPoolRedisExecutor;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedReentrantReadWriteLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class RedisReentrantReadWriteLockTests extends DistributedReentrantReadWriteLockTests {

	protected abstract RedisExecutor newRedisExecutor();

	@BeforeEach
	void initClient() throws IOException {
		RedisExecutor redisExecutor = new JedisPoolRedisExecutor(JedisPoolRedisExecutorTests.newJedisPool());
		
		Set<byte[]> keys = redisExecutor.keys("*".getBytes());
		keys.forEach(key -> {
			redisExecutor.del(key);
		});

		redisExecutor.close();
	}

	@AfterEach
	void closeClient() {
	}

	@Override
	protected DistributedReentrantReadWriteLock newLock(String name) {
		RedisExecutor redisExecutor = newRedisExecutor();
		return new RedisReentrantReadWriteLock(redisExecutor, name, 5L);
	}

}
