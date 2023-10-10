package io.github.icodegarden.nutrient.nio.java;

import io.github.icodegarden.nutrient.nio.NioClient;
import io.github.icodegarden.nutrient.nio.NioServer;
import io.github.icodegarden.nutrient.nio.test.common.ServerClosedTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class JavaServerClosedTests extends ServerClosedTests {

	JavaCS cs = new JavaCS();

	@Override
	protected NioServer nioServer() {
		return cs.nioServer();//时间配置3000
	}

	@Override
	protected NioClient nioClient() {
		return cs.nioClient();//时间配置3000
	}

}
