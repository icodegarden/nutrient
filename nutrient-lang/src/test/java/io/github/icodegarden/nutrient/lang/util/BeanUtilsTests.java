package io.github.icodegarden.nutrient.lang.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class BeanUtilsTests {

	class UserParent {
		protected String pname;
	}

	class User extends UserParent {
		private Long id;
		private String name;
	}

	@Test
	void anyFieldHasValue() throws Exception {
		User user = new User();
		assertThat(BeanUtils.anyFieldHasValue(user, null)).isFalse();

		user.id = 100L;
		assertThat(BeanUtils.anyFieldHasValue(user, null)).isTrue();
		assertThat(BeanUtils.anyFieldHasValue(user, Arrays.asList("id"))).isFalse();
	}

	@Test
	void allFieldHasValue() throws Exception {
		User user = new User();
		assertThat(BeanUtils.allFieldHasValue(user, null)).isFalse();

		user.id = 100L;
		user.name = "n1";
		assertThat(BeanUtils.allFieldHasValue(user, null)).isFalse();
		assertThat(BeanUtils.allFieldHasValue(user, Arrays.asList("pname"))).isTrue();

		user.pname = "n2";
		assertThat(BeanUtils.allFieldHasValue(user, null)).isTrue();
	}
}
