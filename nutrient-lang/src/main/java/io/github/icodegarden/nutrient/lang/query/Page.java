/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2022 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.icodegarden.nutrient.lang.query;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;

/**
 * 
 * @author Fangfang.Xu
 *
 * @param <E>
 */
@ToString
public class Page<E> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;

	/**
	 * 页码，从1开始
	 */
	private int pageNum;
	/**
	 * 页面大小
	 */
	private int pageSize;
	/**
	 * 总数
	 */
	private long totalCount;
	/**
	 * 总页数
	 */
	private int totalPages;
	/**
	 * 包含count查询
	 */
	private boolean count = true;
	/**
	 * 排序
	 */
	private String orderBy;

	public Page(int pageNum, int pageSize) {
		this(pageNum, pageSize, true);
	}

	public Page(int pageNum, int pageSize, boolean count) {
		super(0);
		if (pageNum == 1 && pageSize == Integer.MAX_VALUE) {
			pageSize = 0;
		}
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.count = count;
	}

	public List<E> getResult() {
		return this;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public Page<E> setTotalPages(int totalPages) {
		this.totalPages = totalPages;
		return this;
	}

	public int getPageNum() {
		return pageNum;
	}

	public Page<E> setPageNum(int pageNum) {
		this.pageNum = pageNum <= 0 ? 1 : pageNum;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Page<E> setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		if (totalCount == -1) {
			totalPages = 1;
			return;
		}
		if (pageSize > 0) {
			totalPages = (int) (totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1));
		} else {
			totalPages = 0;
		}
//		if (pageNum > totalPages) {
//			if (totalPages != 0) {
//				pageNum = totalPages;
//			}
//		}
	}

	public String getOrderBy() {
		return orderBy;
	}

	public <E> Page<E> setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return (Page<E>) this;
	}

	public boolean isCount() {
		return this.count;
	}

	public Page<E> setCount(boolean count) {
		this.count = count;
		return this;
	}

}
