package io.github.icodegarden.nutrient.test.concurrent.lock;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedReentrantLock;
import io.github.icodegarden.nutrient.lang.concurrent.lock.MysqlJdbcReentrantLock;
import io.github.icodegarden.nutrient.test.TestsDataSourceDependent;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedReentrantLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlJdbcReentrantLockTests extends DistributedReentrantLockTests {

	@Override
	protected DistributedReentrantLock newLock(String name) {
		return new MysqlJdbcReentrantLock(TestsDataSourceDependent.DATASOURCE, name, 5L);
	}

}
