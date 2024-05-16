package io.github.icodegarden.nutrient.mybatis.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import io.github.icodegarden.nutrient.lang.query.BaseQuery;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class PageHelperUtils {

	public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count) {
		return startPage(pageNum, pageSize, count, null);
	}

	public static <E> Page<E> startPage(int pageNum, int pageSize, String orderBy) {
		return startPage(pageNum, pageSize, true, orderBy);
	}

	public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count, String orderBy) {
		return PageHelper.startPage(pageNum, pageSize, count).setOrderBy(orderBy);
	}

	/**
	 * 只转换类型
	 */
	public static <E, T> Page<E> ofPage(Page<T> page, Function<T, E> elementConvertor) {
		Page<E> newPage = new Page<E>(page.getPageNum(), page.getPageSize());
		newPage.setTotal(page.getTotal());
		newPage.setPages(page.getPages());
		newPage.setCount(page.isCount());
		newPage.setOrderBy(page.getOrderBy());

		convertAddAll(page, elementConvertor, newPage);

		return newPage;
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 */
	public static <E> Page<E> ofPageNoCountAdapt(Page<E> page) {
		ofPageNoCountAdapt(page, BaseQuery.MAX_TOTAL_COUNT);
		return page;
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 * 
	 * @param maxTotal 当没有进行count时，限制的最多条数，例如10000条
	 */
	public static <E> Page<E> ofPageNoCountAdapt(Page<E> page, long maxTotal) {
		noCountAdapt(page, page, maxTotal);
		return page;
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 */
	public static <E, T> Page<E> ofPageNoCountAdapt(Page<T> page, Function<T, E> elementConvertor) {
		return ofPageNoCountAdapt(page, elementConvertor, BaseQuery.MAX_TOTAL_COUNT);
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 * 
	 * @param maxTotal 当没有进行count时，限制的最多条数，例如10000条
	 */
	public static <E, T> Page<E> ofPageNoCountAdapt(Page<T> page, Function<T, E> elementConvertor, long maxTotal) {
		Page<E> newPage = new Page<E>(page.getPageNum(), page.getPageSize());
		noCountAdapt(page, newPage, maxTotal);

		convertAddAll(page, elementConvertor, newPage);

		return newPage;
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 * 
	 * 分页的按正常处理<br>
	 * 不分页但结果条数小于页大小，则总页数按当前页处理，总条数按(总页数-1)*每页大小+当前返回条数<br>
	 * 不分页但结果条数等于页大小，则按最大值处理
	 * 
	 * @param maxTotal 当没有进行count时，限制的最多条数，例如10000条
	 */
	private static void noCountAdapt(Page<?> page, Page<?> targetPage, long maxTotal) {
		if (page.isCount()) {
			targetPage.setTotal(page.getTotal());
			targetPage.setPages(page.getPages());
		} else {
			if (page.getResult().size() < page.getPageSize()) {
				targetPage.setTotal((page.getPageNum() - 1) * page.getPageSize() + page.getResult().size());
				targetPage.setPages(page.getPageNum());
			} else {
				targetPage.setTotal(maxTotal);
				targetPage.setPages((int) (maxTotal / page.getPageSize()));
			}
		}

		targetPage.setCount(page.isCount());
		targetPage.setOrderBy(page.getOrderBy());
	}

	private static <E, T> void convertAddAll(Page<T> page, Function<T, E> elementConvertor, Page<E> targetPage) {
		if (!page.getResult().isEmpty()) {
			List<E> list = page.getResult().stream().map(t -> {
				return elementConvertor.apply(t);
			}).collect(Collectors.toList());

			targetPage.addAll(list);
		}
	}

	public static <E> io.github.icodegarden.nutrient.lang.query.Page<E> convertPage(Page<E> page) {
		io.github.icodegarden.nutrient.lang.query.Page<E> newPage = new io.github.icodegarden.nutrient.lang.query.Page<E>(
				page.getPageNum(), page.getPageSize());
		newPage.setTotalCount(page.getTotal());
		newPage.setTotalPages(page.getPages());
		newPage.setCount(page.isCount());
		newPage.setOrderBy(page.getOrderBy());

		if (page.getResult() != null) {
			newPage.addAll(page.getResult());
		}

		return newPage;
	}
}