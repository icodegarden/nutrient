package io.github.icodegarden.nutrient.lang.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class ReactiveUtils {

	public static <T> T block(Mono<T> mono) {
		return block(mono, null);
	}

	public static <T> T block(Mono<T> mono, Long blockTimeoutMs) {
		AtomicReference<Throwable> referenceE = new AtomicReference<>();
		AtomicReference<T> reference = new AtomicReference<>();

		Thread thread = Thread.currentThread();

		mono.doOnError(e -> {
			referenceE.set(e);

			if (blockTimeoutMs != null) {
				LockSupport.unpark(thread);
			}
		})//
				.subscribeOn(Schedulers.immediate())//
				.subscribe(next -> {
					reference.set(next);

					if (blockTimeoutMs != null) {
						LockSupport.unpark(thread);
					}
				});

		if (blockTimeoutMs != null) {
			LockSupport.parkUntil(SystemUtils.currentTimeMillis() + blockTimeoutMs);
		}

		if (referenceE.get() != null) {
			throw new RuntimeException(referenceE.get());
		}
		return reference.get();
	}
}