package io.github.icodegarden.nutrient.redis.test.concurrent.lock;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedReentrantLock;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.concurrent.lock.RedisReentrantLock;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedReentrantLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class RedisReentrantLockTests extends DistributedReentrantLockTests {

	@Override
	protected DistributedReentrantLock newLock(String name) {
		return new RedisReentrantLock(newRedisExecutor(), name, 5L);
	}

	protected abstract RedisExecutor newRedisExecutor();
}
