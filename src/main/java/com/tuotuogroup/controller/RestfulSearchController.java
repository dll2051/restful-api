package com.tuotuogroup.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tuotuogroup.core.constant.ResultCode;
import com.tuotuogroup.core.constant.ResultMessage;
import com.tuotuogroup.core.web.ResultVO;
import com.tuotuogroup.entity.SaleDetail;
import com.tuotuogroup.service.RestfulSearchService;

@Controller(value = "restfulSearchController")
@Path("/searchservice/")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RestfulSearchController extends BaseRestfulServiceController {

	/**
	 * 日志
	 */
	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private RestfulSearchService restfulSearchService;

	@RequestMapping(value = "saleDetail/queryAllSaleDetail")
	@ResponseBody
	@GET
	@Path(value = "/saleDetailVo/{mallname}/all")
	public ResultVO<List<SaleDetail>> queryItemTreeListByCode(
			@PathParam(value = "mallname") String mallname,
			@QueryParam(value = "source") String source) {
		List<SaleDetail> voList = new ArrayList<SaleDetail>();
		try {
			voList = restfulSearchService.queryAllSaleDetailData(
					mallname, source);
			return new ResultVO<List<SaleDetail>>(ResultCode.SUCCESS, ResultMessage.SUCCESS,
					voList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResultVO<List<SaleDetail>>(ResultCode.FAIL, e.getMessage(), null);

		}
	}


}
