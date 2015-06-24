package com.tuotuogroup.core.pagination;

/**
*
* @描述： 查询用的排序类  
* @创建人：JW   
* @创建时间：2012-8-10 上午10:32:59   
* @修改人： 
* @修改时间： 
* @修改备注：   
* @version 1.0
*
 */
public class SortEntity {

	public final static String ASC = "asc";//升序
	
	public final static String DESC = "desc";//降序
	
	private String sortPropertyAlliesName;//排序字段别名
	
	private String sortType;//排序方式 ASC || DESC
	
	public SortEntity(){
		
	}
	
	public String toHQL(String queryProperty){
		return queryProperty + " " + this.sortType;
	}
	
	public SortEntity(String sortPropertyAlliesName,String sortType){
		this.sortType = sortType;
		this.sortPropertyAlliesName = sortPropertyAlliesName;
	}

	public String getSortPropertyAlliesName() {
		return sortPropertyAlliesName;
	}

	public void setSortPropertyAlliesName(String sortPropertyAlliesName) {
		this.sortPropertyAlliesName = sortPropertyAlliesName;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
}
