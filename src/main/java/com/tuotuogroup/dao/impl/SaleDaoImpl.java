package com.tuotuogroup.dao.impl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.tuotuogroup.core.hibernate.BaseHibernateDao;
import com.tuotuogroup.core.hibernate.Page;
import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.core.pagination.DataGridModel;
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
	/**
	 * 
	 */
	@Override
	public Map getSaleDetailList(DataGridModel dgm) {
		String sql = "select * from tb_saledetail";
		Query query = getSession().createSQLQuery(sql).
		setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Map map = new HashMap();
		map.put("listSize", query.list().size());
		if (dgm != null) {
			query.setFirstResult((dgm.getPagination().getPage() - 1)
					* dgm.getPagination().getRows());
			query.setMaxResults(dgm.getPagination().getRows());
		}
		map.put("list", query.list());
		return map;
	}


}
