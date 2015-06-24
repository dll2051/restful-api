/**
 * 创建日期：Feb 18, 2013
 * 作者： "王胤洪"
 * 版权： 指明该文件的版权信息
 * 功能： 指明该文件所实现的功能
 */

package com.tuotuogroup.core.pagination;

import java.io.Serializable;

public class PaginationRequest implements Serializable
{
	  private static final long serialVersionUID = -631591805020269970L;
	  private int pageNumber;
	  private int pageSize;

	  public PaginationRequest(int pageNumber, int pageSize)
	  {
	    this.pageNumber = ((pageNumber > 0) ? pageNumber : 1);
	    this.pageSize = ((-1 == pageSize) ? PaginationUtil.getDefaultPageSize() : pageSize);
	  }

	  public int getPageSize()
	  {
	    return this.pageSize;
	  }

	  public int getStartRow()
	  {
	    return ((this.pageNumber - 1) * this.pageSize);
	  }

	  public int getPageNumber()
	  {
	    return this.pageNumber;
	  }

}
