package com.ibb.common.dao.base.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibb.common.dao.base.IBaseDao;
import com.ibb.common.util.GenericsUtils;
import com.ibb.common.util.NumberUtils;
import com.ibb.common.util.StringUtils;

/**
 * 基础dao的实现类
 * @param <T>
 * @param <PK>
 */
@SuppressWarnings("unchecked")
public class BaseDao<T extends Serializable, PK extends Serializable> implements IBaseDao<T, PK> {
   
	@Autowired
	private SessionFactory sessionFactory;
	

	private Class<T> pojoClass;

	/**********
	 * 初始化DAO，获取POJO类型
	 */
	public BaseDao() {
		this.pojoClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	/***********
	 * 获得该DAO对应的POJO类型
	 * 
	 * @return
	 */
	public Class<T> getPojoClass() {
		return this.pojoClass;
	}

	/***********
	 * 获得该DAO对应的POJO类型名
	 * 
	 * @return
	 */
	public String getPojoClassName() {
		return getPojoClass().getName();
	}


	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	public Serializable save(T o) {
		return this.getCurrentSession().save(o);
	}

	public void delete(T o) {
		this.getCurrentSession().delete(o);
	}

	public void deleteEntityByPK(PK pk) {
		delete(findByFK(pk));
	}

	public void update(T o) {
		this.getCurrentSession().update(o);
	}

	public void saveOrUpdate(T o) {
		this.getCurrentSession().saveOrUpdate(o);
	}

	public T get(Class<T> c, Serializable pk) {
		return (T) this.getCurrentSession().get(c, pk);
	}

	public T get(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	public T get(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	public T findByFK(PK pk) {
		return (T) this.getCurrentSession().get(getPojoClass(), pk);
	}

	public List<T> find(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.list();
	}

	//通过外键查找对象
	public T findByFK(String hql,Object o)
	{
		return (T) this.getCurrentSession().createQuery(hql).setParameter(0, o).list().get(0);
	}
	
	public List<T> find(Criterion... criterions) {
		return createCriteria(criterions).list();
	}

	public List<T> find(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}
	public List<T> findByList(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				if(params.get(key) instanceof List)
					q.setParameterList(key, (List)params.get(key));
				else
					q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}
	public List<T> find(String hql, int page, int rows) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	
	@Deprecated
	public List<T> findBySQL(String sql, Map<String, Object> params, int page, int rows) {
		SQLQuery q = (SQLQuery)this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	
	public List<Map> findBySQLForPage(String sql, Map<String, Object> params, int page, int rows) {
		SQLQuery q = (SQLQuery)this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object value=params.get(key);
				if (value instanceof  Collection) {
					q.setParameterList(key, (Collection)value);
				}else{
					q.setParameter(key, params.get(key));
				}
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	
	public List findBySQLForPage(String sql, Map<String, Object> params, int page, int rows,Class o) {
		SQLQuery q = (SQLQuery)this.getCurrentSession().createSQLQuery(sql).addEntity(o);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	
	public List findBySQL(String sql, Map<String, Object> params,Class o) {
		SQLQuery q = (SQLQuery)this.getCurrentSession().createSQLQuery(sql).addEntity(o);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}
	

	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	public List findDto(String hql,Map<String,Object> params,Class c){
		Query q = this.getCurrentSession().createQuery(hql).setResultTransformer(Transformers.aliasToBean(c));
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}
	public List findDto(String hql,Map<String,Object> params,int page,int rows,Class c){
		Query q = this.getCurrentSession().createQuery(hql).setResultTransformer(Transformers.aliasToBean(c));
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				//q.setParameter(key, params.get(key));
				if(params.get(key) instanceof List)
					q.setParameterList(key, (List)params.get(key));
				else
					q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	public List<T> find(String hql, Object[] param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}
	public List findPropertys(String hql, Object[] param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}
	public List findPropertys(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
//				q.setParameter(key, params.get(key));
				if(params.get(key) instanceof List)
					q.setParameterList(key, (List)params.get(key));
				else
					q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}
	public int count(String hql, Object[] param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		Object obj = q.uniqueResult();
		if (obj == null)
			return 0;
		return ((Long) obj).intValue();
	}
	public List<T> find(String hql, List<Object> param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.list();
	}
	public int count(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		Object obj = q.uniqueResult();
		if (obj == null)
			return 0;
		return ((Long) obj).intValue();
	}

	public int count(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				//q.setParameter(key, params.get(key));
				if(params.get(key) instanceof List)
					q.setParameterList(key, (List)params.get(key));
				else
					q.setParameter(key, params.get(key));
			}
		}
		Object obj = q.uniqueResult();
		if (obj == null)
			return 0;
		return ((Long) obj).intValue();
	}				
	public int countByList(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				if(params.get(key) instanceof List)
					q.setParameterList(key, (List)params.get(key));
				else
					q.setParameter(key, params.get(key));
			}
		}
		Object obj = q.uniqueResult();
		if (obj == null)
			return 0;
		return ((Long) obj).intValue();
	}

	public List<T> findByCriteria(Criteria criteria) {
		return criteria.list();
	}

	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = this.getCurrentSession().createCriteria(getPojoClass());
		for (Criterion c : criterions){
			if(c!=null){
				criteria.add(c);
			}
		}
		return criteria;
	}

	public Criteria createCriteria(String orderBy, boolean isAsc, Criterion... criterions) {
		Criteria criteria = createCriteria(criterions);
		if (isAsc)
			criteria.addOrder(Order.asc(orderBy));
		else
			criteria.addOrder(Order.desc(orderBy));
		return criteria;
	}

	public int count(final Criteria criteria) {
		Integer count = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
		int totalCount = count.intValue();
		return totalCount;
	}

	public int executeHql(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.executeUpdate();
	}
	
	public Query createQuery(String hql,Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q;
	}
	public int executeHql(String hql, Object[] param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.executeUpdate();
	}
	public int executeHql(String hql,Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		
		return q.executeUpdate();
	}

	public SQLQuery executeSQL(String sql) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		return q;
	}
	
	public Query executeSQL(String sql,ResultTransformer transformers) {
		Query q = this.getCurrentSession().createSQLQuery(sql).setResultTransformer(transformers);
		return q;
	}
	
	public SQLQuery executeSQL(String sql,Map<String, Object> params) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()){
			for (String key : params.keySet()) {
				Object value=params.get(key);
				if (value instanceof  Collection) {
					q.setParameterList(key, (Collection)value);
				}else{
					q.setParameter(key, params.get(key));
				}
			}
		}
		return q;
	}
	
	public SQLQuery executeSQL(String sql,int page, int rows,Map<String, Object> params) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()){
			for (String key : params.keySet()) {
				Object value=params.get(key);
				if (value instanceof  Collection) {
					q.setParameterList(key, (Collection)value);
				}else{
					q.setParameter(key, params.get(key));
				}
			}
		}
		q.setFirstResult((page - 1) * rows).setMaxResults(rows);
		return q;
	}
	
	public int executeUpdateSQL(String sql,Map<String, Object> params) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()){
			for (String key : params.keySet()) {
				Object value=params.get(key);
				if (value instanceof  Collection) {
					q.setParameterList(key, (Collection)value);
				}else{
					q.setParameter(key, params.get(key));
				}
			}
		}
		
		return q.executeUpdate();
	}

	public int executeUpdateSQL(String sql) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);

		return q.executeUpdate();
	}
	/******************
	 * 将联合查询的结果内容从Map或者Object[]转换为实体类型
	 * 
	 * @param items
	 * @return
	 */
	private List transformResults(List items) {

		if (items.size() > 0) {
			if (items.get(0) instanceof Map) {
				ArrayList list = new ArrayList(items.size());
				for (int i = 0; i < items.size(); i++) {
					Map map = (Map) items.get(i);
					list.add(map.get(CriteriaSpecification.ROOT_ALIAS));
				}
				return list;
			} else if (items.get(0) instanceof Object[]) {
				ArrayList list = new ArrayList(items.size());
				int pos = 0;
				for (int i = 0; i < ((Object[]) items.get(0)).length; i++) {
					if (((Object[]) items.get(0))[i].getClass() == getPojoClass()) {
						pos = i;
						break;
					}
				}
				for (int i = 0; i < items.size(); i++) {
					list.add(((Object[]) items.get(i))[pos]);
				}
				return list;
			} else
				return items;
		} else
			return items;
	}
	public List findBySql(String sql) {
		Query q = this.getCurrentSession().createSQLQuery(sql);
		return q.list();
	}
	public List findBySql(String sql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}
	public Object findBySqlDto(String sql,Map<String,Object> params,Class c){
		Query q = this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(c));
		//Query q = this.getSessionFactory().getCurrentSession().createQuery(hql).setResultTransformer(Transformers.aliasToBean(c));
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}
	public List findBySql(String sql, Object[] param) {
		Query q = this.getCurrentSession().createSQLQuery(sql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}
	public void exeBatch2(final String sql,final List<String> valueList){
		this.getCurrentSession().doWork(new Work() {
			public void execute(Connection conn) throws SQLException {
				PreparedStatement stmt=conn.prepareCall(sql);
				conn.setAutoCommit(true);
				for(int i=0;i<valueList.size();i++){
					stmt.setString(1, "");
					stmt.addBatch();
					if(i%100==0){
						stmt.executeBatch();
						conn.commit();
					}
				}
				stmt.executeBatch();
				conn.commit();
			}
		});
	}
	
	public int countSql(String sql, Map<String, Object> params) {
		SQLQuery q = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object value=params.get(key);
				if (value instanceof  Collection) {
					q.setParameterList(key, (Collection)value);
				}else{
					q.setParameter(key, params.get(key));
				}
			}
		}
		Object obj = q.uniqueResult();
		if (obj == null)
			return 0;
		return NumberUtils.toInt(obj.toString());
	}	
	public List executeSQL(String sql,int page, int rows,Map<String, Object> params,Class c) {
		Query q = this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(c));
		if (params != null && !params.isEmpty()){
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		q.setFirstResult((page - 1) * rows).setMaxResults(rows);
		return q.list();
	}

	@Override
	public List executeSQL(String sql, Map<String, Object> params, Class c) {
		Query q = this.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(c));
		if (params != null && !params.isEmpty()){
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}
	
	/**
	 * 查询业务数据（包括分页查询：当page==0||rows==0）
	 * @param searchParams
	 * @param where
	 * @param sql01
	 * @param page
	 * @param rows
	 * @return
	 */
	public List getBusinessDataForPage(Map searchParams,String where,String sql01,int page,int rows){
		StringBuffer sql=new StringBuffer(sql01);

		if(StringUtils.isNotNull(where)){
			sql.append(where);
		}
		if(page==0||rows==0){
			 return  executeSQL(sql.toString(), searchParams).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		 }
	 return findBySQLForPage(sql.toString(), searchParams, page, rows);
	}
	
	/**
	 * 查询业务数据（包括分页查询：当page==0||rows==0）
	 * @param searchParams
	 * @param where
	 * @param countSql 统计总数语句
	 * @param sql 结果集查询语句
	 * @param page
	 * @param rows
	 * @param isCount 是否统计总数
	 * @param isPage 是否分页
	 * @return 当统计总数时，返回的是List,存储的是总数
	 */
	public List getBusinessDataForPage01(Map searchParams,String where,String countSql,String sql,int page,int rows,Boolean isCount,Boolean isPage){
		StringBuffer countSql_v=new StringBuffer(countSql);
		StringBuffer sql_v=new StringBuffer(sql);
		if(StringUtils.isNotNull(where)){
			countSql_v.append(where);
			sql_v.append(where);
		}
		
		if(isPage){
			if(isCount){//返回分页时记录总数
				 List<Integer>  list=new ArrayList<Integer>();
				 int  num=this.countSql(countSql_v.toString(), searchParams);
				 list.add(num);
				 return  list;
			 }else{
				 return findBySQLForPage(sql_v.toString(), searchParams, page, rows);
			 }
			
		}else{
			 return  executeSQL(sql_v.toString(), searchParams).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}
	
	}
}
