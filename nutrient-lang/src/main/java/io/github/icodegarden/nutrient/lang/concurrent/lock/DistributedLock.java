package io.github.icodegarden.nutrient.lang.concurrent.lock;


/**
 * <h1>不支持Reentrant的互斥锁</h1> 全程互斥<br>
 * 
 * @author Fangfang.Xu
 *
 */
public interface DistributedLock {

	boolean isAcquired() throws LockException;

	/**
	 * 阻塞直到获取成功
	 */
	void acquire() throws LockException;

	/**
	 * 阻塞直到获取成功 或 超时
	 * 
	 * @param timeoutMillis 阻塞的时间
	 * @return 是否获取成功
	 */
	boolean acquire(long timeoutMillis) throws LockException;

	void release() throws LockException;
}
