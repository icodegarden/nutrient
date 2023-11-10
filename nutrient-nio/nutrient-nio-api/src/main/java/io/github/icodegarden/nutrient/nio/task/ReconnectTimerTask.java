package io.github.icodegarden.nutrient.nio.task;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.github.icodegarden.nutrient.nio.health.Heartbeat;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端主动发起
 * 
 * @author Fangfang.Xu
 *
 */
@Slf4j
public class ReconnectTimerTask {

	private AtomicLong counter = new AtomicLong();

	private long heartbeatIntervalMillis;
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = TimerTaskThreadPools
			.newScheduledThreadPool(1/* 检测是轻量的，C端不会有很多client */, ReconnectTimerTask.class.getSimpleName());

	public static final ReconnectTimerTask DEFAULT = new ReconnectTimerTask(HeartbeatTimerTask.DEFAULT_INTERVAL_MILLIS);

	public ReconnectTimerTask(long heartbeatIntervalMillis) {
		this.heartbeatIntervalMillis = heartbeatIntervalMillis;
	}

	/**
	 * <p>
	 * 以heartbeatIntervalMillis的间隔进行检查
	 * <p>
	 * 当最近一次收到的时间超过了heartbeatIntervalMillis*3，则自动重连
	 * 
	 * @param heartbeat
	 */
	public ScheduleCancelableRunnable register(Heartbeat heartbeat) {
		ScheduleCancelableRunnable scheduleCancelableRunnable = new ScheduleCancelableRunnable(
				"ReconnectTimerTask-" + heartbeat.toString(), scheduledThreadPoolExecutor) {
			@Override
			public void run() {
				try {
					if (counter.incrementAndGet() % 100 == 0) {
						if (log.isInfoEnabled()) {
							log.info("total Reconnect Tasks:{}", scheduledThreadPoolExecutor.getQueue().size());
						}
					}

					boolean shouldReconnect = heartbeat.shouldReconnect(heartbeatIntervalMillis);

					if (shouldReconnect) {
						try {
							heartbeat.reconnect();
						} catch (Throwable e) {
							/**
							 * 只尝试1次重连，失败则关闭client<br>
							 * 这里只需要只需close不需要cancel任务，cancel任务会在close client时自动触发
							 */
							log.error("reconnect failed, will close client.", e);
							try {
								heartbeat.close();
							} catch (IOException ignore) {
								log.error("WARN ex on close client", ignore);
							}
						}
					}
				} catch (Throwable e) {
					log.error("ex on run ReconnectTimerTask", e);
				}
			}
		};
		scheduleCancelableRunnable.scheduleWithFixedDelay(heartbeatIntervalMillis, heartbeatIntervalMillis,
				TimeUnit.MILLISECONDS);
		return scheduleCancelableRunnable;
	}

	public void shutdown() {
		scheduledThreadPoolExecutor.shutdown();
	}
}
