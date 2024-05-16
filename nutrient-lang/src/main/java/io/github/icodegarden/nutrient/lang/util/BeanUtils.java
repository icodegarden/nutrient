package io.github.icodegarden.nutrient.lang.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import io.github.icodegarden.nutrient.lang.annotation.Nullable;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class BeanUtils {

	/**
	 * 任意字段是否有值
	 * 
	 * @param obj
	 * @param ignoreFieldNames 忽略的字段
	 * @return
	 */
	public static boolean anyFieldHasValue(Object obj, @Nullable Collection<String> ignoreFieldNames) {
		Class<?> cla = obj.getClass();
		List<Field> allDeclaredFields = ClassUtils.getAllDeclaredFields(cla);

		return allDeclaredFields.stream().anyMatch(field -> {
			if (!org.springframework.util.CollectionUtils.isEmpty(ignoreFieldNames)
					&& ignoreFieldNames.contains(field.getName())) {
				return false;
			}
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			try {
				return field.get(obj) != null;
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Can Not Access", e);
			} finally {
				field.setAccessible(accessible);
			}
		});
	}

	/**
	 * 所有字段是否有值
	 * 
	 * @param obj
	 * @param ignoreFieldNames 忽略的字段
	 * @return
	 */
	public static boolean allFieldHasValue(Object obj, @Nullable Collection<String> ignoreFieldNames) {
		Class<?> cla = obj.getClass();
		List<Field> allDeclaredFields = ClassUtils.getAllDeclaredFields(cla);

		return allDeclaredFields.stream().allMatch(field -> {
			if (!org.springframework.util.CollectionUtils.isEmpty(ignoreFieldNames)
					&& ignoreFieldNames.contains(field.getName())) {
				return true;
			}
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			try {
				return field.get(obj) != null;
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Can Not Access", e);
			} finally {
				field.setAccessible(accessible);
			}
		});
	}
}
