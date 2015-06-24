package com.tuotuogroup.core.pagination;

/**
 * 简单分页类
 * 
 * @author Administrator 
 * 
 */
public class SimplePage implements Paginable {
	private static final long serialVersionUID = 1L;
	public static final int DEF_COUNT = 20;

	/**
	 * 检查页码 checkpage
	 * 
	 * @param page
	 * @return if page==null or page<1 then return 1 else return page
	 */
	public static int cpn(Integer page) {
		return (page == null || page < 1) ? 1 : page;
	}

	public SimplePage() {
	}

	/**
	 * 构造器
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 */
	public SimplePage(int page, int rows, int totalCount) {
		setTotalCount(totalCount);
		setRows(rows);
		setPage(page);
		adjustpage();
	}

	/**
	 * 调整页码，使不超过最大页数
	 */
	public void adjustpage() {
		if (page == 1) {
			return;
		}
		int tp = getTotalPage();
		if (page > tp) {
			page = tp;
		}
	}

	/**
	 * 获得页码
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 每页几条数据
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * 总共几条数据
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 总共几页
	 */
	public int getTotalPage() {
		int totalPage = total / rows;
		if (totalPage == 0 || total % rows != 0) {
			totalPage++;
		}
		return totalPage;
	}


	public static int getTotalPage(int total) {
		return total%DEF_COUNT == 0 ? total/DEF_COUNT: (total/DEF_COUNT + 1);
	}
	
	/**
	 * 是否第一页
	 */
	public boolean isFirstPage() {
		return page <= 1;
	}

	/**
	 * 是否最后一页
	 */
	public boolean isLastPage() {
		return page >= getTotalPage();
	}

	/**
	 * 下一页页码
	 */
	public int getNextPage() {
		if (isLastPage()) {
			return page;
		} else {
			return page + 1;
		}
	}

	/**
	 * 上一页页码
	 */
	public int getPrePage() {
		if (isFirstPage()) {
			return page;
		} else {
			return page - 1;
		}
	}

	protected int total = 0;
	protected int rows = 20;
	protected int page = 1;

	/**
	 * if totalCount<0 then totalCount=0
	 * 
	 * @param totalCount
	 */
	public void setTotalCount(int total) {
		if (total < 0) {
			this.total = 0;
		} else {
			this.total = total;
		}
	}

	/**
	 * if pageSize< 1 then pageSize=DEF_COUNT
	 * 
	 * @param pageSize
	 */
	public void setRows(int rows) {
		if (rows < 1) {
			this.rows = DEF_COUNT;
		} else {
			this.rows = rows;
		}
	}

	/**
	 * if page < 1 then page=1
	 * 
	 * @param page
	 */
	public void setPage(int page) {
		if (page < 1) {
			this.page = 1;
		} else {
			this.page = page;
		}
	}

	/**
	 * 
	 * 功能：设置当前页，建议使用setPage
	 * 创建日期：Dec 4, 2012
	 * @author： "王胤洪"
	 * @param page
	 * void
	 * @deprecated
	 * ---------------------------------------
	 */
	@Deprecated
	public void setPageNo(int page){
	  setPage(page) ;
	}
	
	/**
   * 
   * 功能：设置当前大小，建议使用setRows
   * 创建日期：Dec 4, 2012
   * @author： "王胤洪"
   * @param pageSize
   * void
   * @deprecated
   * ---------------------------------------
   */
	@Deprecated
	public void setPageSize(int pageSize){
	   this.setRows(pageSize);
	}
	
	/**
	 * 
	 * 功能：得到总数据条数，建议使用getTotal
	 * 创建日期：Dec 4, 2012
	 * @deprecated
	 * @author： "王胤洪"
	 * @return
	 * int
	 * 如果该函数引用或修改了某些全局变量或对象，也应在函数级注释中说明
	 * ---------------------------------------
	 */
	
	@Deprecated
	public int getTotalCount(){
	  return this.getTotal();
	}
	
}
