package io.github.icodegarden.nutrient.shardingsphere.builder;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.shardingsphere.broadcast.api.config.BroadcastRuleConfiguration;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableReferenceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.NoneShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.util.CollectionUtils;

import io.github.icodegarden.nutrient.shardingsphere.algorithm.MysqlKeyGenerateAlgorithm;
import io.github.icodegarden.nutrient.shardingsphere.algorithm.RangeModShardingAlgorithm;
import io.github.icodegarden.nutrient.shardingsphere.util.DataSourceUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 只拆分datasource 0,1,... ，不在datasource中细分表0,1,...
 * 
 * @author Fangfang.Xu
 *
 */
@Slf4j
public class DataSourceOnlyApiShardingSphereBuilder {

	public static DataSource getDataSource(DataSourceOnlyConfig dataSourceOnlyConfig) throws SQLException {
		LinkedHashMap<String, DataSource> dataSourceMap = DataSourceUtils
				.createDataSourceMap(dataSourceOnlyConfig.getDatasources());
		DataSource firstDataSource = DataSourceUtils.firstDataSource(dataSourceMap);
		MysqlKeyGenerateAlgorithm.registerDataSource(firstDataSource);

		return ShardingSphereDataSourceFactory.createDataSource(
				DataSourceUtils.createDataSourceMap(dataSourceOnlyConfig.getDatasources()),
				Arrays.asList(createShardingRuleConfiguration(dataSourceOnlyConfig),
						createBroadcastRuleConfiguration(dataSourceOnlyConfig)),
				new Properties());
	}

	private static ShardingRuleConfiguration createShardingRuleConfiguration(
			DataSourceOnlyConfig dataSourceOnlyConfig) {
		ShardingRuleConfiguration result = new ShardingRuleConfiguration();

		/**
		 * 表规则
		 */
		List<ShardingTableRuleConfiguration> shardingTableRuleConfigurations = getShardingTableRuleConfigurations(
				dataSourceOnlyConfig);
		result.getTables().addAll(shardingTableRuleConfigurations);

		/**
		 * 绑定表
		 */
		List<String> bindingTableGroups = dataSourceOnlyConfig.getBindingTableGroups();
		log.info("bindingTableGroups:{}", bindingTableGroups);
		if (!CollectionUtils.isEmpty(bindingTableGroups)) {
			for (int i = 0; i < bindingTableGroups.size(); i++) {
				result.getBindingTableGroups().add(new ShardingTableReferenceRuleConfiguration("bindingTableGroup-" + i,
						bindingTableGroups.get(i)));
			}
		}

//		result.setDefaultDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("user_id", "inline"));

		/**
		 * 分片算法
		 */
		List<RangeModShardingAlgorithmConfig> rangeModShardingAlgorithms = dataSourceOnlyConfig
				.getRangeModShardingAlgorithms();
		for (RangeModShardingAlgorithmConfig rangeModShardingAlgorithm : rangeModShardingAlgorithms) {

			rangeModShardingAlgorithm.validate();
			RangeModShardingAlgorithm.registerRangeModShardingAlgorithmConfig(
					rangeModShardingAlgorithm.getAlgorithmName(), rangeModShardingAlgorithm);

			Properties props = new Properties();
			props.setProperty("strategy", "standard");
			props.setProperty("algorithmClassName",
					"io.github.icodegarden.nutrient.shardingsphere.algorithm.RangeModShardingAlgorithm");
			props.setProperty(RangeModShardingAlgorithm.ALGORITHM_NAME_KEY,
					rangeModShardingAlgorithm.getAlgorithmName()/* 如jobidrangemod */);
			result.getShardingAlgorithms().put(rangeModShardingAlgorithm.getAlgorithmName()/* 如jobidrangemod */,
					new AlgorithmConfiguration("CLASS_BASED", props));
		}

		/**
		 * 全局id生成
		 */
		List<String> keyGeneratorTables = dataSourceOnlyConfig.getKeyGeneratorTables();
		for (String table : keyGeneratorTables) {
			Properties mysqljobmainProps = new Properties();
			mysqljobmainProps.setProperty(MysqlKeyGenerateAlgorithm.MODULE_NAME_KEY, table/* 如job_main */);
			result.getKeyGenerators().put(table/* 如job_main */,
					new AlgorithmConfiguration(MysqlKeyGenerateAlgorithm.TYPE, mysqljobmainProps));
		}

		log.info("created ShardingRuleConfiguration is:{}", result);

		return result;
	}

	private static BroadcastRuleConfiguration createBroadcastRuleConfiguration(
			DataSourceOnlyConfig dataSourceOnlyConfig) {
		/**
		 * 广播表
		 */
		List<String> broadcastTables = dataSourceOnlyConfig.getBroadcastTables();
		log.info("broadcastTables:{}", broadcastTables);
		if (!CollectionUtils.isEmpty(broadcastTables)) {
			return new BroadcastRuleConfiguration(broadcastTables);
		}

		return new BroadcastRuleConfiguration(Collections.emptyList());
	}

	/**
	 * job_main配置
	 */
	private static List<ShardingTableRuleConfiguration> getShardingTableRuleConfigurations(
			DataSourceOnlyConfig dataSourceOnlyConfig) {
		List<ShardingTable> shardingTables = dataSourceOnlyConfig.getShardingTables();
		 List<ShardingTableRuleConfiguration> collect = shardingTables.stream().map(shardingTable -> {
			List<DataSourceConfig> dataSourceConfig = dataSourceOnlyConfig.getDatasources();

			String actualDataNodes = dataSourceConfig.stream().map(ds -> {
				return ds.getName() + "." + shardingTable.getTableName()/* 如job_main */;
			}).collect(Collectors.joining(","));

			log.info("actualDataNodes of {}:{}", shardingTable.getTableName(), actualDataNodes);

			ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration(shardingTable.getTableName(),
					actualDataNodes);

			StandardShardingStrategyConfiguration databaseShardingStrategy = new StandardShardingStrategyConfiguration(
					shardingTable.getShardingColumn()/* 如id */,
					shardingTable.getShardingAlgorithmName()/* 如jobidrangemod */ );
			result.setDatabaseShardingStrategy(databaseShardingStrategy);

			if (shardingTable.getKeyGenerateColumn() != null) {
				result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(
						shardingTable.getKeyGenerateColumn()/* 如id */, shardingTable.getTableName()));
			}
			return result;
		}).collect(Collectors.toList());
		 
		 
		 ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration("user",
					"ds0.user");

		 NoneShardingStrategyConfiguration databaseShardingStrategy = new NoneShardingStrategyConfiguration();
			result.setDatabaseShardingStrategy(databaseShardingStrategy);
			collect.add(result);
			
			return collect;
	}

}
