package io.github.icodegarden.nutrient.lang.concurrent.lock;

import javax.sql.DataSource;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlJdbcLock extends DatabaseLock implements JdbcLock {

	public MysqlJdbcLock(DataSource dataSource, String name, Long expireSeconds) {
		super(new MysqlJdbcLockRepository(dataSource), name, expireSeconds);
	}
}
