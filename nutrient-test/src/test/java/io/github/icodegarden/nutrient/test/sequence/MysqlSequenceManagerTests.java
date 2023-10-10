package io.github.icodegarden.nutrient.test.sequence;

import java.io.IOException;
import java.util.Properties;

import com.zaxxer.hikari.HikariDataSource;

import io.github.icodegarden.nutrient.lang.sequence.MysqlSequenceManager;
import io.github.icodegarden.nutrient.lang.sequence.SequenceManager;
import io.github.icodegarden.nutrient.test.sequence.SequenceManagerTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class MysqlSequenceManagerTests extends SequenceManagerTests {

	Properties properties = new Properties();
	{
		try {
			properties.load(MysqlSequenceManagerTests.class.getClassLoader().getResourceAsStream("test.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected SequenceManager getForOneProcess() {
		return newSequenceManager();
	}

	@Override
	protected SequenceManager newForMultiProcess() {
		return newSequenceManager();
	}

	private SequenceManager newSequenceManager() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(properties.getProperty("dataSource.driverClassName"));
		dataSource.setJdbcUrl(properties.getProperty("dataSource.jdbcUrl"));
		dataSource.setUsername(properties.getProperty("dataSource.username"));
		dataSource.setPassword(properties.getProperty("dataSource.password"));

		MysqlSequenceManager idGenerator = new MysqlSequenceManager("GLOBAL", dataSource);
		return idGenerator;
	}
}
