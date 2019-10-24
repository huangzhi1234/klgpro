package com.ibb.common.dao.hibernate3;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Id;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.StringUtils;
import com.ibb.common.util.pagination.PageUtil;
import com.ibb.model.datadic.TBaseDataDictionary;

/**
 * 通用DAO层Hibernate3实现：实现了单表的CURD
 * @author kin wong
 *
 * @param <M> 操作对象
 * @param <PK> 主键(若存在联合主键时，请将联合主键封装成对象)
 */
public abstract class BaseHibernateDao<M extends java.io.Serializable, PK extends java.io.Serializable> implements IBaseDao<M, PK> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseHibernateDao.class);
    
    @Autowired
    @Qualifier("hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	private final Class<M> entityClass;//操作对象类对象
	private String pkName = null;//主键名称
	
    private final String HQL_LIST_ALL;//查询所有记录列表
    private final String HQL_COUNT_ALL;//查询所有记录总数
    

    /**
     * 构造函数
     * 
     * 【通过反射初始化model类的类型、主键】
     * 【初始化常见操作的HQL脚本：查询全部记录、查询记录数】
     */
    @SuppressWarnings("unchecked")
    public BaseHibernateDao() {
        this.entityClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Field[] fields = this.entityClass.getDeclaredFields();
        for(Field f : fields) {
            if(f.isAnnotationPresent(Id.class)) {
                this.pkName = f.getName();
            }
        }
        
        HQL_LIST_ALL = "from " + this.entityClass.getSimpleName() + " order by " + pkName + " desc";
        HQL_COUNT_ALL = "select count(*) from " + this.entityClass.getSimpleName();
    }
     
    /************************************************************************************************/
    /*********************************hibernate 增、删、改实现*******************************************/
    /************************************************************************************************/
    @SuppressWarnings("unchecked")
    public PK save(M model) {
        return (PK) hibernateTemplate.save(model);
    }

    public void update(M model) {
    	hibernateTemplate.update(model);
    }
    
    public void saveOrUpdate(M model) {
    	hibernateTemplate.saveOrUpdate(model);
    	
    }
    

    public void merge(M model) {
    	hibernateTemplate.merge(model);
    }

    public void delete(PK id) {
    	if(this.get(id) != null)
    		hibernateTemplate.delete(this.get(id));
    }

    @Override
    public void deleteObject(M model) {
    	hibernateTemplate.delete(model);
    }
    
    /************************************************************************************************/
    /***************************************** hibernate 缓存处理  *************************************/
    /***********************************************************************************************/
    @Override
    public void flush() {
    	hibernateTemplate.flush();
    }

    @Override
    public void clear() {
    	hibernateTemplate.clear();
    }
    
    /************************************************************************************************/
    /***************hibernate 查询（是否存在记录、全部查询记录或记录数）实现*************************************/
    /************************************************************************************************/
    @Override
    public boolean exists(PK id) {
        return get(id) != null;
    }

	@Override
    public M get(PK id) {
        return (M) hibernateTemplate.get(this.entityClass, id);
    }

    @Override
    public int queryCountAll() {
        Long total = aggregate(HQL_COUNT_ALL);
        return total.intValue();
    }

    @Override
    public List<M> queryListAll() {
        return list(HQL_LIST_ALL);
    }

    @Override
    public List<M> queryListAll(int pn, int pageSize) {
        return list(HQL_LIST_ALL, pn, pageSize,null);
    }
    
    public Session getCurrentSession() {
    	 
		return this.hibernateTemplate.getSessionFactory().getCurrentSession();
	} 
    
    @Override
    public List<M> queryListByCondition(final ConditionQuery query) {
    	return hibernateTemplate.execute(new HibernateCallback<List<M>>() {
    		public List<M> doInHibernate(Session session) throws HibernateException, SQLException{
    			Criteria criteria = session.createCriteria(entityClass);
    	        query.build(criteria);
    	        return criteria.list();
    		}	
    	});
    }
    
    @Override
    public List<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy) {
    	return hibernateTemplate.execute(new HibernateCallback<List<M>>() {
    		public List<M> doInHibernate(Session session) throws HibernateException, SQLException{
    			Criteria criteria = session.createCriteria(entityClass);
    	        query.build(criteria);
    	        orderBy.build(criteria);
    	        return criteria.list();
    		}	
    	});
    }
    
    @Override
    public List<M> queryListByCondition(final ConditionQuery query, final int pn, final int pageSize) {
    	return hibernateTemplate.execute(new HibernateCallback<List<M>>() {
    		public List<M> doInHibernate(Session session) throws HibernateException, SQLException{
    			Criteria criteria = session.createCriteria(entityClass);
    	        query.build(criteria);
    	        int start = PageUtil.getPageStart(pn, pageSize);
    	        if(start != 0) {
    	            criteria.setFirstResult(start);
    	        }
    	        criteria.setMaxResults(pageSize);
    	        return criteria.list();
    		}	
    	});
    }
    
    @Override
    public List<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy, final int pn, final int pageSize) {
    	return hibernateTemplate.execute(new HibernateCallback<List<M>>() {
    		public List<M> doInHibernate(Session session) throws HibernateException, SQLException{
    			Criteria criteria = session.createCriteria(entityClass);
    	        query.build(criteria);
    	        orderBy.build(criteria);
    	        int start = PageUtil.getPageStart(pn, pageSize);
    	        if(start != 0) {
    	            criteria.setFirstResult(start);
    	        }
    	        criteria.setMaxResults(pageSize);
    	        return criteria.list();		//这里就是返回查询结果了
    		}	
    	});
    }
    
    @Override
	public int queryCountByCondition(final ConditionQuery query) {
		Integer count = (Integer) hibernateTemplate.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = session.createCriteria(entityClass);
						query.build(criteria);
						return criteria.setProjection(Projections.rowCount()).uniqueResult();
					}
				});
		return count.intValue();
	}
    
    /************************************************************************************************/
    /***************hibernate 高级查询（条件查询记录或记录数）实现**********************************************/
    /***************【注意：以下方法尚未开放接口，因此service层不能直接调用，请在dao层方法中调用】************************/
    /************************************************************************************************/
   
    /**
     * 根据HQL语句返回整数记录
     * @param hql HQL语句 
     * @param paramlist 参数列表
     * @return 整数记录
     */
    protected long getIdResult(String hql, Object... paramlist) {
        long result = -1;
        List<?> list = list(hql, paramlist);
        if (list != null && list.size() > 0) {
            return ((Number) list.get(0)).longValue();
        }
        return result;
    }

    /**
     * 根据HQL语句查询一页数据信息
     * @param hql HQL语句
     * @param pn 页数
     * @param pageSize 每页记录数 
     * @param paramlist 参数列表
     * @return 对象列表
     */
    protected List<M> listSelf(final String hql, final int pn, final int pageSize, final Object... paramlist) {
        return this.<M> list(hql, pn, pageSize, paramlist);
    }

    /**
     * 根据HQL语句查询一页数据信息
     * @param <T>
     * @param hql HQL语句
     * @param start 页数
     * @param length 每页记录数
     * @param map in参数列表
     * @param paramlist 参数列表
     * @return 一页对象列表
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> listWithIn(final String hql,final int start, final int length, final Map<String, Collection<?>> map, final Object... paramlist) {
    	return hibernateTemplate.execute(new HibernateCallback<List<T>>() {
    		public List<T> doInHibernate(Session session) throws HibernateException, SQLException{
    			Query query = session.createQuery(hql);
    	       
    			setParameters(query, paramlist);
    	        for (Entry<String, Collection<?>> e : map.entrySet()) {
    	            query.setParameterList(e.getKey(), e.getValue());
    	        }
    	        
    	        if (start > -1 && length > -1) {
    	            query.setMaxResults(length);
    	            if (start != 0) {
    	                query.setFirstResult(start);
    	            }
    	        }
    	        
    	        List<T> results = query.list();
    	        
    	        return  results;
			}
		});
    }

    /**
     * 根据HQL语句查询一页数据信息
     * @param <T>
     * @param hql HQL语句
     * @param pn 页数
     * @param pageSize 每页记录数
     * @param paramlist 参数列表
     * @return 一页对象列表
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> list(final String hql, final int pn, final int pageSize, final Object... paramlist) {
    	return hibernateTemplate.execute(new HibernateCallback<List<T>>() {
    		public List<T> doInHibernate(Session session) throws HibernateException, SQLException{
    			Query query = session.createQuery(hql);
    	        setParameters(query, paramlist);
    	        if (pn > -1 && pageSize > -1) {
    	            query.setMaxResults(pageSize);
    	            int start = PageUtil.getPageStart(pn, pageSize);
    	            if (start != 0) {
    	                query.setFirstResult(start);
    	            }
    	        }
    	        if (pn < 0) {
    	            query.setFirstResult(0);
    	        }
    	        List<T> results = query.list();
    	        return results;
    		}	
    		
    	});
    }

    /**
     * 根据HQL语句返回唯一一条记录
     * @param <T>
     * @param hql
     * @param paramlist
     * @return 记录对象
     */
    @SuppressWarnings("unchecked")
	protected <T> T unique(final String hql, final Object... paramlist) {
    	return hibernateTemplate.execute(new HibernateCallback<T>() {
    		public T doInHibernate(Session session) throws HibernateException, SQLException{
    			 Query query = session.createQuery(hql);
    		        setParameters(query, paramlist);
    		        return (T) query.setMaxResults(1).uniqueResult();
    		}	
    	});
    }

    /**
     * 根据in类型的HQL语句查询记录对象
     * @param <T>
     * @param hql HQL语句
     * @param map in语句参数
     * @param paramlist 参数列表
     * @return 记录对象
     */
    @SuppressWarnings("unchecked")
	protected <T> T aggregate(final String hql, final Map<String, Collection<?>> map, final Object... paramlist) {
    	return hibernateTemplate.execute(new HibernateCallback<T>() {
    		public T doInHibernate(Session session) throws HibernateException, SQLException{
    			Query query = session.createQuery(hql);
    	        if (paramlist != null) {
    	            setParameters(query, paramlist);
    	            for (Entry<String, Collection<?>> e : map.entrySet()) {
    	                query.setParameterList(e.getKey(), e.getValue());
    	            }
    	        }

    	        return (T) query.uniqueResult();
    		}	
    	});	
    }
        
    /**
     * 根据HQL语句查询记录对象
     * @param <T> 
     * @param hql HQL语句
     * @param paramlist 参数列表
     * @return 记录对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T aggregate(final String hql, final Object... paramlist) {
    	return hibernateTemplate.execute(new HibernateCallback<T>() {
    		public T doInHibernate(Session session) throws HibernateException, SQLException{
    			Query query = session.createQuery(hql);
    	        setParameters(query, paramlist);

    	        return (T) query.uniqueResult();
    		}	
    	});	
    }

    /**
     * 根据HQL进行更新、删除、插入及其它DDL操作
     * @param hql HQL语句
     * @param paramlist 参数列表
     * @return 处理标识
     */
    protected int execteBulk(final String hql, final Object... paramlist) {
    	return hibernateTemplate.execute(new HibernateCallback<Integer>() {
    		public Integer doInHibernate(Session session) throws HibernateException, SQLException{
    			 Query query = session.createQuery(hql);
    		        setParameters(query, paramlist);
    		        Object result = query.executeUpdate();
    		        return result == null ? 0 : ((Integer) result).intValue();
    		}	
    	});	
    }
    
    /**
     * 根据本地SQL进行更新、删除、插入及其它DDL操作
     * @param natvieSQL 本地SQL
     * @param paramlist 参数列表
     * @return 更新标识
     */
    protected int execteNativeBulk(final String natvieSQL, final Object... paramlist) {
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
    		public Integer doInHibernate(Session session) throws HibernateException, SQLException{
    			Query query = session.createSQLQuery(natvieSQL);
    	        setParameters(query, paramlist);
    	        Object result = query.executeUpdate();
    	        return result == null ? 0 : ((Integer) result).intValue();
    		}	
    	});
    }

    /**
     * 根据本地SQL查询列表
     * @param <T>
     * @param hql HQL
     * @param paramlist 参数列表
     * @return
     */
    protected <T> List<T> list(final String hql, final Object... paramlist) {
        return list(hql, -1, -1, paramlist);
    }
        
    /**
     * 根据本地SQL、查询条件、分页信息查询记录列表
     * @param <T>
     * @param nativeSQL 本地SQL
     * @param pn 页数
     * @param pageSize 每页记录数
     * @param entityList 换成实体对象
     * @param scalarList 返回值列表
     * @param paramlist 参数列表
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> listByNative(final String nativeSQL, final int pn, final int pageSize,
            final List<Entry<String, Class<?>>> entityList, 
            final List<Entry<String, Type>> scalarList, final Object... paramlist) {
    	return hibernateTemplate.execute(new HibernateCallback<List<T>>() {
    		public List<T> doInHibernate(Session session) throws HibernateException, SQLException{
    			SQLQuery query = session.createSQLQuery(nativeSQL);
    	        if (entityList != null) {
    	            for (Entry<String, Class<?>> entity : entityList) {
    	                query.addEntity(entity.getKey(), entity.getValue());
    	            }
    	        }
    	        if (scalarList != null) {
    	            for (Entry<String, Type> entity : scalarList) {
    	                query.addScalar(entity.getKey(), entity.getValue());
    	            }
    	        }

    	        setParameters(query, paramlist);

    	        if (pn > -1 && pageSize > -1) {
    	            query.setMaxResults(pageSize);
    	            int start = PageUtil.getPageStart(pn, pageSize);
    	            if (start != 0) {
    	                query.setFirstResult(start);
    	            }
    	        }
    	        List<T> result = query.list();
    	        return result;
    		}	
    	});
    }
        
    /**
     * 根据本地SQL、查询条件查询记录列表
     * @param <T>
     * @param natvieSQL 本地SQL
     * @param scalarList 返回参数列表
     * @param paramlist SQL参数列表
     * @return 记录列表
     */
    @SuppressWarnings("unchecked")
    protected <T> T aggregateByNative(final String natvieSQL, final List<Entry<String, Type>> scalarList, final Object... paramlist) {
    	return hibernateTemplate.execute(new HibernateCallback<T>() {
    		public T doInHibernate(Session session) throws HibernateException, SQLException{
    			SQLQuery query = session.createSQLQuery(natvieSQL);
    	        if (scalarList != null) {
    	            for (Entry<String, Type> entity : scalarList) {
    	                query.addScalar(entity.getKey(), entity.getValue());
    	            }
    	        }

    	        setParameters(query, paramlist);

    	        Object result = query.uniqueResult();
    	        return (T) result;
    		}	
    	});	
    }
       
    /**
     * 根据查询条件返回记录列表
     * @param <T>
     * @param criteria 查询条件对象
     * @return 记录列表
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> list(Criteria criteria) {
        return criteria.list();
    }

    /**
     * 根据查询条件返回唯一记录 
     * @param <T>
     * @param criteria 查询条件对象
     * @return 记录对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T unique(Criteria criteria) {
        return (T) criteria.uniqueResult();
    }

    /**
     * 根据查询条件返回记录列表
     * @param <T>
     * @param criteria 查询条件对象
     * @return 记录列表
     */
    protected <T> List<T> list(DetachedCriteria criteria) {
        return list(criteria.getExecutableCriteria(hibernateTemplate.getSessionFactory().getCurrentSession()));
    }

    /**
     * 根据查询条件返回唯一记录
     * @param <T>
     * @param criteria 查询条件对象
     * @return 记录对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T unique(DetachedCriteria criteria) {
        return (T) unique(criteria.getExecutableCriteria(hibernateTemplate.getSessionFactory().getCurrentSession()));
    }

    /**
     * 通用参数列表设置方法：设置HQL/SQL语句中的参数
     * @param query 查询对象
     * @param paramlist 参数列表
     */
    protected void setParameters(Query query, Object[] paramlist) {
        if (paramlist != null) {
            for (int i = 0; i < paramlist.length; i++) {
                if(paramlist[i] instanceof Date) {
                    query.setTimestamp(i, (Date)paramlist[i]);
                } else {
                    query.setParameter(i, paramlist[i]);
                }
            }
        }
    }

