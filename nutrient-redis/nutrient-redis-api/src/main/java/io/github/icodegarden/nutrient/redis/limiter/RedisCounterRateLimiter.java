package io.github.icodegarden.nutrient.redis.limiter;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.util.Assert;

import io.github.icodegarden.nutrient.lang.limiter.CounterRateLimiterSupport;
import io.github.icodegarden.nutrient.redis.RedisExecutor;

/**
 * 作用参考 {@link io.github.icodegarden.nutrient.lang.limiter.CounterRateLimiter}
 * 
 * @author Fangfang.Xu
 *
 */
public class RedisCounterRateLimiter extends CounterRateLimiterSupport {

	private static final Charset CHARSET = Charset.forName("utf-8");

	private static final byte[] SETBATCH_SCRIPT = "local v = redis.call('get',KEYS[1]);redis.call('decrBy',KEYS[1],ARGV[1]);return v;"
			.getBytes(CHARSET);

	private byte[] count;

	private byte[] key;

	private RedisExecutor redisExecutor;

	/**
	 * 
	 * @param key      redis计数的key
	 * @param count    在给定的interval中允许的次数
	 * @param interval 计数间隔millis
	 */
	public RedisCounterRateLimiter(RedisExecutor redisExecutor, String key, int count, long interval) {
		super(interval);

		Assert.notNull(redisExecutor, "redisExecutor must not null");
		Assert.hasText(key, "key must not empty");
		if (count <= 0) {
			throw new IllegalArgumentException("count must gt 0");
		}
		if (interval <= 0) {
			throw new IllegalArgumentException("interval must gt 0");
		}
		this.redisExecutor = redisExecutor;
		this.key = key.getBytes(CHARSET);
		this.count = Integer.toString(count).getBytes(CHARSET);
	}

	@Override
	protected void resetToken() {
		redisExecutor.set(key, count);
	}

	@Override
	protected int getAndDecrement(int value) {
		/**
		 * 直接使用decrBy得到的是减去后的结果，相当于decrementAndGet
		 */
		
		byte[] valuebs = Integer.valueOf(value).toString().getBytes(CHARSET);
		//这里返回类型是bytes的原因是value是主动设置进去的，设置进去时是bytes
		List<Object> list = redisExecutor.eval(SETBATCH_SCRIPT, 1, key,valuebs);
		byte[] bs = (byte[])list.get(0);

		String str = new String(bs, CHARSET);
		int v = Integer.parseInt(str);
		return v;
	}
}