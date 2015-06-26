package com.tuotuogroup.model;

import java.io.Serializable;
import java.util.List;

import com.tuotuogroup.entity.SaleDetail;

public class ResultDataVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2527254854666673613L;
	/**
	 * 返回给客户端的错误码
	 */
	private String errno;
	/**
	 * 返回给客户端的当前页最大ID
	 */
	private String maxid;
	/**
	 * 返回给客户端的查询数据
	 */
	private List<SaleDetail> data;

	/**
	 * 构造函数
	 */
	public ResultDataVO() {
		super();
	}

	/**
	 * 构造函数
	 * @param data 数据
	 */
	public ResultDataVO(List<SaleDetail> data) {
		super();
		this.errno = ResultConstant.RESULT_SUCCESS;
		this.maxid = ResultConstant.MESSAGE_SUCCESS;
		this.data = data;
	}

	/**
	 * 构造函数
	 * @param result 执行结果
	 * @param message 信息
	 * @param data 数据
	 */
	public ResultDataVO(String errno, String maxid, List<SaleDetail> data) {
		super();
		this.errno = errno;
		this.maxid = maxid;
		this.data = data;
	}


	/**
	 * @return the data
	 */
	public List<SaleDetail> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<SaleDetail> data) {
		this.data = data;
	}

	public String getErrno() {
		return errno;
	}

	public String getMaxid() {
		return maxid;
	}

	public void setErrno(String errno) {
		this.errno = errno;
	}

	public void setMaxid(String maxid) {
		this.maxid = maxid;
	}
}
