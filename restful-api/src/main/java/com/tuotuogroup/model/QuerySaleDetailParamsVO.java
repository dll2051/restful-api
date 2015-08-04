package com.tuotuogroup.model;

import java.io.Serializable;

public class QuerySaleDetailParamsVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2837050406781405987L;
	/**
	 * 启用状态，为-1或null时时表示查询全部
	 */
	private Integer active;
	/**
	 * 关键字
	 */
	private String keyword;
	/**
	 * 字典类型
	 */
	private String typeCode;
	/**
	 * 父ID
	 */
	private String parentId;

	/**
	 * @return the active
	 */
	public Integer getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Integer active) {
		this.active = active;
	}

	/**
	 * @return the typeCode
	 */
	public String getTypeCode() {
		return typeCode;
	}

	/**
	 * @param typeCode
	 *            the typeCode to set
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
