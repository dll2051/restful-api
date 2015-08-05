package com.tuotuogroup.dao.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.tuotuogroup.core.hibernate.BaseHibernateDao;
import com.tuotuogroup.core.hibernate.Page;
import com.tuotuogroup.core.hibernate.PageRequest;
import com.tuotuogroup.core.hibernate.PageRequest.Sort;
import com.tuotuogroup.dao.TaskDao;
import com.tuotuogroup.entity.SaleDetail;
import com.tuotuogroup.model.QuerySaleDetailParamsVO;

@Repository(value = "taskDao")
public class TaskDaoImpl extends BaseHibernateDao<SaleDetail, String> implements
		TaskDao {
	/**
	 * 设置SessionFactory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 通过传入的集合删除字典
	 * 
	 * @param ids
	 *            id集合
	 */
	public void deleteByIdList(List<String> ids) {
		StringBuilder hql = new StringBuilder("");
		hql.append("DELETE FROM Dictionary ");
		hql.append("WHERE id IN (:ids)");
		sessionFactory.getCurrentSession().createQuery(hql.toString()).setParameterList("ids", ids)
				.executeUpdate();
	}

	/**
	 * 停用/启用字典
	 * 
	 * @param ids
	 *            id集合
	 * @param active
	 *            启用状态
	 */
	public void changeActiveStatus(List<String> ids, Integer active) {
		StringBuilder hql = new StringBuilder("");
		hql.append("UPDATE Dictionary SET active=:active ");
		hql.append("WHERE id IN (:ids)");
		sessionFactory.getCurrentSession().createQuery(hql.toString()).setInteger("active", active)
				.setParameterList("ids", ids).executeUpdate();
	}

	/**
	 * 获得字典信息
	 * 
	 * @param id
	 *            ID
	 * @return
	 */
	public Dictionary getDictionary(String id) {
		StringBuilder hql = new StringBuilder("");
		hql.append("FROM Dictionary ");
		hql.append("WHERE id= :id");
		Object result = sessionFactory.getCurrentSession().createQuery(hql.toString())
				.setString("id", id).uniqueResult();
		return result == null ? null : (Dictionary) result;
	}

	/**
	 * 获得字典信息
	 * 
	 * @param code
	 *            字典编码
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Dictionary getDictionaryByCode(String code) {
		StringBuilder hql = new StringBuilder("");
		hql.append("FROM Dictionary ");
		hql.append("WHERE lower(code)= :code");
		List resultList = sessionFactory.getCurrentSession().createQuery(hql.toString())
				.setString("code", code.toLowerCase()).setMaxResults(1).list();
		Object result = (resultList == null || resultList.isEmpty()) ? null : resultList.get(0);
		return result == null ? null : (Dictionary) result;
	}

	/**
	 * 分页查询字典
	 * 
	 * @param pageRequest
	 *            分页参数
	 * @param params
	 *            查询参数
	 * @return 数据
	 */
	public Page<SaleDetail> queryDictionaryByPage(PageRequest pageRequest,
			QuerySaleDetailParamsVO params) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Dictionary.class);
		// 查询条件，根据需要添加
		if (params != null) {
			if (params.getKeyword() != null && !params.getKeyword().isEmpty()) {
				criteria.add(Restrictions.or(
						Restrictions.ilike("name", "%" + params.getKeyword().trim() + "%"),
						Restrictions.ilike("code", "%" + params.getKeyword().trim() + "%"),
						Restrictions.ilike("wubi", "%" + params.getKeyword().trim() + "%"),
						Restrictions.ilike("pinyin", "%" + params.getKeyword().trim() + "%")));
			}
			if (params.getActive() != null && params.getActive() != -1) {
				criteria.add(Restrictions.eq("active", params.getActive()));
			}
			if (params.getTypeCode() != null && !params.getTypeCode().isEmpty()) {
				criteria.add(Restrictions.eq("typeCode", params.getTypeCode()));
			}
			if (params.getParentId() != null && !params.getParentId().isEmpty()) {
				criteria.add(Restrictions.eq("parentId", params.getParentId()));
			}
		}
		return findPage(pageRequest, criteria);
	}

	/**
	 * 不分页查询字典
	 * 
	 * @param pageRequest
	 *            分页参数，用于排序
	 * 
	 * @param params
	 *            查询参数
	 * @return
	 */
	public List<SaleDetail> queryDictionaryTreeVO(PageRequest pageRequest,
			QuerySaleDetailParamsVO params) {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT ");
		sql.append("	I.id as  \"id\",");
		sql.append("	I.parent_id as \"parentId\", ");
		sql.append("	P.name as \"parentName\", ");
		sql.append("	I.name as \"text\", ");
		sql.append("	I.name as \"name\", ");
		sql.append("	I.code as \"code\", ");
		if (params != null) {
			if (params.getKeyword() != null && !params.getKeyword().isEmpty()) {
				sql.append(" 'open' as \"state\", ");
			}else if (params.getActive() != null && params.getActive() != -1){
				sql.append("	CASE WHEN EXISTS ( SELECT * FROM D_DICTIONARY t  WHERE t.parent_id = I.id AND t.active=:active) THEN 'closed' else 'open' END \"state\", ");
			}else{
				sql.append("	CASE WHEN EXISTS ( SELECT * FROM D_DICTIONARY t  WHERE t.parent_id = I.id ) THEN 'closed' else 'open' END \"state\", ");
			}
		} else {
			sql.append("	CASE WHEN EXISTS ( SELECT * FROM D_DICTIONARY t  WHERE t.parent_id = I.id ) THEN 'closed' else 'open' END \"state\", ");
		}
		sql.append("	I.TYPE_CODE as \"typeCode\", ");
		sql.append("	I.STANDARD_NO as \"standardNo\", ");
		sql.append("	I.USE_RANGE as \"useRange\", ");
		sql.append("	I.SOURCE as \"source\", ");
		sql.append("	I.pinyin as \"pinyin\", ");
		sql.append("	I.wubi as \"wubi\", ");
		sql.append("	I.active as \"active\", ");
		sql.append("	I.is_modify as \"isModify\", ");
		sql.append("	I.is_cache as \"isCache\", ");
		sql.append("	I.sort as \"sort\" ");
		sql.append("FROM D_DICTIONARY I ");
		sql.append("	LEFT JOIN D_DICTIONARY P ON P.id=I.parent_id ");
		if (params != null) {
			StringBuilder whereSql = new StringBuilder();
			if (params.getKeyword() != null && !params.getKeyword().isEmpty()) {
				whereSql.append("AND (");
				whereSql.append("	lower(I.name) like :keyword");
				whereSql.append("	OR lower(I.code) like :keyword");
				whereSql.append("	OR lower(I.wubi) like :keyword");
				whereSql.append("	OR lower(I.standard_no) like :keyword");
				whereSql.append("	OR lower(I.pinyin) like :keyword");
				whereSql.append("	) ");
				values.put("keyword", "%" + params.getKeyword().toLowerCase() + "%");
			}
			if (params.getActive() != null && params.getActive() != -1) {
				whereSql.append("AND I.active=:active ");
				values.put("active", params.getActive());
			}
			if (params.getParentId() != null && !params.getParentId().isEmpty()) {
				whereSql.append("AND I.parent_id=:parentId ");
				values.put("parentId", params.getParentId());
			}
			if (whereSql.length() > 0) {
				sql.append(" WHERE ").append(whereSql.delete(0, 3));
			}
		}
		List<Sort> sorts = pageRequest.getSort();
		if (sorts != null && !sorts.isEmpty()) {
			StringBuilder orderSql = new StringBuilder();
			orderSql.append(" ORDER BY ");
			for (Sort sort : sorts) {
				if ("standardNo".equals(sort.getProperty())) {
					orderSql.append(" I.").append("standard_no").append(" ").append(sort.getDir())
							.append(",");
				} else {
					orderSql.append(" I.").append(sort.getProperty()).append(" ")
							.append(sort.getDir()).append(",");
				}

			}
			orderSql.delete(orderSql.length() - 1, orderSql.length());
			if (orderSql.length() > 0) {
				sql.append(orderSql);
			}
		}
		// return findPageBySQL(DictionaryTreeDataVO.class,pageRequest,
		// sql.toString(), values);
		return findBySQL(SaleDetail.class, sql.toString(), values);
	}

	/**
	 * 通过字典编码查找字典数量
	 * 
	 * @param code
	 *            字典编码
	 * @return 数量
	 */
	public Integer queryDictionaryCount(String code) {
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT ");
		sql.append("	COUNT(id) ");
		sql.append("FROM D_DICTIONARY ");
		sql.append("WHERE code= :code");
		return ((BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(sql.toString())
				.setString("code", code).uniqueResult()).intValue();
	}
	
	/**
	 * 根据状态查询字典信息集合.
	 * 
	 * @param active
	 *            状态.
	 * @return VO集合
	 * @throws SQLException SQL异常
	 */
	public List<SaleDetail> queryByActive(Integer active){
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT ");
		sql.append("	I.id as \"id\",");
		sql.append("	I.parent_id as \"parentId\", ");
		sql.append("	P.name as \"parentName\", ");
		sql.append("	I.name as \"name\", ");
		sql.append("	I.code as \"code\", ");
		sql.append("	I.pinyin as \"pinyin\", ");
		sql.append("	I.wubi as \"wubi\", ");
		sql.append("	I.active as \"active\", ");
		sql.append("	I.sort as \"sort\", ");
		sql.append("	I.type_code as \"typeCode\", ");
		sql.append("	I.standard_no as \"standardNo\", ");
		sql.append("	I.use_range as \"useRange\", ");
		sql.append("	I.source as \"source\", ");
		sql.append("	I.is_modify as \"isModify\", ");
		sql.append("	I.is_cache as \"isCache\", ");
		sql.append("	I.comments as \"comments\" ");
		sql.append("FROM ").append("D_DICTIONARY I ");
		sql.append("	LEFT JOIN ").append("D_DICTIONARY P ON P.id=I.parent_id ");
		sql.append("WHERE I.active= :active");
		values.put("active", active);
		return findBySQL(SaleDetail.class, sql.toString(), values);
	}
}
