package io.github.icodegarden.nutrient.nio.task;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.github.icodegarden.nutrient.nio.health.Heartbeat;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端检测。heartbeat*3检测一次,超时没有来自客户端的心跳，则触发关闭
 * 
 * @author Fangfang.Xu
 *
 */
@Slf4j
public class IdleStateTimerTask {

	private AtomicLong counter = new AtomicLong();

	private long heartbeatIntervalMillis;
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = TimerTaskThreadPools
			.newScheduledThreadPool(1/* idle检测任务是轻量的 */, IdleStateTimerTask.class.getSimpleName());

	public static final IdleStateTimerTask DEFAULT = new IdleStateTimerTask(HeartbeatTimerTask.DEFAULT_INTERVAL_MILLIS);

	public IdleStateTimerTask(long heartbeatIntervalMillis) {
		this.heartbeatIntervalMillis = heartbeatIntervalMillis;
	}

	public ScheduleCancelableRunnable register(Heartbeat heartbeat) {
		ScheduleCancelableRunnable scheduleCancelableRunnable = new ScheduleCancelableRunnable(
				"IdleStateTimerTask-" + heartbeat.toString(), scheduledThreadPoolExecutor) {
			@Override
			public void run() {
				try {
					if (counter.incrementAndGet() % 100 == 0) {
						if (log.isInfoEnabled()) {
							log.info("total IdleState Tasks:{}", scheduledThreadPoolExecutor.getQueue().size());
						}
					}

					long lastReceive = heartbeat.lastReceive();
					if (log.isDebugEnabled()) {
						log.debug("IdleStateTimerTask of heartbeat:{} run at {},from lastReceive:{}", heartbeat,
								System.currentTimeMillis(), (System.currentTimeMillis() - lastReceive));
					}
					if (System.currentTimeMillis() - lastReceive >= heartbeatIntervalMillis * 3) {
						try {
							if (log.isInfoEnabled()) {
								log.info("IdleStateTimerTask of heartbeat:{} was timeout,close...", heartbeat);
							}
							heartbeat.close();
						} catch (Throwable e) {
							log.error("close that heartbeat:{} timeouted occur ex", heartbeat, e);
						}
						cancel();
					}
				} catch (Throwable e) {
					log.error("ex on run IdleStateTimerTask", e);
				}
			}
		};
		scheduleCancelableRunnable.scheduleWithFixedDelay(heartbeatIntervalMillis * 3, heartbeatIntervalMillis * 3,
				TimeUnit.MILLISECONDS);
		return scheduleCancelableRunnable;
	}

	public void shutdown() {
		scheduledThreadPoolExecutor.shutdown();
	}
}
