package io.github.icodegarden.nutrient.exchange.exception;

import java.util.Collection;
import java.util.Collections;

import io.github.icodegarden.nutrient.exchange.loadbalance.MetricsInstance;

/**
 * 请求端发起请求时被拒绝，例如线程池驳回
 * 
 * @author Fangfang.Xu
 *
 */
public class RequesterRejectedExchangeException extends ExchangeException {
	private static final long serialVersionUID = 1L;

	public static final String MESSAGE = "Requester Rejected";

	public RequesterRejectedExchangeException(String reason, Collection<MetricsInstance> candidates) {
		super("403", MESSAGE + "-" + reason, candidates, Collections.emptyList());
	}

}