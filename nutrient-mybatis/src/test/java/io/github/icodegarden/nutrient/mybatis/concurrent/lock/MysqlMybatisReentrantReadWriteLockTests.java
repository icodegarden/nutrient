package io.github.icodegarden.nutrient.mybatis.concurrent.lock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DatabaseReadWriteLockRepository;
import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedReentrantReadWriteLock;
import io.github.icodegarden.nutrient.mybatis.MybatisTestUtils;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisReadWriteLockMapper;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisReentrantReadWriteLock;
import io.github.icodegarden.nutrient.test.TestsDataSourceDependent;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedReentrantReadWriteLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlMybatisReentrantReadWriteLockTests extends DistributedReentrantReadWriteLockTests {

	@BeforeEach
	void initDS() {
		TestsDataSourceDependent.clearTable(DatabaseReadWriteLockRepository.TABLE_NAME);
	}

	@AfterEach
	void closeDS() {
	}

	@Override
	protected DistributedReentrantReadWriteLock newLock(String name) {
		MysqlMybatisReadWriteLockMapper mapper = MybatisTestUtils.getMapper(MysqlMybatisReadWriteLockMapper.class);
		return new MysqlMybatisReentrantReadWriteLock(mapper, name, 5L);
	}

}
