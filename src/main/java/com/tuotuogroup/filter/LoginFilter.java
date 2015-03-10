
package com.tuotuogroup.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class LoginFilter implements Filter {

	private String myMatchUrl = "";

	private String returnPage = "";

	private String notLoginUrl = "";

	private String[] arrayMyMatchUtl;


	/**
	 * 
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public String getMyMatchUrl() {
		return myMatchUrl;
	}

	public void setMyMatchUrl(String myMatchUrl) {
		this.myMatchUrl = myMatchUrl;
	}

	public String getReturnPage() {
		return returnPage;
	}

	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}
	public String getNotLoginUrl() {
		return notLoginUrl;
	}
	public void setNotLoginUrl(String notLoginUrl) {
		this.notLoginUrl = notLoginUrl;
	}

	/**
	 * 过滤器
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		if (myMatchUrl == null || "".equals(myMatchUrl)) {

			myMatchUrl = config.getInitParameter("myMatchUrl");
			if (myMatchUrl != null) {
				myMatchUrl = myMatchUrl.toUpperCase();
			}
		}
		if (returnPage == null || "".equals(returnPage)) {
			returnPage = config.getInitParameter("returnPage");
			if (returnPage == null || "".equals(returnPage)) {
				returnPage = "login/login.jsp";
			}
		}
		if (arrayMyMatchUtl == null && myMatchUrl != null) {
			arrayMyMatchUtl = myMatchUrl.trim().split(",");
		}
	}

}
