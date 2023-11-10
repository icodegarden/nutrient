package io.github.icodegarden.nutrient.nio.task;

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
public class HeartbeatTimerTask {

	private AtomicLong counter = new AtomicLong();

	public static final long DEFAULT_INTERVAL_MILLIS = 60000;
	private long heartbeatIntervalMillis;
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = TimerTaskThreadPools.newScheduledThreadPool(2,
			HeartbeatTimerTask.class.getSimpleName());

	public static final HeartbeatTimerTask DEFAULT = new HeartbeatTimerTask(DEFAULT_INTERVAL_MILLIS);

	public HeartbeatTimerTask(long heartbeatIntervalMillis) {
		this.heartbeatIntervalMillis = heartbeatIntervalMillis;
	}

	public ScheduleCancelableRunnable register(Heartbeat heartbeat) {
		ScheduleCancelableRunnable scheduleCancelableRunnable = new ScheduleCancelableRunnable(
				"HeartbeatTimerTask-" + heartbeat.toString(), scheduledThreadPoolExecutor) {
			@Override
			public void run() {
				try {
					if (counter.incrementAndGet() % 100 == 0) {
						if (log.isInfoEnabled()) {
							log.info("total Heartbeat Tasks:{}", scheduledThreadPoolExecutor.getQueue().size());
						}
					}

					heartbeat.send();
				} catch (Throwable e) {
					log.error("WARN heartbeat:{} send beat occur ex", heartbeat, e);
				}
			}
		};
//		scheduleCancelableRunnable.scheduleWithFixedDelay(0, heartbeatIntervalMillis, TimeUnit.MILLISECONDS);
		/**
		 * 初始delay heartbeatIntervalMillis而不是0
		 */
		scheduleCancelableRunnable.scheduleWithFixedDelay(heartbeatIntervalMillis, heartbeatIntervalMillis,
				TimeUnit.MILLISECONDS);
		return scheduleCancelableRunnable;
	}
}
