package com.tuotuogroup.core.pagination;



/**
 * 
*
* @描述：   
* @创建人：Liuyong   
* @创建时间：2012-8-9 上午12:50:05   
* @修改人：Liuyong   
* @修改时间：2012-8-9 上午12:50:05   
* @修改备注：   
* @version 1.0
*
 */
public class DataGridModel {
	
	private static final long serialVersionUID = 7232798260610351343L;
	/*
	private int page; //当前页,名字必须为page
	private int rows ; //每页大小,名字必须为rows
	*/
	private String sort; //排序字段
	private String order; //排序规则
	private SortEntity sortEntity;
	/*
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}*/
	
	public String getSort() {
		return sort;
	}
	public SortEntity getSortEntity() {
		return sortEntity;
	}
	public void setSortEntity(SortEntity sortEntity) {
		this.sortEntity = sortEntity;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	private Pagination pagination = new Pagination();

	public Pagination getPagination() {
		return pagination;
	}
	

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
	
}
