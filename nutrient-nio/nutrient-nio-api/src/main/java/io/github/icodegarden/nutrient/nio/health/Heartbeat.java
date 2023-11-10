package io.github.icodegarden.nutrient.nio.health;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.icodegarden.nutrient.lang.util.SystemUtils;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface Heartbeat extends Closeable {
	Logger log = LoggerFactory.getLogger(Heartbeat.class);

	void send() throws IOException;

	void receive();

	void response() throws IOException;

	long lastSend();

	long lastReceive();

	default boolean shouldReconnect(long heartbeatIntervalMillis) {
		boolean shouldReconnect = false;
		long lastReceive = lastReceive();
		if (isClosed()) {
			if (log.isInfoEnabled()) {
				log.info("client heartbeat:{} was closed, should reconnect.", this);
			}
			shouldReconnect = true;
		} else if ((SystemUtils.currentTimeMillis() - lastReceive) >= (heartbeatIntervalMillis * 3)) {
			if (log.isInfoEnabled()) {
				log.info("client heartbeat:{} lastReceive was timeout:{}, should reconnect.", this,
						heartbeatIntervalMillis * 3);
			}
			shouldReconnect = true;
		}
		return shouldReconnect;
	}

	void reconnect() throws IOException;

	boolean isClosed();
}
