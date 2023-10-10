package io.github.icodegarden.nutrient.lang.concurrent.lock;

import java.util.List;

import io.github.icodegarden.nutrient.lang.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface DatabaseReadWriteLockRepository {

	public static final String TABLE_NAME = "distributed_read_write_lock";

	void createRow(String lockName, String identifier, Long expireSeconds, String lockAt, boolean readType);

	@NotNull
	List<LockDO> listLockedDatas(String lockName, String nowStr);
	
	@NotNull
	List<LockDO> listLockedDataInterProcess(String lockName, String identifier, boolean readType, String nowStr);

	int deleteRow(String lockName, String identifier);

	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	class LockDO {
		private String identifier;
		private boolean readType;
	}
}
