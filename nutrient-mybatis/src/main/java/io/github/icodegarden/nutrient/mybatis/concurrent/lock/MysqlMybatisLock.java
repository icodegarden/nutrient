package io.github.icodegarden.nutrient.mybatis.concurrent.lock;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DatabaseLock;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlMybatisLock extends DatabaseLock implements MybatisLock {

	public MysqlMybatisLock(MysqlMybatisLockMapper mapper, String name, Long expireSeconds) {
		super(mapper, name, expireSeconds);
	}
}
