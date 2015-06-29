package com.tuotuogroup.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tuotuogroup.core.hibernate.Page;
import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.dao.SaleDao;
import com.tuotuogroup.entity.SaleDetail;
import com.tuotuogroup.service.SaleService;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {
	
	/**
	 * 日志工具
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource(name="saleDao")
	private SaleDao saleDao;

	public void saveOrUpdate(SaleDetail saledetail) {
		saleDao.saveOrUpdate(saledetail);
	}


	public Page<SaleDetail> queryDictionaryByPage(PageRequest pageRequest) {
		return saleDao.queryDictionaryByPage(pageRequest);
	}


	/**
	 * 
	 */
	@Override
	public Map getSaleDetailList(PageRequest pageRequest) {
		return saleDao.getSaleDetailList(pageRequest);
	}


	/**
	 * 
	 */
	@Override
	public SaleDetail getOneSaleDetail() {
		return saleDao.getOneSaleDetail();
	}


	/**
	 * 
	 */
	@Override
	public SaleDetail getSaleDetailByID(String id) {
		return saleDao.getSaleDetailByID(id);
	}


	/**
	 * 
	 */
	@Override
	public void updateSaleDetail(String id, String url) {
		saleDao.updateSaleDetail(id,url);
	}


	/**
	 * 
	 */
	@Override
	public void updateErrorSaleUrl(String id) {
		saleDao.updateErrorSaleUrl(id);
	}
	

}