//----------------------------------------zhousiliang:add----------------------------------begin
	
	
	public List executeSQL(final String sql,final Map<String, Object> params) {

		   return hibernateTemplate.execute(new HibernateCallback<List>() {
				public List doInHibernate(Session session) throws HibernateException, SQLException{
		
					SQLQuery q = session.createSQLQuery(sql);
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
					return q.list();
					  
				 }	
			});	
	}
	
	@Override
	public List executeSQL(final String hql, final Map<String, Object> params, final int pn,
			final int pageSize) {

		   return hibernateTemplate.execute(new HibernateCallback<List>() {
				public List doInHibernate(Session session) throws HibernateException, SQLException{
		
					Query q = session.createQuery(hql);
				
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
					if(pn > 0){
						int start = PageUtil.getPageStart(pn, pageSize);
						q.setFirstResult(start);
						q.setMaxResults(pageSize);
					}
					return q.list();
					  
				 }	
			});	
	}
	
	public List executeSQL(final String sql,final Map<String, Object> params,final Class cla) {

		   return hibernateTemplate.execute(new HibernateCallback<List>() {
				public List doInHibernate(Session session) throws HibernateException, SQLException{
		
					SQLQuery q = session.createSQLQuery(sql);
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
					return q.addEntity(cla).list();
					  
				 }	
			});	
	}
    
    public List<Map> findBySQLForPage(final String sql, final Map<String, Object> params, final int page, final int rows) {
		
		return hibernateTemplate.execute(new HibernateCallback<List<Map>>() {
    		public List<Map> doInHibernate(Session session) throws HibernateException, SQLException{

    			SQLQuery q = (SQLQuery)session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
    	});	
	}
    
    
    
    public List<M> find(final String hql, final Map<String, Object> params) {
    	
    	return hibernateTemplate.execute(new HibernateCallback<List<M>>() {
    		public List<M> doInHibernate(Session session) throws HibernateException, SQLException{

    			Query q = session.createQuery(hql);
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
    			return q.list();
    		}	
    	});	
    	
    	
	}
    
    
  public List<M> find(final String hql) {
    	
    	return hibernateTemplate.execute(new HibernateCallback<List<M>>() {
    		public List<M> doInHibernate(Session session) throws HibernateException, SQLException{

    			Query q = session.createQuery(hql);
    			return q.list();
    		}	
    	});	
    	
    	
	}
    
  
  public M get(final String hql,final Map<String, Object> params) {
		  return hibernateTemplate.execute(new HibernateCallback<M>() {
		  		public M doInHibernate(Session session) throws HibernateException, SQLException{
		
		  			Query q =session.createQuery(hql);
		  			if (params != null && !params.isEmpty()) {
		  				for (String key : params.keySet()) {
		  					q.setParameter(key, params.get(key));
		  				}
		  			}
		  			List<M> l = q.list();
		  			if (l != null && l.size() > 0) {
		  				return l.get(0);
		  			}
		  			return null;
		  		}	
	  	});	
	
}
  
  public List findPropertys(final String hql, final Object[] param) {
  
  
	   return hibernateTemplate.execute(new HibernateCallback<List<M>>() {
			public List<M> doInHibernate(Session session) throws HibernateException, SQLException{
	
					Query q = session.createQuery(hql);
					if (param != null && param.length > 0) {
						for (int i = 0; i < param.length; i++) {
							q.setParameter(i, param[i]);
						}
					}
					return q.list();
				  
			 }	
		});	
  }
  
  
  public M find(ConditionQuery query){
	 List<M> list= queryListByCondition(query);
	 M m=null;
	 if (list.size()>0) {
		m=list.get(0);
	 }
	 return m;
	  
  }
  /**
   * 通过sql进行分页查询
   * @param sql
   * @param params
   * @param pn
   * @param pageSize
   * @return
   */
  public List executeBySql(final String sql, final Map<String, Object> params, final int pn,
			final int pageSize) {

		   return hibernateTemplate.execute(new HibernateCallback<List>() {
				public List doInHibernate(Session session) throws HibernateException, SQLException{
		
					SQLQuery q = session.createSQLQuery(sql);
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
					if(pn > 0){
						int start = PageUtil.getPageStart(pn, pageSize);
						q.setFirstResult(start);
						q.setMaxResults(pageSize);
					}
					return q.list();
					  
				 }	
			});	
	}

  
  
  
//----------------------------------------zhousiliang:add----------------------------------end
}