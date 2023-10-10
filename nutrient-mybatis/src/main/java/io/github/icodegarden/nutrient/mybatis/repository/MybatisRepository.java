package io.github.icodegarden.nutrient.mybatis.repository;
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

}
