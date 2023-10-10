package io.github.icodegarden.nutrient.nio;

import java.io.IOException;

import io.github.icodegarden.nutrient.lang.exception.remote.RemoteException;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface Channel {
	
	void write(Object obj) throws RemoteException;
	
	void close() throws IOException;
}