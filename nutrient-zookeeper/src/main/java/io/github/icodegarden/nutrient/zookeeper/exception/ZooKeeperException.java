package io.github.icodegarden.nutrient.zookeeper.exception;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class ZooKeeperException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ZooKeeperException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZooKeeperException(Throwable cause) {
		super(cause);
	}

	public ZooKeeperException(String message) {
		super(message);
	}

}
