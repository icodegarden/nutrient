package io.github.icodegarden.nutrient.lang.util;

import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * 适用于没有直接引用具有io.github.icodegarden.nursery.springboot.SpringContext包的场景
 * 
 * @author Fangfang.Xu
 *
 */
public class SpringContextUtils {

	private static final Method getApplicationContextMethod;

	static {
		if (!ClassUtils.isPresent("io.github.icodegarden.nursery.springboot.SpringContext", null)) {
			throw new IllegalStateException("SpringContext must present.");
		}
		try {
			Class<?> cla = ClassUtils.forName("io.github.icodegarden.nursery.springboot.SpringContext", null);
			getApplicationContextMethod = cla.getDeclaredMethod("getApplicationContext");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static ApplicationContext getApplicationContext() {
		try {
			return (ApplicationContext) getApplicationContextMethod.invoke(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
