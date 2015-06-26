package com.tuotuogroup.controller;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuotuogroup.core.web.ResultConstant;
import com.tuotuogroup.core.web.ResultVO;
import com.tuotuogroup.entity.SaleDetail;
import com.tuotuogroup.model.ResultDataVO;
import com.tuotuogroup.service.SaleService;
import com.tuotuogroup.service.TaskService;
import com.tuotuogroup.utils.GsonUtil;
import com.tuotuogroup.utils.SourceAPIUtil;
import com.tuotuogroup.utils.browser.HtmlJsoup;

/**
 * 
 * @描述:任务Controller类
 */
@Controller
public class TaskController {
	/**
	 * 日志
	 */
	private Log log = LogFactory.getLog(getClass());

	/**
	 * 任务Service
	 */
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SaleService saleService;


	/**
	 * 跳转到字典管理首页
	 * 
	 * @return 页面地址
	 */
	@RequestMapping(value = "task/toInterface")
	public String taskInterface(String dictionaryId, String dictionaryName, String dictionaryCode) {
		return "task/taskcount";
	}
	/**
	 * 获取数据并保存
	 * @return
	 */
	@RequestMapping(value = "task/interface")
	@ResponseBody
	public Map<String,Object> interfaceView(HttpServletRequest request) throws Exception  {
		Map<String,Object> result = new HashMap<String, Object>();
		String url = SourceAPIUtil.getGetDataUrl();
		HtmlJsoup hj = new HtmlJsoup();
		String data = hj.getPage(url, null, null, null, "UTF-8","UTF-8").body().html();
		System.out.println(data);
		ResultDataVO vo = GsonUtil.fromJson(data, ResultDataVO.class);
		List<SaleDetail> saleList = vo.getData();
		int count = 0;
		for (SaleDetail saleDetail : saleList) {
			saleDetail.setStatus(0);//未处理标志
			saleService.saveOrUpdate(saleDetail);
			count++;
		}
		result.put("count", String.valueOf(count));
		return result;
	}
	@RequestMapping(value = "task/toControl")
	public String toControlView() {
		return "task/control_view";
	}
	@RequestMapping(value = "task/go")
	@ResponseBody
	public Map<String, Object> goTaskView(HttpServletRequest request) throws Exception  {
		Map<String,Object> result = new HashMap<String, Object>();
		SaleDetail saleDetail = saleService.getOneSaleDetail();
		if(null!=saleDetail){
			String oldUrl = saleDetail.getUrl();
			HtmlJsoup hj = new HtmlJsoup();
			String script = hj.getPage(oldUrl, null, null, null, "UTF-8",
					"UTF-8").body().html();
			String[] str = script.split(">");
			String func = str[1].replace("</script", "").replace("eval(", "").replace("\n", "").trim();
			func = func.substring(0, func.length()-1);
			result.put("id", saleDetail.getId());
			func = "var funcstr = "+func+"; var obj = funcstr.split(\";\");var value = obj[0].split(\"=\"); var newHref = value[1];";
			result.put("js", func);
		}else{
			result.put("id", "error");
		}
		return result;
	}
	@RequestMapping(value = "task/toSaleSave")
	public String toSaleSave(HttpServletRequest request,String id,String url,String error,Model model) throws Exception  {
		Map<String,String> map = new HashMap<String, String>();
		StringBuffer formStr = new StringBuffer();
		if("error".equals(error)){
			formStr.append("<form  method = 'post' id = 'chooseForm' action='../saleDetail/errorSaleUrl.action'>\n");
			formStr.append("<input name='id' value='"+id+"' type='hidden'/>\n");
			formStr.append("</form>");
		}else{
			formStr.append("<form  method = 'post' id = 'chooseForm' action='../saleDetail/update.action'>\n");
			formStr.append("<input name='id' value='"+id+"' type='hidden'/>\n");
			formStr.append("<input name='url' value='"+url+"' type='hidden'/>\n");
			formStr.append("</form>");
		}
		map.put("data", formStr.toString());
		model.addAttribute("params", JSONObject.fromObject(map).toString());
		return "task/formjsp";
	}
	/**
	 * 保存字典
	 * 
	 * @return ResultVO
	 */
	@RequestMapping(value = "task/saveOrUpdate")
	@ResponseBody
	public ResultVO saveOrUpdate(String json) {
		ResultVO resultVO = null;
		try {
			JSONObject jsonObject = JSONObject.fromObject(json);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(SaleDetail.class);
			@SuppressWarnings("rawtypes")
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("dictionary", Dictionary.class);
			classMap.put("dictionaryItemProperties", SaleDetail.class);
			jsonConfig.setClassMap(classMap);
			SaleDetail dictionaryItem = (SaleDetail) JSONObject.toBean(jsonObject,
					jsonConfig);
//			taskService.saveOrUpdate(dictionaryItem);
			// 将修改后的值返回客户端更新UI
			SaleDetail treeVO = new SaleDetail();
			BeanUtils.copyProperties(treeVO, dictionaryItem);
			resultVO = new ResultVO(treeVO);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultVO.setResult(ResultConstant.RESULT_FAIL);
			resultVO.setMessage(e.getMessage());
		}
		return resultVO;
	}

}
