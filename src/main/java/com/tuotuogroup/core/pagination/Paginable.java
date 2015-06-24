package com.tuotuogroup.core.pagination;

/**
 * 
 * 分页接口
 * 
 * @author Administrator 
 * 
 */
public interface Paginable {
	/**
	 * 总记录数
	 * 
	 * @return
	 */
	public int getTotal();

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPage();

	/**
	 * 每页记录数
	 * 
	 * @return
	 */
	public int getRows();

	/**
	 * 当前页号
	 * 
	 * @return
	 */
	public int getPage();

	/**
	 * 是否第一页
	 * 
	 * @return
	 */
	public boolean isFirstPage();

	/**
	 * 是否最后一页
	 * 
	 * @return
	 */
	public boolean isLastPage();

	/**
	 * 返回下页的页号
	 */
	public int getNextPage();

	/**
	 * 返回上页的页号
	 */
	public int getPrePage();
}
