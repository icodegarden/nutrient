package io.github.icodegarden.nutrient.exchange;

import io.github.icodegarden.nutrient.exchange.exception.ExchangeFailedReason;
import lombok.Getter;
import lombok.ToString;

/**
 * 该对象可以用于表示 成功或失败
 * @author Fangfang.Xu
 *
 */
@Getter
@ToString
public class ReasonExchangeResult {

	private final boolean success;

	private Object result;
	
	private ExchangeFailedReason exchangeFailedReason;

	public ReasonExchangeResult(boolean success, Object result, ExchangeFailedReason exchangeFailedReason) {
		this.success = success;
		this.result = result;
		this.exchangeFailedReason = exchangeFailedReason;
	}
}