package io.github.icodegarden.nutrient.lang.filter;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface TrustFilter<T> {

	boolean filter(T t);
}
