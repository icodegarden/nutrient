package io.github.icodegarden.nutrient.lang.metricsregistry;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface ScheduleFlushable {

	void enableScheduleFlush(int scheduleMillis);

	void disableScheduleFlush();
	
}
