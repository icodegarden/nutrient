package io.github.icodegarden.nutrient.hbase.query;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.hadoop.hbase.client.Connection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.icodegarden.nutrient.hbase.HBaseEnv;
import io.github.icodegarden.nutrient.hbase.query.HBaseTableDataCountStorage;
import io.github.icodegarden.nutrient.lang.query.TableDataCountPO;

/**
 * 
 * @author Fangfang.Xu
 *
 */
class HBaseTableCountStorageTests {

	HBaseEnv hBaseEnv = HBaseEnvFactory.devAliyun();
	Connection connection = hBaseEnv.getConnection();
	HBaseTableDataCountStorage dao = new HBaseTableDataCountStorage(connection);

	@Test
	void add() throws Exception {
		TableDataCountPO po = new TableDataCountPO();
		po.setTableName("test_table");
		po.setDataCount(10L);
		po.setUpdatedAt(LocalDateTime.now());
		dao.add(po);
		
		List<TableDataCountPO> list = dao.findAll();

		Assertions.assertThat(list).hasSize(1);

		dao.delete(list.get(0).getId());
	}

	@Test
	void updateCount() throws Exception {
		TableDataCountPO po = new TableDataCountPO();
		po.setTableName("test_table");
		po.setDataCount(10L);
		po.setUpdatedAt(LocalDateTime.now());
		dao.add(po);

		dao.updateCount(po.getTableName(), 100);

		List<TableDataCountPO> list = dao.findAll();

		Assertions.assertThat(list.get(0).getDataCount()).isEqualTo(100);

		dao.delete(list.get(0).getId());
	}
}
