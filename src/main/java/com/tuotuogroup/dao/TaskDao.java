package com.tuotuogroup.dao;

import org.hibernate.SessionFactory;

import com.tuotuogroup.core.hibernate.IBaseHibernateDao;
import com.tuotuogroup.entity.SaleDetail;

public interface TaskDao extends IBaseHibernateDao<SaleDetail, String> {
	
	/**
	 * 设置SessionFactory
	 * @param sessFactory
	 */
	public void setSessionFactory(SessionFactory sessFactory);
	
}
