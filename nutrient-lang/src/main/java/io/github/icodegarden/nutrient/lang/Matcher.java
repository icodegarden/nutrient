package io.github.icodegarden.nutrient.lang;

/**
 * 
 * @author Fangfang.Xu
 *
 * @param <T>
 */
public interface Matcher<T> {

	boolean matches(T object);
}