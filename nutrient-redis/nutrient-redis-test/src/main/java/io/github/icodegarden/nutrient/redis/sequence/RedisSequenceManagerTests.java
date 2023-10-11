package io.github.icodegarden.nutrient.redis.sequence;

import io.github.icodegarden.nutrient.lang.sequence.SequenceManager;
import io.github.icodegarden.nutrient.redis.RedisExecutor;
import io.github.icodegarden.nutrient.redis.sequence.RedisSequenceManager;
import io.github.icodegarden.nutrient.test.sequence.SequenceManagerTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class RedisSequenceManagerTests extends SequenceManagerTests {

	@Override
	protected SequenceManager getForOneProcess() {
		return newSequenceManager();
	}

	@Override
	protected SequenceManager newForMultiProcess() {
		return newSequenceManager();
	}

	private SequenceManager newSequenceManager() {
		return new RedisSequenceManager("GLOBAL", newRedisExecutor(), 100);
	}

	protected abstract RedisExecutor newRedisExecutor();
}
