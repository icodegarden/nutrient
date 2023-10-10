package io.github.icodegarden.nutrient.test.registry;

import io.github.icodegarden.nutrient.lang.registry.MysqlJdbcRegistry;
import io.github.icodegarden.nutrient.lang.registry.Registration;
import io.github.icodegarden.nutrient.lang.registry.Registry;
import io.github.icodegarden.nutrient.lang.registry.RegistryListener;
import io.github.icodegarden.nutrient.test.TestsDataSourceDependent;
import io.github.icodegarden.nutrient.test.registry.RegistryTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlJdbcRegistryTests extends RegistryTests {

	@Override
	protected Registry<Registration> newRegistry(RegistryListener registryListener) {
		return new MysqlJdbcRegistry(TestsDataSourceDependent.DATASOURCE, registryListener);
	}

}
