package com.tuotuogroup.dao.impl;

import java.util.Dictionary;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tuotuogroup.core.hibernate.BaseHibernateDao;
import com.tuotuogroup.core.hibernate.Page;
import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.dao.SaleDao;
import com.tuotuogroup.entity.SaleDetail;

@Repository(value = "saleDao")
public class SaleDaoImpl extends BaseHibernateDao<SaleDetail, String> implements
		SaleDao {
	/**
	 * 设置SessionFactory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public Page<SaleDetail> queryDictionaryByPage(PageRequest pageRequest) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Dictionary.class);
		return findPage(pageRequest, criteria);
	}


}
