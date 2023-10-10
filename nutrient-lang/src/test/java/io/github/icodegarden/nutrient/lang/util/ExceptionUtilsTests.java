package io.github.icodegarden.nutrient.lang.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.github.icodegarden.nutrient.lang.util.ExceptionUtils;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class ExceptionUtilsTests {

	@Test
	void causeOf() throws Exception {
		IllegalArgumentException e1 = new IllegalArgumentException("Missing:name");
		IllegalStateException e2 = new IllegalStateException("forbidon", e1);
		RuntimeException e3 = new RuntimeException("runtime", e2);

		IllegalArgumentException causeOf = ExceptionUtils.causeOf(e3, IllegalArgumentException.class);

		assertThat(causeOf).isNotNull();
		assertThat(causeOf).isEqualTo(e1);
		
		NullPointerException causeOf2 = ExceptionUtils.causeOf(e3, NullPointerException.class);
		assertThat(causeOf2).isNull();
	}
}
