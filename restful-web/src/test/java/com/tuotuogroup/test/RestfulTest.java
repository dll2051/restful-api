package com.tuotuogroup.test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javax.transaction.SystemException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RestfulTest {

	private static String wsdlUrl;
	private static String methodAllUser;
	private static String methodAllUserByPage;
	private static String methodUsers;
	private static String methodUsersByPage;
	
	
	/*static { 
		ResourceBundle rb = ResourceBundle.getBundle("wsdl");
		wsdlUrl = rb.getString("wsdlUrl");
		methodAllUser = rb.getString("methodAllUser");
		methodAllUserByPage = rb.getString("methodAllUserByPage");
		methodUsers = rb.getString("methodUsers");
		methodUsersByPage = rb.getString("methodUsersByPage");
	}*/

	public static String getWsdlUrl() {
		return wsdlUrl;
	}

	public static String getMethodAllUser() {
		return methodAllUser;
	}

	public static String getMethodAllUserByPage() {
		return methodAllUserByPage;
	}

	public static String getMethodUsers() {
		return methodUsers;
	}

	public static String getMethodUsersByPage() {
		return methodUsersByPage;
	}

	public static String getXmlStr(Object[] params) {
		Object[] results = null;
		/*try {
			URL url = new URL(wsdlUrl);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.connect();
			Client client = new Client(httpConnection.getInputStream(), null);
			if(params == null || params.length == 0) {
				params = new Object[] {};
			}
			results = client.invoke(methodUsersByPage, params);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return (String) results[0];
	}

	public static String getAllUserXml() {
		Object[] results = null;
		try {
			URL url = new URL(wsdlUrl);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.connect();
			/*Client client = new Client(httpConnection.getInputStream(), null);
			results = client.invoke(methodAllUser, new Object[] {});*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (String) results[0];
	}
	
	public static void main(String[] args) throws Exception{
//		testRestful();
		System.out.println(getMethodInvoke());
	}
	/**
	* get调用
	* 
	* @param url
	* @return
	* @throws Exception
	*/
	public static String getMethodInvoke() throws Exception {
		String url = "http://localhost:8080/restful-api/restful/searchservice/saleDetailVo/天猫/all";
		String responseMsg = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			httpget.setHeader("Content-Type", "application/xml; charset=utf-8");
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			responseMsg = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			throw new SystemException("webservice请求异常");
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return responseMsg;
	}
	public static void testRestful(){
		String baseAddress = "http://localhost:8080/restful-api/restful/searchservice/";
        String result = WebClient.create(baseAddress)
        		.acceptEncoding("UTF-8").encoding("UTF-8")
            .path("/saleDetailVo").path("天猫").path("all")
            .query("isSelectState", "true")
            .accept(MediaType.APPLICATION_JSON)
            .get(String.class);
        System.out.println(result);
	}	
}
