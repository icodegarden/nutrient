package io.github.icodegarden.nutrient.lang.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class RegistryGracefullyShutdown implements GracefullyShutdown {

	private static final Logger log = LoggerFactory.getLogger(RegistryGracefullyShutdown.class);

	private int gracefullyShutdownOrder = Integer.MIN_VALUE;// 优先级最高

	private final io.github.icodegarden.nutrient.lang.registry.Registry registry;

	public RegistryGracefullyShutdown(io.github.icodegarden.nutrient.lang.registry.Registry registry) {
		this.registry = registry;
	}

	public void setGracefullyShutdownOrder(int gracefullyShutdownOrder) {
		this.gracefullyShutdownOrder = gracefullyShutdownOrder;
	}

	@Override
	public String shutdownName() {
		return "icodegarden-Registry";
	}

	@Override
	public void shutdown() {
		log.info("do icodegarden Registry graceful shutdown...");
		registry.close();
	}

	@Override
	public int shutdownOrder() {
		return gracefullyShutdownOrder;
	}
}