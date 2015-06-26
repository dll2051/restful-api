package com.tuotuogroup.dao;

import java.util.List;
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

	/**
	 * @param mallname
	 * @param source
	 */
	public List<SaleDetail> queryAllSaleDetailData(String mallname, String source);

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
