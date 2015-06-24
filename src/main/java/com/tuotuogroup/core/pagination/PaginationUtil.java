/**
 * 创建日期：Feb 18, 2013
 * 作者： "王胤洪"
 * 版权： 指明该文件的版权信息
 * 功能： 指明该文件所实现的功能
 */

package com.tuotuogroup.core.pagination;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;

public class PaginationUtil {
//	  private static final Logger logger = Logger(PaginationUtil.class);
	  private static final int DEFAULT_PAGE_SIZE = 15;
	  private static Properties props = null;

	  static {
	    props = new Properties();
	    try {
	      props.load(PaginationUtil.class.getResourceAsStream("pagination.properties"));
	    } catch (IOException e) {
//	      logger.warn("file pagination.properties is not found.", e);
	      props = null;
	    }
	  }

	  public static int getDefaultPageSize()
	  {
	    int pageSize = 0;
	    if (props != null) {
	      try {
	        pageSize = Integer.parseInt(props.getProperty("pagination.pageSize"));
	      } catch (NumberFormatException e) {
	        pageSize = 0;
	      }
	    }
	    if (pageSize > 0) {
	      return pageSize;
	    }
	    return 15;
	  }

	  public static int getMaxPageNumber(int totalSize, int pageSize)
	  {
	    if (totalSize == 0) return 1;
	    int maxPageNumber = totalSize / pageSize;
	    if (totalSize % pageSize > 0) ++maxPageNumber;
	    return maxPageNumber;
	  }
}
