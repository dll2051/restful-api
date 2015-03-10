package com.tuotuogroup.service;

import com.tuotuogroup.core.hibernate.Page;
import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.entity.SaleDetail;

public interface SaleService {
	public void saveOrUpdate(SaleDetail saledetail);
	public Page<SaleDetail> queryDictionaryByPage(PageRequest pageRequest);
	
}
