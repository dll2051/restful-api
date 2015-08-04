package com.tuotuogroup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuotuogroup.core.constant.ResultCode;
import com.tuotuogroup.core.constant.ResultMessage;
import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.core.web.ResultConstant;
import com.tuotuogroup.core.web.ResultVO;
import com.tuotuogroup.entity.SaleDetail;
import com.tuotuogroup.service.SaleService;

@Controller
public class SaleController {
	/**
	 * 日志
	 */
	private Log log = LogFactory.getLog(getClass());
	@Autowired
	private SaleService saleService;

	/**
	 * 跳转到管理首页
	 * 
	 * @return 页面地址
	 */
	@RequestMapping(value = "saleDetail/index")
	public String index() {
		return "sale/index";
	}
	

	@RequestMapping(value = "saleDetail/saveOrUpdate")
	@ResponseBody
	public ResultVO saveOrUpdate(SaleDetail saleDetail) {
		ResultVO resultVO = null;
		try {
			if (saleDetail != null && saleDetail.getId().isEmpty()) {
				saleDetail.setStatus(1);
			}
			saleService.saveOrUpdate(saleDetail);
			resultVO = new ResultVO(saleDetail);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultVO.setResult(ResultConstant.RESULT_FAIL);
			resultVO.setMessage(e.getMessage());
		}
		return resultVO;
	}
	@RequestMapping(value = "saleDetail/update")
	@ResponseBody
	public ResultVO update(String id,String url) {
		ResultVO resultVO = new ResultVO();
		try {
			saleService.updateSaleDetail(id,url);
			resultVO.setResult(ResultCode.SUCCESS);
			resultVO.setMessage(ResultMessage.SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultVO.setResult(ResultConstant.RESULT_FAIL);
			resultVO.setMessage(e.getMessage());
		}
		return resultVO;
	}
	@RequestMapping(value = "saleDetail/errorSaleUrl")
	@ResponseBody
	public ResultVO errorSaleUrl(String id) {
		ResultVO resultVO = new ResultVO();
		try {
			saleService.updateErrorSaleUrl(id);
			resultVO.setResult(ResultCode.SUCCESS);
			resultVO.setMessage(ResultMessage.SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultVO.setResult(ResultConstant.RESULT_FAIL);
			resultVO.setMessage(e.getMessage());
		}
		return resultVO;
	}




	/**
	 * 查询
	 * @return 返回给客户端的结果
	 */
	@RequestMapping(value = "saleDetail/querySaleDetail")
	@ResponseBody
	public ResultVO queryDictionary(PageRequest pageRequest,
			@RequestParam(value = "id", required = false) String id) {
		ResultVO resultVO = null;
		List<SaleDetail> saleDetails=null;
		try {
			//如果不是搜索，需要异步加载，只查询当级节点
			if(pageRequest==null){
				pageRequest=new PageRequest();
				pageRequest.setOrderBy("sort,name");
				pageRequest.setOrderDir("asc,asc");
			}else if(pageRequest.getOrderBy()==null||pageRequest.getOrderBy().isEmpty()){
				pageRequest.setOrderBy("sort,name");
				pageRequest.setOrderDir("asc,asc");
			}
			saleDetails=(List<SaleDetail>) saleService.queryDictionaryByPage(pageRequest);
			resultVO = new ResultVO(saleDetails);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultVO.setResult(ResultConstant.RESULT_FAIL);
			resultVO.setMessage(e.getMessage());
		}
		return resultVO;
	}
	/**
	 * 查询
	 * @return 返回给客户端的结果
	 */
	@RequestMapping(value = "saleDetail/querySaleDetailList")
	@ResponseBody
	public Map<String, Object> querySaleDetailList(PageRequest pageRequest,
			@RequestParam(value = "id", required = false) String id) {
		Map<String, Object> result = new HashMap<String, Object>(2);
		try {
			Map map = saleService.getSaleDetailList(pageRequest);
			if (map != null) {
				// 查询总条数
				    result.put("total", map.get("listSize"));
				// 查询结果集
				    result.put("rows", map.get("list"));
				    return result;
				}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
