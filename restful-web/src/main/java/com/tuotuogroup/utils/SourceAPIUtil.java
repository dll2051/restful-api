package com.tuotuogroup.utils;

import java.util.ResourceBundle;
public class SourceAPIUtil {

	private static String hostUrl;
	private static String methodGetData;
	private static String methodGo;
	
	static { 
		ResourceBundle rb = ResourceBundle.getBundle("dataSource");
		hostUrl = rb.getString("hostUrl");
		methodGetData = rb.getString("methodGetData");
		methodGo = rb.getString("methodGo");
	}

	private static String getHostUrl() {
		return hostUrl;
	}
	private static String getMethodGetData() {
		return methodGetData;
	}
	private static String getMethodGo() {
		return methodGo;
	}
	public static String getGetDataUrl() {
		return getHostUrl()+"/"+getMethodGetData();
	}
	public static String getGoUrl() {
		return getHostUrl()+"/"+getMethodGo();
	}
}
