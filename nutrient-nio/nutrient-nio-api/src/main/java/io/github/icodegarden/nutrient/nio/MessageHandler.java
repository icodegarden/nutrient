package io.github.icodegarden.nutrient.nio;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface MessageHandler<T, R> {

	/**
	 * for send only without response
	 */
	void receive(T obj);

	R reply(T obj);
}
