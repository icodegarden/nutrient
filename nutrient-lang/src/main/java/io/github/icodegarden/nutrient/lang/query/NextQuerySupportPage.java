package io.github.icodegarden.nutrient.lang.query;

/**
 * 
 * @author Fangfang.Xu
 *
 * @param <E>
 */
public class NextQuerySupportPage<E> extends Page<E> implements NextQuerySupportList<E> {

	private static final long serialVersionUID = 1L;

	private final NextQuerySupportList<? extends E> nextQuerySupportList;

	public NextQuerySupportPage(Page<E> p, NextQuerySupportList<? extends E> nextQuerySupportList) {
		this(p.getPageNum(), p.getPageSize(), p.getTotalCount(), p.isCount(), p.getOrderBy(), nextQuerySupportList);
	}

	public NextQuerySupportPage(int pageNum, int pageSize, long totalCount, boolean count, String orderBy,
			NextQuerySupportList<? extends E> nextQuerySupportList) {
		super(pageNum, pageSize);
//		super.setPages(pages);pages会自动算
		super.setTotalCount(totalCount);
		super.setCount(count);
		super.setOrderBy(orderBy);

		this.nextQuerySupportList = nextQuerySupportList;
		addAll(nextQuerySupportList);
	}

	@Override
	public boolean hasNextPage() {
		return nextQuerySupportList.hasNextPage();
	}

	@Override
	public String getSearchAfter() {
		return nextQuerySupportList.getSearchAfter();
	}

}
