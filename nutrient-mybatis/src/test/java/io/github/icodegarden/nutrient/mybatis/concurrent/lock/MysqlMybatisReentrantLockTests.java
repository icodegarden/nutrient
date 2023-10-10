package io.github.icodegarden.nutrient.mybatis.concurrent.lock;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedReentrantLock;
import io.github.icodegarden.nutrient.mybatis.MybatisTestUtils;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisLockMapper;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisReentrantLock;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedReentrantLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlMybatisReentrantLockTests extends DistributedReentrantLockTests {

	@Override
	protected DistributedReentrantLock newLock(String name) {
		MysqlMybatisLockMapper mapper = MybatisTestUtils.getMapper(MysqlMybatisLockMapper.class);
		return new MysqlMybatisReentrantLock(mapper, name, 5L);
	}

}
