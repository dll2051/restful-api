package com.tuotuogroup.dao;

import java.util.Map;

import org.hibernate.SessionFactory;

import com.tuotuogroup.core.hibernate.IBaseHibernateDao;
import com.tuotuogroup.core.hibernate.Page;
import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.core.pagination.DataGridModel;
import com.tuotuogroup.entity.SaleDetail;

public interface SaleDao extends IBaseHibernateDao<SaleDetail, String> {
	
	/**
	 * 设置SessionFactory
	 * @param sessFactory
	 */
	public void setSessionFactory(SessionFactory sessFactory);
	
	public Page<SaleDetail> queryDictionaryByPage(PageRequest pageRequest);

	/**
	 * @return
	 */
	public Map getSaleDetailList(DataGridModel dgm);
}
