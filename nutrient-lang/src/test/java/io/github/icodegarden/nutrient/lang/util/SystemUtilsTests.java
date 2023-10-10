package io.github.icodegarden.nutrient.lang.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.github.icodegarden.nutrient.lang.util.SystemUtils;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class SystemUtilsTests {
	
	@Test
	void getIp() throws Exception {
		assertThat(SystemUtils.getIp()).isNotNull();
	}

	@Test
	void getIpv4s() throws Exception {
		assertThat(SystemUtils.getIpv4s()).isNotEmpty();
	}
	
	@Test
	void getIpv6s() throws Exception {
		assertThat(SystemUtils.getIpv6s()).isNotNull();
	}
	
	@Test
	void getIps() throws Exception {
		assertThat(SystemUtils.getIps()).isNotEmpty();
	}
}
