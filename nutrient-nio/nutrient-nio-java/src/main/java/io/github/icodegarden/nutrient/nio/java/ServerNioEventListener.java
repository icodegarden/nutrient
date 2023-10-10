package io.github.icodegarden.nutrient.nio.java;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface ServerNioEventListener {

	ServerSocketChannel serverSocketChannel();

	void onAccept() throws IOException;
}
