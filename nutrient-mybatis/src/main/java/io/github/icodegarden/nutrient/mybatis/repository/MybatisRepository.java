package io.github.icodegarden.nutrient.mybatis.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.github.icodegarden.nutrient.lang.query.BaseQuery;
import io.github.icodegarden.nutrient.lang.repository.Repository;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public interface MybatisRepository<PO, U, Q extends BaseQuery, W, DO> extends Repository<PO, U, Q, W, DO, Object> {

	DO findOne(@Param("id") Object id, @Param("with") W with);

	List<DO> findByIds(@Param("ids") List<Object> ids, @Param("with") W with);

	void increment(@Param("id") Object id, @Param("fieldName") String fieldName, @Param("value") long value);
}
