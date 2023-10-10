package io.github.icodegarden.nutrient.zookeeper;

import io.github.icodegarden.nutrient.lang.sequence.SequenceManager;
import io.github.icodegarden.nutrient.test.sequence.SequenceManagerTests;
import io.github.icodegarden.nutrient.zookeeper.ZooKeeperHolder;
import io.github.icodegarden.nutrient.zookeeper.ZooKeeperSequenceManager;
import io.github.icodegarden.nutrient.zookeeper.ZooKeeperHolder.Config;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class ZooKeeperSequenceManagerTests extends SequenceManagerTests {

	@Override
	protected SequenceManager getForOneProcess() {
		return newSequenceManager();
	}

	@Override
	protected SequenceManager newForMultiProcess() {
		return newSequenceManager();
	}

	private SequenceManager newSequenceManager() {
		Config config = new ZooKeeperHolder.Config(PropertiesConfig.zkConnectString, 30000, 10000);
		config.setAclAuth("xff:xff");
		ZooKeeperHolder zkh = new ZooKeeperHolder(config);

		return new ZooKeeperSequenceManager("GLOBAL", zkh);
	}

}
