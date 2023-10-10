package io.github.icodegarden.nutrient.exchange.loadbalance;

import java.util.LinkedList;
import java.util.Queue;

import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics;
import io.github.icodegarden.nutrient.lang.metricsregistry.Metrics.DimensionName;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class Constants {

	static final Queue<MetricsInstance> EMPTY_METRICS_INSTANCE = new LinkedList<MetricsInstance>();
	static final Metrics IGNORE_METRICS = new Metrics(new Metrics.Dimension(new DimensionName("ignore"), 1, 0));
}
