package io.github.icodegarden.nutrient.nio.netty;

import java.net.InetSocketAddress;

import io.github.icodegarden.nutrient.nio.NioClient;
import io.github.icodegarden.nutrient.nio.NioServer;
import io.github.icodegarden.nutrient.nio.netty.NettyNioClient;
import io.github.icodegarden.nutrient.nio.netty.NettyNioServer;
import io.github.icodegarden.nutrient.nio.test.common.MultiClientServerParallelTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class NettyMultiClientServerParallelTests extends MultiClientServerParallelTests {

	@Override
	protected NioServer nioServer1(int port) {
		return new NettyNioServer("netty1", new InetSocketAddress("127.0.0.1", port), 3000, messageHandler);
	}

	@Override
	protected NioServer nioServer2(int port) {
		return new NettyNioServer("netty2", new InetSocketAddress("127.0.0.1", port), 3000,messageHandler);
	}

	@Override
	protected NioClient nioClient(int port) {
		return new NettyNioClient(new InetSocketAddress("127.0.0.1", port), 3000);
	}

}
