package io.github.icodegarden.nutrient.mybatis.registry;

import io.github.icodegarden.nutrient.lang.concurrent.lock.DistributedLock;
import io.github.icodegarden.nutrient.lang.registry.DatabaseRegistry;
import io.github.icodegarden.nutrient.lang.registry.Registration;
import io.github.icodegarden.nutrient.lang.registry.RegistryListener;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisLock;
import io.github.icodegarden.nutrient.mybatis.concurrent.lock.MysqlMybatisLockMapper;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class MysqlMybatisRegistry extends DatabaseRegistry<Long> implements MybatisRegistry<Registration> {

	private final MysqlMybatisLockMapper lockMapper;

	public MysqlMybatisRegistry(MysqlMybatisLockMapper lockMapper, MysqlMybatisRegistryMapper mapper,
			RegistryListener listener) {
		super(mapper, listener);
		this.lockMapper = lockMapper;
	}

	@Override
	protected DistributedLock getRegisterLock(String name, Long expireSeconds) {
		return new MysqlMybatisLock(lockMapper, name, expireSeconds);
	}
}
