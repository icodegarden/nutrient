package io.github.icodegarden.nutrient.exchange;

import java.io.Serializable;

import io.github.icodegarden.nutrient.lang.annotation.Nullable;

/**
 * 该对象只用于表示成功的结果
 * 
 * @author Fangfang.Xu
 *
 */
public interface ExchangeResult extends Serializable {

	@Nullable
	Object response();
	
}
