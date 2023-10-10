package io.github.icodegarden.nutrient.lang.query;
import java.util.List;

import io.github.icodegarden.nutrient.lang.exception.DuplicateKeyException;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface TableDataCountStorage {
	
	void add(TableDataCountPO po) throws DuplicateKeyException;

	int updateCount(String tableName, long count);
	
	List<TableDataCountPO> findAll();
	
}