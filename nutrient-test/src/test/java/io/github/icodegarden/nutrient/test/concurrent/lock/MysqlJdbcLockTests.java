package io.github.icodegarden.nutrient.test.concurrent.lock;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedLock;
import io.github.icodegarden.nutrient.lang.concurrent.lock.MysqlJdbcLock;
import io.github.icodegarden.nutrient.test.TestsDataSourceDependent;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlJdbcLockTests extends DistributedLockTests {

	@Override
	protected DistributedLock newDistributedLock(String name) {
		return new MysqlJdbcLock(TestsDataSourceDependent.DATASOURCE, name, getExpireSeconds());
	}
	
	@Override
	protected long getExpireSeconds() {
		return 5;
	}
}
