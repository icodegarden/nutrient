package io.github.icodegarden.nutrient.test.concurrent.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DatabaseReadWriteLockRepository;
import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedReentrantReadWriteLock;
import io.github.icodegarden.nutrient.lang.concurrent.lock.MysqlJdbcReentrantReadWriteLock;
import io.github.icodegarden.nutrient.test.TestsDataSourceDependent;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedReentrantReadWriteLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlJdbcReentrantReadWriteLockTests extends DistributedReentrantReadWriteLockTests {

	@BeforeEach
	void initDS() {
		TestsDataSourceDependent.clearTable(DatabaseReadWriteLockRepository.TABLE_NAME);
	}

	@AfterEach
	void closeDS() {
	}

	@Override
	protected DistributedReentrantReadWriteLock newLock(String name) {
		return new MysqlJdbcReentrantReadWriteLock(TestsDataSourceDependent.DATASOURCE, name, 5L);
	}

}
