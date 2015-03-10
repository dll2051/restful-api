package com.tuotuogroup.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.core.web.ResultConstant;
import com.tuotuogroup.core.web.ResultVO;
import com.tuotuogroup.entity.SaleDetail;
import com.tuotuogroup.service.SaleService;

@Controller
public class SaleController {
	public SaleController() {
		System.out.println("===============SaleController");
	}
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
		return "dictionary/index";
	}

	@RequestMapping(value = "saleDetail/saveOrUpdate")
	@ResponseBody
	public ResultVO saveOrUpdate(SaleDetail saleDetail) {
		ResultVO resultVO = null;
		try {
			if (saleDetail != null && saleDetail.getId().isEmpty()) {
				saleDetail.setId(null);
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
}
