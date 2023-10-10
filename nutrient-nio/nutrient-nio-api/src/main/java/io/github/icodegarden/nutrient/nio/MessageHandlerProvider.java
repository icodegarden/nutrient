package io.github.icodegarden.nutrient.nio;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface MessageHandlerProvider<T, R> {

	MessageHandler<T, R> getMessageHandler();

	boolean supports(Object msg);

}
