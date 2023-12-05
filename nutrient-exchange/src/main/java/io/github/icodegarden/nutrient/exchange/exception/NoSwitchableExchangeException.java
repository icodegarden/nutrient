package io.github.icodegarden.nutrient.exchange.exception;

import java.util.Collection;

import io.github.icodegarden.nutrient.exchange.loadbalance.MetricsInstance;

/**
 * 实例交互失败且没有切换实例的必要
 * 
 * @author Fangfang.Xu
 *
 */
public class NoSwitchableExchangeException extends ExchangeException {
	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "No Switchable";

	public NoSwitchableExchangeException(String statusCode, Collection<MetricsInstance> candidates,
			Collection<ExchangeFailedInstance> exchangedInstances) {
		super(statusCode, MESSAGE, candidates, exchangedInstances);
	}

}