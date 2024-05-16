package io.github.icodegarden.nutrient.lang.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class ClassUtils {

	/**
	 * 获取目标类的接口（可多级）的泛型
	 * 
	 * @param target       目标类
	 * @param interfaceCla 接口
	 * @return 不为null
	 */
	public static List<Class<?>> getGenericInterfaceTypes(Object target, Class<?> interfaceCla) {
		return getGenericInterfaceTypes(target.getClass(), interfaceCla);
	}
	
	/**
	 * 获取目标类的接口（可多级）的泛型
	 * 
	 * @param target       目标类
	 * @param interfaceCla 接口
	 * @return 不为null
	 */
	public static List<Class<?>> getGenericInterfaceTypes(Class<?> target, Class<?> interfaceCla) {
		ParameterizedType pt = getGenericInterfaceParameterizedType(target, interfaceCla);
		if (pt != null) {
			List<Class<?>> list = new LinkedList<Class<?>>();
			for (Type type : pt.getActualTypeArguments()) {
				list.add((Class<?>) type);
			}
			return list;
		}
		return Collections.emptyList();
	}

	public static ParameterizedType getGenericInterfaceParameterizedType(Object target, Class<?> interfaceCla) {
		return getGenericInterfaceParameterizedType(target.getClass(), interfaceCla);
	}

	public static ParameterizedType getGenericInterfaceParameterizedType(Class<?> target, Class<?> interfaceCla) {
		if (target.equals(Object.class)) {
			return null;
		}
		Type[] genericInterfaces = target.getGenericInterfaces();

		for (Type genericInterface : genericInterfaces) {
			if (genericInterface instanceof ParameterizedType
					&& ((ParameterizedType) genericInterface).getRawType().equals(interfaceCla)) {
				return (ParameterizedType) genericInterface;
			}
		}
		return getGenericInterfaceParameterizedType(target.getSuperclass(), interfaceCla);
	}
	
	/**
	 * @param cla
	 * @return 包含所有父类的字段
	 */
	public static List<Field> getAllDeclaredFields(Class<?> cla) {
		List<Field> allDeclaredFields = new LinkedList<Field>();
		while (cla != null && cla != Object.class) {
			Field[] declaredFields = cla.getDeclaredFields();
//			allDeclaredFields.addAll(Arrays.asList(declaredFields));
			for(Field declaredField:declaredFields) {
				if(!declaredField.getName().endsWith("this$0")) {
					allDeclaredFields.add(declaredField);
				}
			}

			cla = cla.getSuperclass();
		}
		return allDeclaredFields;
	}
}