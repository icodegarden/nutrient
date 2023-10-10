package io.github.icodegarden.nutrient.zookeeper.concurrent.lock;

import java.util.LinkedList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.client.ZKClientConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedReentrantLock;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedReentrantLockTests;
import io.github.icodegarden.nutrient.zookeeper.PropertiesConfig;
import io.github.icodegarden.nutrient.zookeeper.concurrent.lock.ZooKeeperReentrantLock;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class ZooKeeperReentrantLockTests extends DistributedReentrantLockTests {

	List<CuratorFramework> clients = new LinkedList<>();
	String root = "/zklock-test";

	@Override
	protected DistributedReentrantLock newLock(String name) {
		/*
		 * initClient
		 */
		RetryPolicy retryPolicy = new RetryOneTime(100);
		ZKClientConfig zkClientConfig = new ZKClientConfig();
		zkClientConfig.setProperty(ZKClientConfig.ZOOKEEPER_SERVER_PRINCIPAL,
				"zookeeper/" + PropertiesConfig.zkConnectString);
		CuratorFramework client = CuratorFrameworkFactory.newClient(PropertiesConfig.zkConnectString, 3000, 1000,
				retryPolicy, zkClientConfig);
		client.start();

		// -----------------
		clients.add(client);
		return new ZooKeeperReentrantLock(client, root, name);
	}

	@BeforeEach
	void initClient() {
	}

	@AfterEach
	void closeClient() {
		clients.forEach(client -> {
			client.close();
		});
	}

}
