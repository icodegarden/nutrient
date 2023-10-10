package io.github.icodegarden.nutrient.exchange.loadbalance;

import io.github.icodegarden.nutrient.lang.annotation.Nullable;
import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics;
import io.github.icodegarden.nutrient.lang.metricsregistry.RegisteredInstance;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface MetricsInstance {

	@Nullable
	RegisteredInstance getAvailable();

	@Nullable
	Metrics getMetrics();

	boolean isOverload();

}
