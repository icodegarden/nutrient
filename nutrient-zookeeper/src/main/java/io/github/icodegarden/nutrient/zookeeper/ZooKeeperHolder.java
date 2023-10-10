package io.github.icodegarden.nutrient.zookeeper;

import java.io.Closeable;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.ZKClientConfig;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import io.github.icodegarden.nutrient.lang.Validateable;
import io.github.icodegarden.nutrient.lang.util.ThreadUtils;
import io.github.icodegarden.nutrient.zookeeper.exception.ConnectTimeoutZooKeeperException;
import io.github.icodegarden.nutrient.zookeeper.exception.ExceedExpectedZooKeeperException;
import io.github.icodegarden.nutrient.zookeeper.exception.ZooKeeperException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ZooKeeperHolder implements Closeable {
	private static final Logger log = LoggerFactory.getLogger(ZooKeeperHolder.class);

	/**
	 * 外部可配
	 */
	public static long MAX_WAIT_CONNECTED_MS = 10000;

	private final ScheduledThreadPoolExecutor reconnectScheduledThreadPool = ThreadUtils
			.newSingleScheduledThreadPool("ZooKeeperReconnectTimerTask");

	private volatile boolean closeCalled;

	private final Config config;

	private volatile ZooKeeper zk;

	private List<NewZooKeeperListener> listeners = new CopyOnWriteArrayList<NewZooKeeperListener>();

	public ZooKeeperHolder(String connectString, int sessionTimeout, int connectTimeout) {
		this(new Config(connectString, sessionTimeout, connectTimeout));
	}

	public ZooKeeperHolder(Config config) {
		Objects.requireNonNull(config, "config must not null");

		this.config = config;
		newZooKeeper();

		ZooKeeperReconnectTimerTask reconnectTimerTask = new ZooKeeperReconnectTimerTask();
		reconnectScheduledThreadPool.scheduleWithFixedDelay(reconnectTimerTask, config.getConnectTimeout(), 1000,
				TimeUnit.MILLISECONDS);
	}

	public void addNewZooKeeperListener(NewZooKeeperListener listener) {
		listeners.add(listener);
		listeners.sort(Comparator.comparingInt(NewZooKeeperListener::order));
	}

	List<NewZooKeeperListener> listNewZooKeeperListeners() {
		return listeners;
	}

	private void newZooKeeper() throws ZooKeeperException {
		try {
			StateWatcher stateWatcher = new StateWatcher();

			ZKClientConfig zkClientConfig = new ZKClientConfig();
			zkClientConfig.setProperty(ZKClientConfig.ZOOKEEPER_SERVER_PRINCIPAL,
					"zookeeper/" + config.getConnectString());

			/**
			 * 不会阻塞；若server处于不可用，zk将一直自动重连
			 */
			zk = new ZooKeeper(config.getConnectString(), config.getSessionTimeout(), stateWatcher, zkClientConfig);
			if (config.getAclAuth() != null) {
				zk.addAuthInfo("digest", config.getAclAuth().getBytes());
			}

			if (log.isInfoEnabled()) {
				log.info("success new ZooKeeper, connectString:{}, sessionTimeout:{}", config.getConnectString(),
						config.getSessionTimeout());
			}
			stateWatcher.setZooKeeper(zk);

			if (!listeners.isEmpty()) {
				for (NewZooKeeperListener listener : listeners) {
					log.info("trigger onNewZooKeeper listener:{}", listener.getClass().getName());
					listener.onNewZooKeeper();
				}
			}
		} catch (IOException e) {
			throw new ExceedExpectedZooKeeperException("ex on new ZooKeeper", e);
		}
	}

	public ZooKeeper getZK() {
		if (closeCalled) {
			throw new IllegalStateException(ZooKeeperHolder.class.getSimpleName() + " was closed");
		}
		return zk;
	}

	/**
	 * 确保得到的zk当前的状态是connected的，但不保证在使用时还是连接上的，受限于zk自身的状态变化及时性
	 */
	public ZooKeeper getConnectedZK() throws ZooKeeperException {
		if (closeCalled) {
			throw new IllegalStateException(ZooKeeperHolder.class.getSimpleName() + " was closed");
		}
		if (!zk.getState().isConnected()) {
			synchronized (this) {
				if (!zk.getState().isConnected()) {
					/**
					 * 等待连上的notify
					 */
					try {
						this.wait(config.getConnectTimeout());
					} catch (InterruptedException ignore) {
					}

					/**
					 * 如果还是未连接
					 */
					if (!zk.getState().isConnected()) {
						throw new ConnectTimeoutZooKeeperException(
								String.format("zookeeper wait connected timeout:%d, connectString:%s",
										config.getConnectTimeout(), config.getConnectString()));
					}
				}
			}
		}

		return zk;
	}

	/**
	 * create if not exists
	 * 
	 * @param root
	 * @throws ZooKeeperException
	 */
	public void ensureRootNode(String root) throws ZooKeeperException {
		/**
		 * /a/b/c -> a/b/c
		 */
		String substring = root.substring(1);
		String[] nodes = substring.split("/");

		String path = "";
		for (String node : nodes) {
			path += "/" + node;
			try {
				Stat exists = getConnectedZK().exists(path, false);
				if (exists == null) {
					try {
						getConnectedZK().create(path, new byte[0], ACLs.AUTH_ALL_ACL, CreateMode.PERSISTENT);
					} catch (InterruptedException ignore) {
					} catch (KeeperException.NodeExistsException ignore) {
					}
				}
			} catch (InterruptedException ignore) {
			} catch (KeeperException e) {
				throw new ExceedExpectedZooKeeperException(String.format("ex on ensure root node of exists [%s]", path),
						e);
			}
		}
	}

	/**
	 * 给外部使用
	 */
	@Override
	public void close() throws IOException {
		reconnectScheduledThreadPool.shutdown();

		closeInternal();

		closeCalled = true;
	}

	/**
	 * 给内部使用
	 * 
	 * @throws IOException
	 */
	private void closeInternal() throws IOException {
		try {
			zk.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * 给内部使用
	 * 
	 * @throws IOException
	 */
	private synchronized void reconnect() throws IOException {
		log.warn("{} start reconnect, close and create a new ZooKeeper", this.getClass().getSimpleName());
		/**
		 * IMPT 不可以直接使用close()方法，那是给外部调用使用的
		 */
		closeInternal();

		/**
		 * 必须new新的
		 */
		newZooKeeper();
	}

	private class ZooKeeperReconnectTimerTask implements Runnable {
		private Long lastNotConnectedTimestamp;

		@Override
		public void run() {
			try {
				/**
				 * 如果状态不是已连接，且距离首次探测到不是已连接已过去N时间，则需要重连
				 */
				if (!zk.getState().isConnected()) {
					/**
					 * 因为zk client自动重连并不是万无一失的，可能出现下面场景:<br>
					 * zk server日志：<br>
					 * Refusing session request for client /172.24.16.157:37402 as it has seen zxid
					 * 0x83098a our last zxid is 0x3e6 client must try another server<br>
					 * <br>
					 * zk client日志：<br>
					 * o.a.z.ClientCnxn[1290]:Session 0x10313cb829f3c49 for sever
					 * zk37-svc/172.25.7.41:2181, Closing socket connection. Attempting reconnect
					 * except it is a SessionExpiredException. <br>
					 * org.apache.zookeeper.ClientCnxn$EndOfStreamException: Unable to read
					 * additional data from server sessionid 0x10313cb829f3c49, likely server has
					 * closed socket<br>
					 * at
					 * org.apache.zookeeper.ClientCnxnSocketNIO.doIO(ClientCnxnSocketNIO.java:77)<br>
					 * at
					 * org.apache.zookeeper.ClientCnxnSocketNIO.doTransport(ClientCnxnSocketNIO.java:350)<br>
					 * at org.apache.zookeeper.ClientCnxn$SendThread.run(ClientCnxn.java:1280)<br>
					 * <br>
					 * 
					 * 以上场景zk client会一直重连，但server却一直拒绝<br>
					 * 因此当检查到距离首次等待失败已过去xx时间还没自动连上，则调用reconnect来重新new ZooKeeper保障成功建立新的连接<br>
					 */

					/**
					 * 记录首次失败的时间
					 */
					if (lastNotConnectedTimestamp == null) {
						lastNotConnectedTimestamp = System.currentTimeMillis();
					}
					/**
					 * 已有过失败，且已过去N时间
					 */
					if (lastNotConnectedTimestamp <= (System.currentTimeMillis() - MAX_WAIT_CONNECTED_MS)) {
						log.warn("lastNotConnectedTimestamp is exceed, start reconnect to ensure connect success");
						reconnect();

						/**
						 * 重连成功后重置；若重连未成功则lastNotConnectedTimestamp不重置，下次任务触发时还会触发重连
						 */
						lastNotConnectedTimestamp = null;
					}
				} else {
					/**
					 * 已连接的重置
					 */
					lastNotConnectedTimestamp = null;
				}
			} catch (Throwable e) {
				log.error("ex on ZooKeeperReconnectTimerTask", e);
			}
		}
	}

	private class StateWatcher implements Watcher {
		private ZooKeeper zk;

		private void setZooKeeper(ZooKeeper zk) {
			this.zk = zk;
		}

		@Override
		public void process(WatchedEvent event) {
			/**
			 * Event.EventType.None属于连接状态的Event
			 */
			if (event.getType() == Event.EventType.None) {
				if (log.isInfoEnabled()) {
					log.info("zk.state:{}, sessionId:{}", event.getState(), zk.getSessionId());
				}
				switch (event.getState()) {
				/**
				 * Disconnected不是server发给client的，是client自己识别到的<br>
				 * 当网络不可用较长一段时间、zk server下线或不可用 就会触发，此时zk client会自动一直重连
				 */
				case Disconnected:
					break;
				/**
				 * 每当zk client和server建立连接成功时触发，场景如new ZooKeeper之后连接成功、Disconnected出现后又连接成功
				 */
				case SyncConnected:
					synchronized (ZooKeeperHolder.this) {
						ZooKeeperHolder.this.notify();
					}
					break;
				/**
				 * 认证失败
				 */
				case AuthFailed:
					break;
				/**
				 * Expired是server发给client的，表示session过期必须new新的ZooKeeper，否则永远无法再自动建立连接<br>
				 * 当由于网络不稳定等问题时间超过sessionTimeout就会触发
				 */
				case Expired:
					while (true) {
						log.warn("Session Expired, start reconnect to ensure connect success");
						try {
							reconnect();
							break;// break loop if success
						} catch (Exception e) {
							log.warn("ex on newZooKeeper when Session Expired", e);
							// continue loop
							try {
								// 稍后重试
								Thread.sleep(3000);
							} catch (InterruptedException e1) {
							}
						}
					}

					break;
				/**
				 * 只在client主动调用close后
				 */
				case Closed:
					break;
				default:
					break;
				}
			}
		}
	}

	@Setter
	@Getter
	@ToString
	public static class Config implements Validateable {
		@NonNull
		private String connectString;// 127.0.0.1:2181,127.0.0.2:2181,127.0.0.3:2181

		private int sessionTimeout = 30000;

		private int connectTimeout = 3000;

		private String aclAuth;

		public Config() {

		}

		public Config(String connectString, Integer sessionTimeout, Integer connectTimeout) {
			if (connectString == null || connectString.isEmpty()) {
				throw new IllegalArgumentException("connectString must not empty");
			}
			this.connectString = connectString;
			this.sessionTimeout = sessionTimeout;
			this.connectTimeout = connectTimeout;
		}

		@Override
		public void validate() throws IllegalArgumentException {
			Assert.hasText(connectString, "connectString must not empty");
		}

	}
}
