package io.github.icodegarden.nutrient.exchange;

import io.github.icodegarden.nutrient.lang.exception.remote.RemoteException;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface Protocol {

	<R> R exchange(ProtocolParams params) throws RemoteException;

}
