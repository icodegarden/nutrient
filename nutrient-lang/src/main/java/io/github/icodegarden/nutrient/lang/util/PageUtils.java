package io.github.icodegarden.nutrient.lang.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.icodegarden.nutrient.lang.query.BaseQuery;
import io.github.icodegarden.nutrient.lang.query.NextQuerySupportArrayList;
import io.github.icodegarden.nutrient.lang.query.NextQuerySupportList;
import io.github.icodegarden.nutrient.lang.query.NextQuerySupportPage;
import io.github.icodegarden.nutrient.lang.query.Page;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public abstract class PageUtils {

	private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<Page>();

	public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count) {
		return startPage(pageNum, pageSize, count, null);
	}

	public static <E> Page<E> startPage(int pageNum, int pageSize, String orderBy) {
		return startPage(pageNum, pageSize, true, orderBy);
	}

	public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count, String orderBy) {
		Page<E> page = new Page<E>(pageNum, pageSize, count);
		page.setOrderBy(orderBy);

		LOCAL_PAGE.set(page);

		return page;
	}

	/**
	 * 是否执行了分页，即是否执行了startPage 且 正在分页中<br>
	 * 如果分页调用已经结束，则是false，因为LocalPage已被自动remove
	 * 
	 * @return
	 */
	public static boolean isPage() {
		Page<Object> page = LOCAL_PAGE.get();
		return page != null;
	}

	/**
	 * 是否执行count，即是否执行了startPage 且 正在分页中 + count参数=true<br>
	 * 否则false
	 * 
	 * @return
	 */
	public static boolean isCount() {
		Page<Object> page = LOCAL_PAGE.get();
		return page != null ? page.isCount() : false;
	}

	public static void clearPage() {
		LOCAL_PAGE.remove();
	}

	/**
	 * 只转换类型
	 */
	public static <E, T> Page<E> ofPage(Page<T> page, Function<T, E> elementConvertor) {
		Page<E> newPage = new Page<E>(page.getPageNum(), page.getPageSize());
		newPage.setTotalCount(page.getTotalCount());
		newPage.setTotalPages(page.getTotalPages());
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
			targetPage.setTotalCount(page.getTotalCount());
			targetPage.setTotalPages(page.getTotalPages());
		} else {
			if (page.getResult().size() < page.getPageSize()) {
				targetPage.setTotalCount((page.getTotalPages() - 1) * page.getPageSize() + page.getResult().size());
				targetPage.setTotalPages(page.getPageNum());
			} else {
				targetPage.setTotalCount(maxTotal);
				targetPage.setTotalPages((int) (maxTotal / page.getPageSize()));
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

	// ----------------------------------------------------------

	/**
	 * Page 转 NextQuerySupportPage
	 */
	public static <E> NextQuerySupportPage<E> pageToNextQuerySupportPage(Page<E> page,
			Function<E, String> searchAfterSupplier) {
		NextQuerySupportList<E> nextQuerySupportList = NextQuerySupportArrayList.newSupportSearchAfter(page,
				searchAfterSupplier);
		return new NextQuerySupportPage<E>(page.getPageNum(), page.getPageSize(), page.getTotalCount(), page.isCount(),
				page.getOrderBy(), nextQuerySupportList);
	}

	/**
	 * Page 转 NextQuerySupportPage
	 */
	public static <T, E> NextQuerySupportPage<E> pageToNextQuerySupportPage(Page<T> page,
			Function<T, E> elementConvertor, Function<T, String> searchAfterSupplier) {
		NextQuerySupportList<E> nextQuerySupportList = NextQuerySupportArrayList.newSupportSearchAfter(page,
				elementConvertor, searchAfterSupplier);
		return new NextQuerySupportPage<E>(page.getPageNum(), page.getPageSize(), page.getTotalCount(), page.isCount(),
				page.getOrderBy(), nextQuerySupportList);
	}

	/**
	 * 只转换类型
	 */
	public static <T, E> NextQuerySupportPage<E> ofNextQuerySupportPage(NextQuerySupportPage<T> page,
			Function<T, E> elementConvertor) {
		NextQuerySupportList<E> nextQuerySupportList = NextQuerySupportArrayList.newSupportSearchAfter(page,
				elementConvertor, one -> page.getSearchAfter());

		return new NextQuerySupportPage<E>(page.getPageNum(), page.getPageSize(), page.getTotalCount(), page.isCount(),
				page.getOrderBy(), nextQuerySupportList);
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 */
	public static <E> NextQuerySupportPage<E> ofNextQuerySupportPageNoCountAdapt(NextQuerySupportPage<E> page) {
		return ofNextQuerySupportPageNoCountAdapt(page, BaseQuery.MAX_TOTAL_COUNT);
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 * 
	 * @param maxTotal 当没有进行count时，限制的最多条数，例如10000条
	 */
	public static <E> NextQuerySupportPage<E> ofNextQuerySupportPageNoCountAdapt(NextQuerySupportPage<E> page,
			long maxTotal) {
		noCountAdapt(page, page, maxTotal);
		return page;
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 */
	public static <E, T> NextQuerySupportPage<E> ofNextQuerySupportPageNoCountAdapt(NextQuerySupportPage<T> page,
			Function<T, E> elementConvertor) {
		return ofNextQuerySupportPageNoCountAdapt(page, elementConvertor, BaseQuery.MAX_TOTAL_COUNT);
	}

	/**
	 * <h1>自适应可能不进行count</h1>
	 * 
	 * @param maxTotal 当没有进行count时，限制的最多条数，例如10000条
	 */
	public static <E, T> NextQuerySupportPage<E> ofNextQuerySupportPageNoCountAdapt(NextQuerySupportPage<T> page,
			Function<T, E> elementConvertor, long maxTotal) {
		NextQuerySupportPage<E> newPage = ofNextQuerySupportPage(page, elementConvertor);
		noCountAdapt(page, newPage, maxTotal);

		return newPage;
	}
}