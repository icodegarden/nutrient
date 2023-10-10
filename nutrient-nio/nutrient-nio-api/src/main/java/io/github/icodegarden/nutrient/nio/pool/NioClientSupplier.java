package io.github.icodegarden.nutrient.nio.pool;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import io.github.icodegarden.nutrient.nio.NioClient;

/**
 * 
 * @author Fangfang.Xu
 *
 */
@FunctionalInterface
public interface NioClientSupplier {
	Logger log = LoggerFactory.getLogger(NioClientSupplier.class);

	NioClient get(String ip, int port);

	public static final NioClientSupplier DEFAULT = (ip, port) -> {
		try {
			if (ClassUtils.isPresent("io.github.icodegarden.nutrient.nio.netty.NettyNioClient", null)) {
				if(log.isInfoEnabled()) {
					log.info("Supplier create NioClient of type NettyNioClient.");
				}

				Class<?> cla = ClassUtils.forName("io.github.icodegarden.nutrient.nio.netty.NettyNioClient", null);
				Constructor<?> constructor = cla.getDeclaredConstructor(InetSocketAddress.class);
				return (NioClient) constructor.newInstance(new InetSocketAddress(ip, port));
			} else {
				if(log.isInfoEnabled()) {
					log.info("Supplier create NioClient of type JavaNioClient.");
				}

				Class<?> cla = ClassUtils.forName("io.github.icodegarden.nutrient.nio.java.JavaNioClient", null);
				Constructor<?> constructor = cla.getDeclaredConstructor(InetSocketAddress.class);
				return (NioClient) constructor.newInstance(new InetSocketAddress(ip, port));
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	};

//	public static final NioClientSupplier NETTY = (ip, port) -> {
//		return NettyNioClients.create(ip, port);
//	};
//	
//	public static final NioClientSupplier JAVA = (ip, port) -> {
//		ClientNioSelector clientNioSelector = null;
//		try {
//			clientNioSelector = ClientNioSelector.openNew("commons-exchange");
//		} catch (IOException e) {
//			throw new ExceedExpectedNioException(e);
//		}
//		return new JavaNioClient(new InetSocketAddress(ip, port), clientNioSelector);
//	};
}
