/**
 * 创建日期：Feb 18, 2013
 * 作者： "王胤洪"
 * 版权： 指明该文件的版权信息
 * 功能： 指明该文件所实现的功能
 */

package com.tuotuogroup.core.pagination;

import java.io.Serializable;
import java.util.Collection;

public class PaginationResult implements Serializable {
	 private static final long serialVersionUID = -2610331321553743461L;
	  private int totalSize;
	  private Collection result;
	  private PaginationRequest request;

	  public PaginationRequest getRequest()
	  {
	    return this.request;
	  }

	  public PaginationResult(int totalSize, Collection result, PaginationRequest request)
	  {
	    this.totalSize = totalSize;
	    this.result = result;
	    this.request = request;
	  }

	  public Collection getResult() {
	    return this.result;
	  }

	  public int getTotalSize() {
	    return this.totalSize;
	  }
}
