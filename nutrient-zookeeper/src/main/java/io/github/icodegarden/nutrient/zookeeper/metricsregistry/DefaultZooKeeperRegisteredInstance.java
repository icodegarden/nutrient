package io.github.icodegarden.nutrient.zookeeper.metricsregistry;

import io.github.icodegarden.nutrient.lang.metricsregistry.DefaultRegisteredInstance;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class DefaultZooKeeperRegisteredInstance extends DefaultRegisteredInstance
		implements ZooKeeperRegisteredInstance {

	private String znode;

	public DefaultZooKeeperRegisteredInstance(String znode, String serviceName, String instanceName, String ip,
			int port) {
		super(serviceName, instanceName, ip, port);
		this.znode = znode;
	}

	public String getZnode() {
		return znode;
	}

	@Override
	public String toString() {
		return "[znode=" + znode + "," + super.toString() + "]";
	}

}