package cn.lzs.share.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import cn.lzs.share.common.util.Finder;

public class BaseDao<T extends Serializable> extends HibernateDaoSupport  {
	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 泛型类型class
	 */
	private Class<T> entityClass;
	
	public enum OrderBy{
		desc , asc
	}
	
	@Resource(name = "sessionFactory")
	public void setFactory(SessionFactory factory) {
		this.setSessionFactory(factory);
	}

	@SuppressWarnings("unchecked")
	public BaseDao(){
		//对泛型进行具体的赋值
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
    
    public Class<T> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * 保存实体
	 */
	public T save(T entity) {
		Assert.notNull(entity);
		getSession().save(entity);
		return entity;
	}

	/**
	 * 更新实体
	 */
	public Object update(Object entity) {
		Assert.notNull(entity);
		getSession().update(entity);
		return entity;
	}

	/**
	 * 更新或保存实体
	 */
	public Object saveOrUpdate(Object entity) {
		Assert.notNull(entity);
		getSession().saveOrUpdate(entity);
		return entity;
	}

	/**
	 * 更新实体
	 */
	public Object merge(Object entity) {
		Assert.notNull(entity);
		return getSession().merge(entity);
	}

	/**
	 * 删除实体
	 */
	public void delete(Object entity) {
		Assert.notNull(entity);
		getSession().delete(entity);
	}

	/**
	 * 根据ID删除实体
	 */
	public T deleteById( Serializable id) {
		Assert.notNull(id);
		T entity = load(id);
		getSession().delete(entity);
		return entity;
	}
	
	/**
	 * 批量删除
	 */
	public void deleteAll(List<T> entities){
		getHibernateTemplate().deleteAll(entities);
	}

	/**
	 * 加载实体，实体不存在就抛异常
	 */
	@SuppressWarnings("unchecked")
	public T load( Serializable id) {
		Assert.notNull(id);
		return (T) getSession().load(getEntityClass(), id);
	}

	/**
	 * 通过ID获取实体
	 */
	@SuppressWarnings("unchecked")
	public T get( Serializable id)throws Exception {
		T s=(T) getSession().get(getEntityClass() , id);
		if(s==null)
			throw new Exception("id="+id+" 的对象无法找到！");
		return s;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return createQuery("from " + entityClass.getSimpleName()).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(String hql, Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询唯一对象.
	 */
	public Object findUnique(String hql, Object... values) {
		return createQuery(hql, values).uniqueResult();
	}

	/**
	 * 获取分页<br />
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<T> pageFind(Finder finder) {
		int totalCount = countQueryResult(finder);
		List<T> list = null;
		if (totalCount < 1) {
			list = new ArrayList<T>();
			return list;
		}
		Query query = getSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		finder.setTotalSize(totalCount);
		query.setFirstResult(finder.getFirstResult());
		query.setMaxResults(finder.getPageSize());
		list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Finder finder) {
		Query query = getSession().createQuery(finder.getOrigHql());
		finder.setParamsToQuery(query);
		query.setFirstResult(finder.getFirstResult());
		if (finder.getPageSize() > 0) {
			query.setMaxResults(finder.getPageSize());
		}
		List<T> list = query.list();
		return list;
	}

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	public Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 通过count查询获得本次查询所能获得的对象总数.
	 * 
	 * @param finder
	 * @return
	 */
	public int countQueryResult(Finder finder) {
		Query query = getSession().createQuery(finder.getRowCountHql());
		finder.setParamsToQuery(query);
		return ((Number) query.iterate().next()).intValue();
	}

	public void refresh(Object entity) {
		getSession().refresh(entity);
	}
	
    /**
     * 执行Hsql语句
     * 
     * @param Hsql 查询语句
     * delete from 实体名  where id=5
     */
    public void executeHsql(String Hsql) throws Exception{
		Session session = null;
		try {
			session = (Session) getSession();
			session.createQuery(Hsql).executeUpdate();
		}
    	catch(DataAccessException dataAccessException){
    		logger.error("批处理失败 : " + dataAccessException.getMessage());
    		throw new Exception("批处理发生错误");
    	}
    	catch(Exception exception){
    		logger.error("批处理失败 : " + exception.getMessage());
    		throw new Exception("批处理发生错误");
    	}  		
		finally {
			releaseSession(session);
		}
    }	
    
    /**
     * 执行sql语句进行批量操作
     * 
     * @param sql 查询语句
     * delete from 表名  where id=5
     */
    public void executeSql(String sql) throws Exception{
		try {
			getSession().createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			logger.error("sql操作失败 : " + e.getMessage());
		} 
    }
	
    @SuppressWarnings("unchecked")
	public Object executeSql(String sql , Class T , Finder finder) throws Exception{
    	try {
    		Query query = getSession().createSQLQuery(sql).addEntity(T);
    		Integer totalNum = ((Number) query.iterate().next()).intValue();
    		finder.setTotalSize(totalNum);
    		query.setFirstResult(finder.getFirstResult());
    		query.setMaxResults(finder.getPageSize());
    		return query.list();
		} catch (Exception e) {
			throw e;
		}
    }
}
