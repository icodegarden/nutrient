package io.github.icodegarden.nutrient.shardingsphere.lifecycle;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;

import io.github.icodegarden.nutrient.lang.lifecycle.GracefullyStartup;
import io.github.icodegarden.nutrient.shardingsphere.util.DataSourceUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Fangfang.Xu
 *
 */
@Slf4j
public class ShardingSphereDataSourceGracefullyStartup implements GracefullyStartup {

	private final ShardingSphereDataSource dataSource;

	public ShardingSphereDataSourceGracefullyStartup(ShardingSphereDataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void start() throws Throwable {
		/**
		 * 无损上线,利用getConnection促使连接池初始化完成
		 */
		if (dataSource != null) {
			log.info("commons beans init DataSource pool of getConnection, datasource:{}", dataSource);

			Map<String, DataSource> dataSourceMap = DataSourceUtils
					.dataSourceMap((ShardingSphereDataSource) dataSource);

			dataSourceMap.values().forEach(dataSource -> {
				initGetConnection(dataSource);
			});
		}
	}

	private void initGetConnection(DataSource dataSource) {
		try (Connection connection = dataSource.getConnection();) {
			// do nothing
		} catch (Exception e) {
			log.warn("ex on init DataSource pool of getConnection", e);
		}
	}

}
