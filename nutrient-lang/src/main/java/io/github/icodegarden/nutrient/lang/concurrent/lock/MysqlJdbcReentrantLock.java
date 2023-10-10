package io.github.icodegarden.nutrient.lang.concurrent.lock;

import javax.sql.DataSource;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlJdbcReentrantLock extends CustomizeReentrantLock {

	public MysqlJdbcReentrantLock(DataSource dataSource, String name, Long expireSeconds) {
		super(new MysqlJdbcLock(dataSource, name, expireSeconds));
	}
}
