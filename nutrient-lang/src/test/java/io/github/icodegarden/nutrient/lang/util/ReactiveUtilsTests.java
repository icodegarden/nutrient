package io.github.icodegarden.nutrient.lang.util;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class ReactiveUtilsTests {

	@Test
	void block() throws Exception {
		Mono<String> mono = Mono.just("d1");
		System.out.println(ReactiveUtils.block(mono));

		mono = Mono.just("d2");
		System.out.println(ReactiveUtils.block(mono, 100L));
	}
}
