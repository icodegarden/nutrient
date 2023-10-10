package io.github.icodegarden.nutrient.test.repository;

import io.github.icodegarden.nutrient.lang.repository.Database;
import io.github.icodegarden.nutrient.lang.repository.MysqlJdbcDatabase;
import io.github.icodegarden.nutrient.test.TestsDataSourceDependent;
import io.github.icodegarden.nutrient.test.repository.DatabaseTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class MysqlJdbcDatabaseTests extends DatabaseTests {

	@Override
	protected Database getDatabase() {
		return new MysqlJdbcDatabase(TestsDataSourceDependent.DATASOURCE);
	}
}
