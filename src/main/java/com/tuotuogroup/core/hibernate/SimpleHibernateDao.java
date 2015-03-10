package com.tuotuogroup.core.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tuotuogroup.core.utils.Reflections;

/**
 * 封装Hibernate原生API的DAO泛型基类.
 * 
 * 参考Spring2.5自带的Petlinc例子, 取消了HibernateTemplate, 直接使用Hibernate原生API.
 * 
 * @param <T>
 *            DAO操作的对象类型
 * @param <ID>
 *            主键类型
 * 
 */
public class SimpleHibernateDao<T, ID extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected Class<T> entityClass;

	@Autowired
	protected SessionFactory sessionFactory;

	protected Session getSession() {
		try{
			return sessionFactory.getCurrentSession();
		}catch(Exception e){
			return sessionFactory.openSession();
		}
	}
	/**
	 * 用于Dao层子类使用的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends
	 * SimpleHibernateDao<User, Long>
	 */
	public SimpleHibernateDao() {
		this.entityClass = Reflections.getClassGenricType(getClass());
	}

	public SimpleHibernateDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * 保存新增或修改的对象.
	 */
	public void save(final T entity) {
		Validate.notNull(entity, "entity不能为空");
		sessionFactory.getCurrentSession().save(entity);
		logger.debug("save entity: {}", entity);
	}

	public void saveOrUpdate(final T entity) {
		Validate.notNull(entity, "entity不能为空");
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
		logger.debug("save entity: {}", entity);
	}

	/**
	 * 删除对象.
	 * 
	 * @param entity
	 *            对象必须是session中的对象或含id属性的transient对象.
	 */
	public void delete(final T entity) {
		Validate.notNull(entity, "entity不能为空");
		sessionFactory.getCurrentSession().delete(entity);
		logger.debug("delete entity: {}", entity);
	}

	/**
	 * 按id删除对象.
	 */
	public void delete(final ID id) {
		Validate.notNull(id, "id不能为空");
		delete(get(id));
		logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
	}

	/**
	 * 按id获取对象.
	 */
	public T get(final ID id) {
		Validate.notNull(id, "id不能为空");
		return (T) sessionFactory.getCurrentSession().load(entityClass, id);
	}

	/**
	 * 按id列表获取对象列表.
	 */
	public List<T> get(final Collection<ID> ids) {
		return find(Restrictions.in(getIdName(), ids));
	}

	/**
	 * 获取全部对象.
	 */
	public List<T> getAll() {
		return find();
	}

	/**
	 * 获取全部对象, 支持按属性行序.
	 */
	public List<T> getAll(String orderByProperty, boolean isAsc) {
		Criteria c = createCriteria();
		if (isAsc) {
			c.addOrder(Order.asc(orderByProperty));
		} else {
			c.addOrder(Order.desc(orderByProperty));
		}
		return c.list();
	}

	/**
	 * 按属性查找对象列表, 匹配方式为相等.
	 */
	public List<T> findBy(final String propertyName, final Object value) {
		Validate.notBlank(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return find(criterion);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	public T findUniqueBy(final String propertyName, final Object value) {
		Validate.notBlank(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(criterion).uniqueResult();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <X> List<X> find(final String hql, final Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <X> List<X> find(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).list();
	}

	public List findBySQL(String sql, Object... values) {
		Validate.notBlank(sql);
		SQLQuery query = createSQLQuery(sql, null, null, values);
		return query.list();
	}

	public List findBySQL(String sql, String[] field, Type[] fieldtype, Object[] values) {
		Validate.notBlank(sql);
		SQLQuery query = createSQLQuery(sql, field, fieldtype, values);
		return query.list();
	}

	public <T> List<T> findBySQL(Class<T> entityClass, String sql, final Object... values) {
		Validate.notBlank(sql);
		SQLQuery query = createSQLQuery(sql, null, null, values).addEntity(entityClass);
		return query.list();
	}

	/**
	 * 根据sql查询并转化为VO
	 * 
	 * @param voClass
	 * @param sql
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findBySQL(Class<X> voClass, String sql, final Map<String, ?> values) {
		Validate.notBlank(sql);
		return createSQLQuery(sql, null, null, null)
				.setResultTransformer(Transformers.aliasToBean(voClass))
				.setProperties(values).list();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <X> X findUnique(final String hql, final Object... values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Object... values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(final String queryString, final Object... values) {
		Validate.notBlank(queryString, "queryString不能为空");
		Query query = sessionFactory.getCurrentSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public Query createQuery(final String queryString, final Map<String, ?> values) {
		Validate.notBlank(queryString, "queryString不能为空");
		Query query = sessionFactory.getCurrentSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	public SQLQuery createSQLQuery(String queryString, String[] field, Type[] fieldtype,
			Object[] values) {
		Validate.notBlank(queryString, "queryString不能为空", new Object[0]);
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}

		if (field != null) {
			for (int i = 0; i < field.length; i++) {
				query.addScalar(field[i], fieldtype[i]);
			}
		}
		return query;
	}

	/**
	 * 按Criteria查询对象列表.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public List<T> find(final Criterion... criterions) {
		return createCriteria(criterions).list();
	}

	public List<T> find(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(sessionFactory
				.getCurrentSession());
		return criteria.list();
	}

	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public T findUnique(final Criterion... criterions) {
		return (T) createCriteria(criterions).uniqueResult();
	}

	/**
	 * 根据Criterion条件创建Criteria. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 初始化对象. 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化. 如果传入entity,
	 * 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性. 如需初始化关联属性,需执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
	 * Hibernate.initialize
	 * (user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initProxyObject(Object proxy) {
		Hibernate.initialize(proxy);
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * 为Query添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	/**
	 * 为Criteria添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Criteria distinct(Criteria criteria) {
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	/**
	 * 取得对象的主键名.
	 */
	public String getIdName() {
		ClassMetadata meta = sessionFactory.getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
	 */
	public boolean isPropertyUnique(final String propertyName, final Object newValue,
			final Object oldValue) {
		if (newValue == null || newValue.equals(oldValue)) {
			return true;
		}
		Object object = findUniqueBy(propertyName, newValue);
		return (object == null);
	}

	public int batchExecSQL(String sql, Object... values) {
		Validate.notBlank(sql);
		SQLQuery query = createSQLQuery(sql, null, null, values);
		return query.executeUpdate();
	}

}