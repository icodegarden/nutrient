package io.github.icodegarden.nutrient.mybatis.concurrent.lock;

import io.github.icodegarden.nutrient.lang.concurrent.lock.CustomizeReentrantLock;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlMybatisReentrantLock extends CustomizeReentrantLock {

	public MysqlMybatisReentrantLock(MysqlMybatisLockMapper mapper, String name, Long expireSeconds) {
		super(new MysqlMybatisLock(mapper, name, expireSeconds));
	}
}
