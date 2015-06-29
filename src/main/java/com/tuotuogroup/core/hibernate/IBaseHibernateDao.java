/**
 * 
 */
package com.tuotuogroup.core.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import com.tuotuogroup.core.hibernate.PropertyFilter.MatchType;

public interface IBaseHibernateDao<T, PK extends Serializable> extends
		ISimpleHibernateDao<T, PK> {
	// -- 分页查询函数 --//

	/**
	 * 分页获取全部对象.
	 */
	public Page<T> getAll(final PageRequest pageRequest);

	/**
	 * 通过SQL分页获取对象
	 * 
	 * @param pageRequest
	 *            分页参数
	 * @param sql
	 *            SQL
	 * @param values
	 *            查询参数
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPageBySQL(final PageRequest pageRequest, String sql,
			Object... values);

	/**
	 * 按HQL分页查询.
	 * 
	 * @param pageRequest
	 *            分页参数.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            数量可变的查询参数,按顺序绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final PageRequest pageRequest, String hql,
			final Object... values);

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page
	 *            分页参数.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final PageRequest pageRequest, String hql,
			final Map<String, ?> values);

	/**
	 * 按Criteria分页查询.
	 * 
	 * @param pageRequest
	 *            分页参数.
	 * @param criterions
	 *            数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final PageRequest pageRequest,
			final Criterion... criterions);

	/**
	 * 按Criteria分页查询.
	 * 
	 * @param pageRequest
	 *            分页参数
	 * @param criteria
	 *            Criterion
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final PageRequest pageRequest,
			final Criteria criteria);

	// -- 属性过滤条件(PropertyFilter)查询函数 --//

	/**
	 * 按属性查找对象列表,支持多种匹配方式.
	 * 
	 * @param matchType
	 *            匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
	 */
	public List<T> findBy(final String propertyName, final Object value,
			final MatchType matchType);

	/**
	 * 按属性过滤条件列表查找对象列表.
	 */
	public List<T> find(List<PropertyFilter> filters);

	/**
	 * 按属性过滤条件列表分页查找对象.
	 */
	public Page<T> findPage(final PageRequest pageRequest,
			final List<PropertyFilter> filters);

	/**
	 * 按属性条件列表创建Criterion数组,辅助函数.
	 */
	public Criterion[] buildCriterionByPropertyFilter(
			final List<PropertyFilter> filters);

	/**
	 * 按照传入的where条件数组删除对象
	 * 
	 * @param where
	 */
	public void delete(Object[][] where);

	/**
	 * 按照传入的where条件数组和实体类型删除对象
	 * 
	 * @param entityType
	 * @param where
	 */
	public void delete(Class<T> entityType, Object[][] where);

	public SessionFactory getSessionFactory();
}
