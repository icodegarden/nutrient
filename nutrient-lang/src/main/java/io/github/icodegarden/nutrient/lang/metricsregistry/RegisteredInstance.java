package io.github.icodegarden.nutrient.lang.metricsregistry;

import io.github.icodegarden.nutrient.lang.NamedObject;
import io.github.icodegarden.nutrient.lang.annotation.Nullable;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface RegisteredInstance extends NamedObject {

	String getServiceName();

	String getInstanceName();

	/**
	 * 
	 * @return 例如http,https
	 */
	@Nullable
	default String getScheme() {
		return null;
	}

	String getIp();

	int getPort();

}
