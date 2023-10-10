package io.github.icodegarden.nutrient.mybatis.registry;

import io.github.icodegarden.nutrient.lang.registry.Registration;
import io.github.icodegarden.nutrient.lang.registry.Registry;
import io.github.icodegarden.nutrient.lang.registry.RegistryListener;
import io.github.icodegarden.nutrient.mybatis.MybatisTestUtils;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisLockMapper;
import io.github.icodegarden.nutrient.mybatis.registry.MysqlMybatisRegistry;
import io.github.icodegarden.nutrient.mybatis.registry.MysqlMybatisRegistryMapper;
import io.github.icodegarden.nutrient.test.registry.RegistryTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlMybatisRegistryTests extends RegistryTests {

	@Override
	protected Registry<Registration> newRegistry(RegistryListener registryListener) {
		MysqlMybatisLockMapper lockMapper = MybatisTestUtils.getMapper(MysqlMybatisLockMapper.class);
		MysqlMybatisRegistryMapper mapper = MybatisTestUtils.getMapper(MysqlMybatisRegistryMapper.class);
		MysqlMybatisRegistry mysqlMybatisRegistry = new MysqlMybatisRegistry(lockMapper, mapper, registryListener);
		mysqlMybatisRegistry.setLockAcquireTimeoutMillis(30000);;
		return mysqlMybatisRegistry;
	}
}
