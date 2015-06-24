/**
 * 创建日期：Feb 18, 2013
 * 作者： "王胤洪"
 * 版权： 指明该文件的版权信息
 * 功能： 指明该文件所实现的功能
 */

package com.tuotuogroup.core.pagination;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class PaginationWebUtil {
	public static final String PARAMETER_PAGE_SIZE = "pageSize";
	  public static final String PARAMETER_PAGE_NUMBER = "pageNumber";
	  public static final String PARAMETER_PAGE_GO_SUBMIT = "pageGoSubmit";
	  public static final String PAGINATION_PAGE_ID_KEY = "com.wonders.pdcdc.common.pagination.pageId";
	  public static final String PAGINATION_RESULT_KEY = "com.wonders.pdcdc.common.pagination.result";
	  public static final String PAGINATION_PAGE_SIZE_KEY = "com.wonders.pdcdc.common.pagination.pageSize";
	  public static final String PAGINATION_PAGE_NUMBER_KEY = "com.wonders.pdcdc.common.pagination.pageNumber";
	  public static final String PAGINATION_MAX_PAGE_NUMBER_KEY = "com.wonders.pdcdc.common.pagination.maxPageNumber";
	  public static final String PAGINATION_TARGET_LINK = "com.wonders.pdcdc.common.pagination.targetLink";

	  public static void savePaginationInfo(HttpServletRequest request, PaginationResult result)
	  {
	    request.setAttribute("com.wonders.pdcdc.common.pagination.result", result);
	  }

	  public static PaginationRequest getPaginationRequest(HttpServletRequest request)
	  {
	    return getPaginationRequest(request, -1);
	  }

	  public static PaginationRequest getPaginationRequest(HttpServletRequest request, int initPageSize) {
	    String strPageNumber = request.getParameter("pageNumber");
	    String strPageSize = request.getParameter("pageSize");

	    int pageNumber = 0;
	    int pageSize;
	    try {
	      pageSize = Integer.parseInt(strPageSize);
	    } catch (NumberFormatException e) {
	      pageSize = initPageSize;
	      pageNumber = 1;
	    }
	    if (pageNumber == 0) {
	      try {
	        pageNumber = Integer.parseInt(strPageNumber);
	      } catch (NumberFormatException e) {
	        pageNumber = 1;
	      }
	    }
	    return new PaginationRequest(pageNumber, pageSize);
	  }

	  public static void removeInternalParameters(Map parameterMap)
	  {
	    parameterMap.remove("temppageNumber");
	    parameterMap.remove("temppageSize");
	    parameterMap.remove("pageGoSubmit");
	    parameterMap.remove("pageGoSubmit.x");
	    parameterMap.remove("pageGoSubmit.y");
	  }
}
