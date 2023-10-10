package io.github.icodegarden.nutrient.lang.filter;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.icodegarden.nutrient.lang.filter.BlackListFilter;

/**
 * 
 * @author Fangfang.Xu
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public class BlackListFilterTests {

	protected String key = "key";
	protected String key2 = "key2";

	@Test
	public void filter() throws Exception {
		BlackListFilter<String> filter = new BlackListFilter<String>(Arrays.asList("key"));

		boolean b = filter.filter(key2);
		Assertions.assertThat(b).isTrue();

		b = filter.filter(key);
		Assertions.assertThat(b).isFalse();
	}
}
