package com.tuotuogroup.service;

import java.util.Map;

import com.tuotuogroup.core.hibernate.Page;
import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.entity.SaleDetail;

public interface SaleService {
	public void saveOrUpdate(SaleDetail saledetail);
	public Page<SaleDetail> queryDictionaryByPage(PageRequest pageRequest);
	/**
	 * @return
	 */
	public Map getSaleDetailList(PageRequest pageRequest);
	/**
	 * @return
	 */
	public SaleDetail getOneSaleDetail();
	/**
	 * @param id
	 * @return
	 */
	public SaleDetail getSaleDetailByID(String id);
	/**
	 * @param id
	 * @param url
	 */
	public void updateSaleDetail(String id, String url);
	/**
	 * @param id
	 */
	public void updateErrorSaleUrl(String id);
	
}
