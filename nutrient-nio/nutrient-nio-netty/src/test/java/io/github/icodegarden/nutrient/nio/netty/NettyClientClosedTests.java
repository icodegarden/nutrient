package io.github.icodegarden.nutrient.nio.netty;

import java.net.InetSocketAddress;

import io.github.icodegarden.nutrient.nio.NioClient;
import io.github.icodegarden.nutrient.nio.NioServer;
import io.github.icodegarden.nutrient.nio.netty.NettyNioClient;
import io.github.icodegarden.nutrient.nio.netty.NettyNioServer;
import io.github.icodegarden.nutrient.nio.test.common.ClientClosedTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class NettyClientClosedTests extends ClientClosedTests {

	@Override
	protected NioServer nioServer() {
		return new NettyNioServer("netty", new InetSocketAddress("127.0.0.1", 8888), messageHandler);
	}

	@Override
	protected NioClient nioClient() {
		return new NettyNioClient(new InetSocketAddress("127.0.0.1", 8888));
	}
}
