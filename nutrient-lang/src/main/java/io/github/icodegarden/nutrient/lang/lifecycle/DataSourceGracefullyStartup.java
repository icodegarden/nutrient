package io.github.icodegarden.nutrient.lang.lifecycle;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.util.ClassUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Fangfang.Xu
 *
 */
@Slf4j
public class DataSourceGracefullyStartup implements GracefullyStartup {

	private final DataSource dataSource;

	public DataSourceGracefullyStartup(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void start() throws Throwable {
		/**
		 * 无损上线,利用getConnection促使连接池初始化完成
		 */
		if (dataSource != null) {
			log.info("commons beans init DataSource pool of getConnection, datasource:{}", dataSource);

			if (ClassUtils.isPresent(
					"io.github.icodegarden.nutrient.shardingsphere.builder.DataSourceOnlyApiShardingSphereBuilder",
					null)) {
				Class<?> shardingSphereDataSourceClass = ClassUtils.forName(
						"org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource", null);
				if (ClassUtils.isAssignableValue(shardingSphereDataSourceClass, dataSource)) {
					/**
					 * @see ShardingSphereDataSourceGracefullyStartup
					 */
					return;
				}
			} else {
				initGetConnection(dataSource);
			}
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
