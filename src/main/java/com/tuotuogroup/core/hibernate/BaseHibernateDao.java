package com.tuotuogroup.core.hibernate;

import com.tuotuogroup.core.hibernate.PageRequest.Sort;
import com.tuotuogroup.core.hibernate.PropertyFilter.MatchType;
import com.tuotuogroup.core.utils.Reflections;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.springframework.util.Assert;

/**
 * 封装Hibernate原生API的DAO泛型基类.
 * 
 * 可在Service层直接使用, 也可以扩展泛型DAO子类使用, 见两个构造函数的注释. 参考Spring2.5自带的Petlinc例子,
 * 取消了HibernateTemplate, 直接使用Hibernate原生API.
 * 
 * @param <T>
 *            DAO操作的对象类型
 * @param <PK>
 *            主键类型
 * 
 */
@SuppressWarnings("unchecked")
public class BaseHibernateDao<T, PK extends Serializable> extends
		SimpleHibernateDao<T, PK> implements IBaseHibernateDao<T, PK> {

	public static final String DEFAULT_ALIAS = "x";

	/**
	 * 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends HibernateDao<User,
	 * Long>{ }
	 */
	public BaseHibernateDao() {
		super();
	}

	public BaseHibernateDao(Class<T> entityClass) {
		super(entityClass);
	}

	// -- 分页查询函数 --//

	/**
	 * 分页获取全部对象.
	 */
	public Page<T> getAll(final PageRequest pageRequest) {
		return findPage(pageRequest);
	}

	public Page<T> findPageBySQL(final PageRequest pageRequest, String sql,
			Object... values) {
		Assert.notNull(pageRequest, "pageRequest不能为空");

		Page<T> page = new Page<T>(pageRequest);

		String countSql = prepareCountHql(sql);
		List countlist = createSQLQuery(countSql, null, null, values).list();

		Query q = createSQLQuery(sql, null, null, values);

		long totalCount = 0L;
		if (countlist.size() > 0) {
			totalCount = ((BigDecimal) countlist.get(0)).longValue();
		}

		setPageParameterToQuery(q, pageRequest);

		List result = q.list();
		page.setRows(result);
		page.setTotal(totalCount);
		return page;
	}

	public Page<T> findPageBySQLToMap(final PageRequest pageRequest,
			String sql, Object... values) {
		Assert.notNull(pageRequest, "pageRequest不能为空");

		Page<T> page = new Page<T>(pageRequest);

		String countSql = prepareCountHql(sql);
		List countlist = createSQLQuery(countSql, null, null, values).list();

		Query q = createSQLQuery(sql, null, null, values);

		long totalCount = 0L;
		if (countlist.size() > 0) {
			totalCount = ((BigDecimal) countlist.get(0)).longValue();
		}
		q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		setPageParameterToQuery(q, pageRequest);

		List result = q.list();
		page.setRows(result);
		page.setTotal(totalCount);
		return page;
	}

	/**
	 * 根据sql查询并转化为VO
	 * 
	 * @param voClass
	 * @param sql
	 * @param values
	 * @return
	 * @author hutao
	 */
	@SuppressWarnings({ "rawtypes" })
	public <X> Page<X> findPageBySQL(Class<X> voClass,
			final PageRequest pageRequest, String sql,
			final Map<String, Type> fieldsTypesMap, final Map<String, ?> values) {
		Validate.notBlank(sql);
		Page<X> page = new Page<X>(pageRequest);

		String countSql = prepareCountHql(sql);
		List countlist = createSQLQuery(countSql, null, null, null)
				.setProperties(values).list();

		SQLQuery q = createSQLQuery(sql, null, null, null);

		long totalCount = 0L;
		if (countlist.size() > 0) {
			totalCount = ((BigDecimal) countlist.get(0)).longValue();
		}
		q.setResultTransformer(Transformers.aliasToBean(voClass));
		setPageParameterToQuery(q, pageRequest);
		if (fieldsTypesMap != null) {
			for (String key : fieldsTypesMap.keySet()) {
				q.addScalar(key, fieldsTypesMap.get(key));
			}
		}
		List result = q.setProperties(values).list();
		page.setRows(result);
		page.setTotal(totalCount);
		return page;
	}

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
			final Object... values) {
		Validate.notNull(pageRequest, "pageRequest不能为空");

		Page<T> page = new Page<T>(pageRequest);

		if (pageRequest.isCountTotal()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotal(totalCount);
		}

		if (pageRequest.isOrderBySetted()) {
			hql = setOrderParameterToHql(hql, pageRequest);
		}
		Query q = createQuery(hql, values);

		setPageParameterToQuery(q, pageRequest);

		List result = q.list();
		page.setRows(result);
		return page;
	}

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
			final Map<String, ?> values) {
		Validate.notNull(pageRequest, "page不能为空");

		Page<T> page = new Page<T>(pageRequest);

		if (pageRequest.isCountTotal()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotal(totalCount);
		}

		if (pageRequest.isOrderBySetted()) {
			hql = setOrderParameterToHql(hql, pageRequest);
		}

		Query q = createQuery(hql, values);
		setPageParameterToQuery(q, pageRequest);

		List result = q.list();
		page.setRows(result);
		return page;
	}

	/**
	 * 按Criteria分页查询.
	 * 
	 * @param page
	 *            分页参数.
	 * @param criterions
	 *            数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final PageRequest pageRequest,
			final Criterion... criterions) {
		Validate.notNull(pageRequest, "page不能为空");

		Page<T> page = new Page<T>(pageRequest);

		Criteria c = createCriteria(criterions);

		if (pageRequest.isCountTotal()) {
			long totalCount = countCriteriaResult(c);
			page.setTotal(totalCount);
		}

		setPageRequestToCriteria(c, pageRequest);

		List result = c.list();
		page.setRows(result);
		return page;
	}

	public Page<T> findPage(final PageRequest pageRequest,
			final Criteria criteria) {
		Assert.notNull(pageRequest, "page不能为空");

		Page<T> page = new Page<T>(pageRequest);

		if (pageRequest.isCountTotal()) {
			long totalCount = countCriteriaResult(criteria);
			page.setTotal(totalCount);
		}

		setPageRequestToCriteria(criteria, page);

		List result = criteria.list();
		page.setRows(result);
		return page;
	}

	/**
	 * 在HQL的后面添加分页参数定义的orderBy, 辅助函数.
	 */
	protected String setOrderParameterToHql(final String hql,
			final PageRequest pageRequest) {
		StringBuilder builder = new StringBuilder(hql);
		builder.append(" order by");

		for (Sort orderBy : pageRequest.getSort()) {
			builder.append(String.format(" %s.%s %s,", DEFAULT_ALIAS,
					orderBy.getProperty(), orderBy.getDir()));
		}

		builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}

	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected Query setPageParameterToQuery(final Query q,
			final PageRequest pageRequest) {
		q.setFirstResult(pageRequest.getOffset());
		q.setMaxResults(pageRequest.getPageSize());
		return q;
	}

	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageRequestToCriteria(final Criteria c,
			final PageRequest pageRequest) {
		Validate.isTrue(pageRequest.getPageSize() > 0,
				"Page Size must larger than zero");

		c.setFirstResult(pageRequest.getOffset());
		c.setMaxResults(pageRequest.getPageSize());

		if (pageRequest.isOrderBySetted()) {
			for (Sort sort : pageRequest.getSort()) {
				if (Sort.ASC.equals(sort.getDir())) {
					c.addOrder(Order.asc(sort.getProperty()));
				} else {
					c.addOrder(Order.desc(sort.getProperty()));
				}
			}
		}
		return c;
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:"
					+ countHql, e);
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:"
					+ countHql, e);
		}
	}

	protected String prepareCountHql(String orgHql) {
		String countHql = "select count (*) "
				+ removeSelect(removeOrders(orgHql));
		return countHql;
	}

	/**
	 * 
	 * @param hql
	 * @return 修改记录： 2014-10-21 将indexOf改为lastIndexOf 修改人：hutao
	 */
	protected static String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().lastIndexOf("from");
		return hql.substring(beginPos);
	}

	protected static String removeOrders(String hql) {
		Pattern p = Pattern.compile(" order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, " ");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	protected long countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) Reflections.getFieldValue(impl,
					"orderEntries");
			Reflections.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();
		long totalCount = (totalCountObject != null) ? totalCountObject : 0;

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			Reflections.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}

	// -- 属性过滤条件(PropertyFilter)查询函数 --//

	/**
	 * 按属性查找对象列表,支持多种匹配方式.
	 * 
	 * @param matchType
	 *            匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
	 */
	public List<T> findBy(final String propertyName, final Object value,
			final MatchType matchType) {
		Criterion criterion = buildCriterion(propertyName, value, matchType);
		return find(criterion);
	}

	/**
	 * 按属性过滤条件列表查找对象列表.
	 */
	public List<T> find(List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return find(criterions);
	}

	/**
	 * 按属性过滤条件列表分页查找对象.
	 */
	public Page<T> findPage(final PageRequest pageRequest,
			final List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPage(pageRequest, criterions);
	}

	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 */
	protected Criterion buildCriterion(final String propertyName,
			final Object propertyValue, final MatchType matchType) {
		Validate.notBlank(propertyName, "propertyName can't be null");
		Criterion criterion = null;
		// 根据MatchType构造criterion
		switch (matchType) {
		case EQ:
			criterion = Restrictions.eq(propertyName, propertyValue);
			break;
		case LIKE:
			criterion = Restrictions.like(propertyName, (String) propertyValue,
					MatchMode.ANYWHERE);
			break;

		case LE:
			criterion = Restrictions.le(propertyName, propertyValue);
			break;
		case LT:
			criterion = Restrictions.lt(propertyName, propertyValue);
			break;
		case GE:
			criterion = Restrictions.ge(propertyName, propertyValue);
			break;
		case GT:
			criterion = Restrictions.gt(propertyName, propertyValue);
		}
		return criterion;
	}

	/**
	 * 按属性条件列表创建Criterion数组,辅助函数.
	 */
	public Criterion[] buildCriterionByPropertyFilter(
			final List<PropertyFilter> filters) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		for (PropertyFilter filter : filters) {
			if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
				Criterion criterion = buildCriterion(filter.getPropertyName(),
						filter.getMatchValue(), filter.getMatchType());
				criterionList.add(criterion);
			} else {// 包含多个属性需要比较的情况,进行or处理.
				Disjunction disjunction = Restrictions.disjunction();
				for (String param : filter.getPropertyNames()) {
					Criterion criterion = buildCriterion(param,
							filter.getMatchValue(), filter.getMatchType());
					disjunction.add(criterion);
				}
				criterionList.add(disjunction);
			}
		}
		return criterionList.toArray(new Criterion[criterionList.size()]);
	}

	public void delete(Object[][] where) {
		delete(this.entityClass, where);
	}

	public void delete(Class<T> entityType, Object[][] where) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				new StringBuilder().append("delete from ")
						.append(entityType.getName()).toString()).append(" a");

		Object[] values = (Object[]) null;
		if (where != null) {
			sb.append(" where ");
			values = new Object[where.length];

			int i = 0;
			int len = where.length;
			for (Object[] key : where) {
				sb.append(new StringBuilder().append("a.").append(key[0])
						.append("=?").toString());
				if (i < len - 1) {
					sb.append(" AND ");
				}

				values[(i++)] = key[1];
			}
		}

		batchExecute(sb.toString(), values);
	}

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void main(String[] args) {
		BaseHibernateDao
				.removeSelect("select 1,exists (select 1 from t where 1=1 and 1 order by id asc) from app_user where not exists (select 1 from t2)");
	}
}