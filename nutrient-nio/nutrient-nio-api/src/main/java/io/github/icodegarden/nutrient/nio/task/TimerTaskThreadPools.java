package io.github.icodegarden.nutrient.nio.task;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import io.github.icodegarden.nutrient.lang.concurrent.NamedThreadFactory;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class TimerTaskThreadPools {

	public static final ScheduledThreadPoolExecutor newScheduledThreadPool(int corePoolSize, String namePrefix) {
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(corePoolSize,
				new NamedThreadFactory(namePrefix));
		scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
		return scheduledThreadPoolExecutor;
	}

}
