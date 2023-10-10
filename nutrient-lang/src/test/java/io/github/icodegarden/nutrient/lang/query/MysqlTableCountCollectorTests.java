package io.github.icodegarden.nutrient.lang.query;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.icodegarden.nutrient.lang.TestsDataSourceDependent;
import io.github.icodegarden.nutrient.lang.query.MysqlTableDataCountCollector;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class MysqlTableCountCollectorTests {

	MysqlTableDataCountCollector collector = new MysqlTableDataCountCollector(TestsDataSourceDependent.DATASOURCE);

	@Test
	void version() throws Exception {
		String version = collector.version();

		Assertions.assertThat(version).isNotEmpty();
	}

	@Test
	void listTables() throws Exception {
		List<String> listTables = collector.listTables();

		Assertions.assertThat(listTables).isNotEmpty();
	}

	@Test
	void countTable() throws Exception {
		/**
		 * 需要先人工建表
		 */
		long countTable = collector.countTable("table_data_count");
		System.out.println(countTable);
		Assertions.assertThat(countTable).isGreaterThanOrEqualTo(0);
	}
}
