package io.github.icodegarden.nutrient.nio.java;

import io.github.icodegarden.nutrient.nio.NioClient;
import io.github.icodegarden.nutrient.nio.NioServer;
import io.github.icodegarden.nutrient.nio.test.common.CommunicateTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JavaCommunicateTests extends CommunicateTests {

	JavaCS cs = new JavaCS();

	@Override
	protected NioServer nioServer() {
		return cs.nioServer();
	}

	@Override
	protected NioClient nioClient() {
		return cs.nioClient();
	}

}
