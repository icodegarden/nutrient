package io.github.icodegarden.nutrient.lang.registry;

import java.util.List;

/**
 * 
 * @author Fangfang.Xu
 *
 * @param <R>
 */
public interface Discovery<R extends Registration> {

	boolean isRegistered(R registration);

	List<R> listInstances(String name);
}
