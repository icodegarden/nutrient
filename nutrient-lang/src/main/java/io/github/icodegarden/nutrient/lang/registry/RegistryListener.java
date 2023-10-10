package io.github.icodegarden.nutrient.lang.registry;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface RegistryListener {

	/**
	 * 注册成功
	 */
	void onRegistered(Registration registration, Integer index);
	
	/**
	 * 未能及时更新租期导致过期
	 */
	void onLeaseExpired(Registration registration);
}
