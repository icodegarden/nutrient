package io.github.icodegarden.nutrient.lang.exception.remote;

/**
 * @author Fangfang.Xu
 */
public class ConnectFailedRemoteException extends ClientRemoteException {

	private static final long serialVersionUID = 1L;

	public ConnectFailedRemoteException(String message) {
		super(message);
	}
	
	public ConnectFailedRemoteException(String message, Throwable cause) {
		super(message, cause);
	}

}