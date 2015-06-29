package com.tuotuogroup.core.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @描述:返回给前台AJAX调用的结果VO
 */
@XmlRootElement()  
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ResultVO<T>{
	/**
	 * 返回给客户端的结果
	 */
	private String result;
	/**
	 * 返回给客户端的提示信息
	 */
	private String message;
	/**
	 * 返回给客户端的查询数据
	 */
	private T data;
	
	/**
	 * 构造函数
	 */
	public ResultVO() {
		super();
	}
	/**
	 * 构造函数
	 * @param data 数据
	 */
	public ResultVO(T data) {
		super();
		this.result = ResultConstant.RESULT_SUCCESS;
		this.message = ResultConstant.MESSAGE_SUCCESS;
		this.data = data;
	}
	/**
	 * 构造函数
	 * @param result 执行结果
	 * @param message 信息
	 * @param data 数据
	 */
	public ResultVO(String result, String message, T data) {
		super();
		this.result = result;
		this.message = message;
		this.data = data;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
	
}
