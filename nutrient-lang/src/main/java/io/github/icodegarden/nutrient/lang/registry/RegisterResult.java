package io.github.icodegarden.nutrient.lang.registry;

import lombok.Getter;
import lombok.ToString;

/**
 * 
 * @author Fangfang.Xu
 *
 */
@Getter
@ToString
public class RegisterResult {

	private final Integer index;

	public RegisterResult(Integer index) {
		super();
		this.index = index;
	}
}
