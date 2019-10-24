package com.ibb.common.dao.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;


/**
 * 基础Dao
 * @param <T>
 * @param <PK>
 */
public interface IBaseDao<T extends Serializable, PK extends Serializable> {

	public Session getCurrentSession();
	public Serializable save(T o);
    
	public List findBySQL(String sql, Map<String, Object> params,Class o);
	public void delete(T o);
	
	public Query executeSQL(String sql,ResultTransformer transformers);
	
	public void deleteEntityByPK(PK pk);

	public void update(T o);

	public void saveOrUpdate(T o);

	public T get(Class<T> c, Serializable pk);

	public T get(String hql);

	public T get(String hql, Map<String, Object> params);
	
	public T findByFK(PK pk);

	public List<T> find(String hql);

	public T findByFK(String hql, Object o);

	public List<T> find(Criterion... criterions);

	public List<T> find(String hql, Map<String, Object> params);
	
	public List<T> findByList(String hql, Map<String, Object> params);
	
	public List<T> find(String hql, int page, int rows);

	public List<T> findBySQL(String sql, Map<String, Object> params, int page, int rows);
	
	public List<T> find(String hql, Map<String, Object> params, int page, int rows);
	
	public List findDto(String hql,Map<String,Object> params,Class c);
	/**
	 * 连表查询返回 DTO 分页
	 * @param hql
	 * @param params
	 * @param c DTO.class
	 * @return
	 * 
	 * @author Ou
	 * 2013-4-22
	 */
	public List findDto(String hql,Map<String,Object> params,int page,int rows,Class c);
	
	public List<T> find(String hql, Object[] param);
	
	public List findPropertys(String hql, Object[] param);
	
	public List findPropertys(String hql, Map<String, Object> params);
	
	public List<T> find(String hql, List<Object> param);

	public List<T> findByCriteria(final Criteria criteria);
	
	public int count(String hql, Object[] param); 
	public int count(String hql);

	public int count(String hql, Map<String, Object> params);
	
	public int countByList(String hql, Map<String, Object> params) ;

	 /*************
     * 创建Criteria对象
     * @param criterions 可变的Restrictions条件列表
     * @return
     */
    public Criteria createCriteria(Criterion... criterions);

    /************
     * 创建Criteria对象，带排序字段与升降序字段
     * @param orderBy
     * @param isAsc
     * @param criterions
     * @return
     */
    public Criteria createCriteria(String orderBy, boolean isAsc,Criterion... criterions);
	
	/************
     * 根据Criteria统计总数
     * @param criteria
     * @return
     */
    public int count(final Criteria criteria);
	public Query createQuery(String hql,Map<String, Object> params);

    public int executeHql(String hql);
    public int executeHql(String hql, Object[] param);
    public int executeHql(String hql,Map<String, Object> params);

    public SQLQuery executeSQL(String sql);
 
    public SQLQuery executeSQL(String sql,Map<String, Object> params);
    public SQLQuery executeSQL(String sql,int page, int rows,Map<String, Object> params);

	public List findBySql(String sql);
	public List findBySql(String sql, Map<String, Object> params);
	public int executeUpdateSQL(String sql,Map<String, Object> params); 
	public int executeUpdateSQL(String sql); 
	
	public int countSql(String sql, Map<String, Object> params);
	public List executeSQL(String sql,int page, int rows,Map<String, Object> params,Class c) ;
	
	public List<Map> findBySQLForPage(String sql, Map<String, Object> params, int page, int rows);
	
	public List findBySQLForPage(String sql, Map<String, Object> params, int page, int rows,Class o);
	
	public List executeSQL(String sql,Map<String, Object> params,Class c) ;
	public List getBusinessDataForPage(Map searchParams,String where,String sql01,int page,int rows);
	public List getBusinessDataForPage01(Map searchParams,String where,String countSql,String sql,int page,int rows,Boolean isCount,Boolean isPage);

}
