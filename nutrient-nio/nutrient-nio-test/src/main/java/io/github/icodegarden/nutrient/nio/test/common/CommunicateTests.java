package io.github.icodegarden.nutrient.nio.test.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class CommunicateTests extends CSBaseTests {

	/**
	 * 测试建立连接后，正常通讯
	 */
	@Test
	public void communicate() throws Exception {
		startServer();
		startClient();

		nioClient.send("xff666");
		nioClient.send("xff777");
		nioClient.send("xff888");

		String req = UUID.randomUUID().toString();

		String response = nioClient.request(req);
		assertEquals("response-" + req, response);

		response = nioClient.request(50.05);
		assertEquals("response-50.05", response);

		response = nioClient.request(100, 3000);
		assertEquals("response-100", response);

		byte[] _10MB = new byte[1024 * 1024 * 10];// 10M
		response = nioClient.request(_10MB, 3000);
		assertNotNull(response);

		// ------------------------------------------------------------

		Future<String> future = nioClient.requestFuture(50.05);
		assertEquals("response-50.05", future.get());

		future = nioClient.requestFuture(100);
		assertEquals("response-100", future.get(3000, TimeUnit.MILLISECONDS));

		// ------------------------------------------------------------

		AtomicReference<Object> callbackVal = new AtomicReference<Object>();

		Consumer<String> successConsumer = v -> {
			callbackVal.set(v);
			synchronized (callbackVal) {
				callbackVal.notify();
			}
		};

		future = nioClient.requestCallback(100, successConsumer);
		synchronized (callbackVal) {
			callbackVal.wait(3000);// 等待回调触发
		}

		assertEquals("response-100", callbackVal.get());
		assertEquals("response-100", future.get(3000, TimeUnit.MILLISECONDS));
	}

}
