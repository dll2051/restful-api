package com.tuotuogroup.service;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.tuotuogroup.entity.SaleDetail;

@Path("/searchservice")
@Produces({ "application/json", "application/xml" })
public interface RestfulSearchService {

	List<SaleDetail> queryAllSaleDetailData(String mallname, String source);

}
