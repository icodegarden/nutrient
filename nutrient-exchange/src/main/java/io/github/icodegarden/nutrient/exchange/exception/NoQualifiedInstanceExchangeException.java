package io.github.icodegarden.nutrient.exchange.exception;

import java.util.Collection;

import io.github.icodegarden.nutrient.exchange.loadbalance.MetricsInstance;

/**
 * 没有合格的候选实例
 * 
 * @author Fangfang.Xu
 *
 */
public class NoQualifiedInstanceExchangeException extends ExchangeException {
	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "No Qualified Instance";

	public NoQualifiedInstanceExchangeException(Collection<MetricsInstance> candidates) {
		super("417", MESSAGE, candidates, null);
	}

}