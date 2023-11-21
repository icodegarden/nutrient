package io.github.icodegarden.nutrient.lang.exception.reactive;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ReactiveBlockException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ReactiveBlockException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReactiveBlockException(Throwable cause) {
		super(cause);
	}

}
