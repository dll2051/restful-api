package com.tuotuogroup.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tuotuogroup.dao.SaleDao;
import com.tuotuogroup.entity.SaleDetail;
import com.tuotuogroup.service.RestfulSearchService;

@Service(value="saleDetailSearchService")
public class RestfulSearchServiceImpl implements RestfulSearchService {

	@Resource(name = "saleDao")
	private SaleDao saleDao;

	@Override
	public List<SaleDetail> queryAllSaleDetailData(String mallname,
			String source) {
		return saleDao.queryAllSaleDetailData(mallname,source);
	}
}
