package io.github.icodegarden.nutrient.mybatis.concurrent.lock;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedLock;
import io.github.icodegarden.nutrient.mybatis.MybatisTestUtils;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisLock;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisLockMapper;
import io.github.icodegarden.nutrient.test.concurrent.lock.DistributedLockTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlMybatisLockTests extends DistributedLockTests {

	@Override
	protected DistributedLock newDistributedLock(String name) {
		MysqlMybatisLockMapper mapper = MybatisTestUtils.getMapper(MysqlMybatisLockMapper.class);
		return new MysqlMybatisLock(mapper, name, getExpireSeconds());
	}

	@Override
	protected long getExpireSeconds() {
		return 5;
	}
}
