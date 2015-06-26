/**
 * 
 */
package com.tuotuogroup.utils.browser;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.gson.Gson;
import com.tuotuogroup.entity.SaleDetail;
import com.tuotuogroup.model.ResultDataVO;
import com.tuotuogroup.utils.GsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author wonders
 * 
 */
public class AppTest {
	public static void main(String[] args) throws Exception {
		// String oldUrl = "http://zhufu.sinaapp.com/api/go.php?id=1077983";
		String oldUrl = "http://zhufu.sinaapp.com/api/getdata.php?v=1.1.6&page=1";
		HtmlJsoup hj = new HtmlJsoup();
		String data = hj.getPage(oldUrl, null, null, null, "UTF-8",
				"UTF-8").body().html();
		//System.out.println(data);
		ResultDataVO vo = GsonUtil.fromJson(data, ResultDataVO.class);
		List<SaleDetail> saleList = vo.getData();
		for (SaleDetail saleDetail : saleList) {
			System.out.println(saleDetail.getMallname());
		}
		System.out.println(vo.getMaxid());
		/*  
		String oldUrl = "http://zhufu.sinaapp.com/api/go.php?id=1430003";
		HtmlJsoup hj = new HtmlJsoup();
		String script = hj.getPage(oldUrl, null, null, null, "UTF-8",
				"UTF-8").body().html();
		String[] str = script.split(">");
		String func = str[1].replace("</script", "").replace("eval(", "").replace("\n", "").trim();
		func = func.substring(0, func.length()-1);
		 
		System.out.println(func);*/
		 
		String teStr = "function(p,a,c,k,e,d){e=function(c){return c.toString(36)};if(!''.replace(/^/,String)){while(c--){d[c.toString(a)]=k[c]||c.toString(a)}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('0 2=\'1://7.8.9/6.b?5=3\';0 4=\'1://a.n.c/k=l&m=i\';h(d(){e.f.g=2},j);',24,24,'var|http|uVUXTwpBR|43618274931|IdYjhbtfm|id|item|detail|tmall|com|hRK|htm|Nde|function|window|location|href|setTimeout|GZQovtaZ|1000|gmYbNV|aTWqnhE|SPN|qrfZrT'.split('|'),0,{})";
		//System.out.println(teStr);
		
		//excuteScript(teStr);
		/*String p = "1 2='0://8.9.a/b/7-c-5-3.4';1 6='0://p.d.m/n=o&k=j';f(e(){g.h.i=2},l);";
		String a = "26";
		String c = "26";
		String k = "http,var,MHgfUmAEz,02,htm,6JV,HtRpGHpyl,A28,www,newegg,cn,Product,032,ispvef,function,setTimeout,window,location,href,XlkmelFv,knO,1000,myJ,jzVBql,GcfcnUA,ItS";
		String e = "0";
		String d = "";
		excuteScript(p, a, c, k, e, d);*/
		
		
		
	}

	public static void excuteScript(String p,String a,String c,String k,String e,String d) {
		ScriptEngineManager sem = new ScriptEngineManager(); /* script引擎管理 */
		ScriptEngine se = sem.getEngineByName("javascript"); /* script引擎 */
		try {
			se.eval("function splitHref(   ) { "
//					+ "var html = "+func
					+ "var html = ' var MHgfUmAEz=\\'http://www.newegg.cn/Product/A28-032-6JV-02.htm\\';var HtRpGHpyl=2;setTimeout(function(){window.location.href=MHgfUmAEz},1000);';"
					+ "var obj = html.split(';');"
					+ "var value = obj[0].split('=');"
					+ "var newHref = value[1];"
					+ "return newHref;"
					+ "}"); 
			StringBuffer sb =new StringBuffer();
			sb.append("var p = "+"1 2=\\'0://8.9.a/b/7-c-5-3.4\\';1 6=\\'0://p.d.m/n=o&k=j\\';f(e(){g.h.i=2},l);"+";");
			sb.append("var a= 26;");
			sb.append("var c = 26;");
			sb.append("var k = 'http,var,MHgfUmAEz,02,htm,6JV,HtRpGHpyl,A28,www,newegg,cn,Product,032,ispvef,function,setTimeout,window,location,href,XlkmelFv,knO,100000,myJ,jzVBql,GcfcnUA,ItS';");
			sb.append("var e = 0;");
			sb.append("var d;");
			
			sb.append("function func1(c){ return c.toString(36); }");
			sb.append("e = func1(c);");
			//sb.append("if(!''.replace(/^/,String)){while(c--){d[c.toString(a)]=k[c]||c.toString(a);}");
			
			
			
			
			sb.append("print('Hello '+e+'!');");
			se.eval(sb.toString());
			
			
			
			
/*			se.eval(" var strname = 'Key' ");  执行一段script 
			
			se.eval("function sayHello(   ) { "
					+ " print('Hello '+strname+'!');return 'my name is '+strname;"
					+ "}");  添加一个方法 
*/		
			Invocable invocableEngine = (Invocable) se;
			String callbackvalue = (String) invocableEngine
					.invokeFunction("splitHref"); /* 调用方法中的函数 */
			System.out.println(callbackvalue);
			/** 打印返回值 */

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
