package io.github.icodegarden.nutrient.mybatis.repository;

import io.github.icodegarden.nutrient.lang.repository.Database;
import io.github.icodegarden.nutrient.mybatis.MybatisTestUtils;
import io.github.icodegarden.nutrient.mybatis.repository.MysqlMybatisDatabaseMapper;
import io.github.icodegarden.nutrient.test.repository.DatabaseTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class MysqlMybatisDatabaseTests extends DatabaseTests {

	@Override
	protected Database getDatabase() {
		return MybatisTestUtils.getMapper(MysqlMybatisDatabaseMapper.class);
	}
}