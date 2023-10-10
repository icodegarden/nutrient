package io.github.icodegarden.nutrient.zookeeper.concurrent.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.client.ZKClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedReentrantReadWriteLock;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedReentrantReadWriteLockTests;
import io.github.icodegarden.nutrient.zookeeper.PropertiesConfig;
import io.github.icodegarden.nutrient.zookeeper.concurrent.lock.ZooKeeperReentrantReadWriteLock;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ZooKeeperReentrantReadWriteLockTests extends DistributedReentrantReadWriteLockTests {

	String root = "/zklock-test";
	CuratorFramework client;

	@BeforeEach
	void initClient() {
		RetryPolicy retryPolicy = new RetryOneTime(100);
		ZKClientConfig zkClientConfig = new ZKClientConfig();
		zkClientConfig.setProperty(ZKClientConfig.ZOOKEEPER_SERVER_PRINCIPAL,
				"zookeeper/" + PropertiesConfig.zkConnectString);
		client = CuratorFrameworkFactory.newClient(PropertiesConfig.zkConnectString, 3000, 1000, retryPolicy,
				zkClientConfig);
		client.start();
	}

	@AfterEach
	void closeClient() {
		client.close();
	}

	@Override
	protected DistributedReentrantReadWriteLock newLock(String name) {
		return new ZooKeeperReentrantReadWriteLock(client, root, name);
	}

	@Test
	void destory() throws Exception {
		ZooKeeperReentrantReadWriteLock zooKeeperReentrantReadWriteLock = new ZooKeeperReentrantReadWriteLock(client,
				root, "lock");
		zooKeeperReentrantReadWriteLock.readLock().acquire(1000);
		zooKeeperReentrantReadWriteLock.writeLock().acquire(1000);
		zooKeeperReentrantReadWriteLock.destory();
	}
}
