package com.ibb.common.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;

/**
 * 通用DAO接口
 * 
 * @author kin wong
 *
 * @param <M> 操作对象
 * @param <PK> 主键
 */
public interface IBaseDao<M extends java.io.Serializable, PK extends java.io.Serializable> {
	/*********************单表增、删、改************************/
	/**
	 * 保存一条数据
	 * @param model 待保存的对象
	 * @return 对象的主键
	 */
    public PK save(M model);

    /**
     * 保存或更新：无则保存，有则更新
     * @param model 操作对象
     */
    public void saveOrUpdate(M model);
    
    /**
     * 更新一条记录
     * @param model 操作对象
     */
    public void update(M model);
    
    /**
     * 合并模型对象状态到底层会话
     * @param model 操作对象
     */
    public void merge(M model);

    /**
     * 根据主键删除一条记录
     * @param id 主键
     */
    public void delete(PK id);

    /**
     * 根据对象删除一条记录
     * @param model 操作对象
     */
    public void deleteObject(M model);
    
    
    /************************单表普通查询（主键记录、全记录、主键存在）*******************************/
    /**
     * 根据主键获得一条记录对象
     * @param id 主键
     * @return 操作对象
     */
    public M get(PK id);
    
    /**
     * 获得所有记录对象列表
     * @return 记录对象列表
     */
    public List<M> queryListAll();
    
    /**
     * 是否存在该记录
     * @param id 主键
     * @return true-存在，false-不存在
     */
    public boolean exists(PK id);

    
    
    /************************单表分页查询（全查、条件查）*******************************/
    /**
     * 根据分页信息获得对象列表
     * @param pn 当前页数（第几页）
     * @param pageSize 页大小
     * @return 记录对象列表
     */
    public List<M> queryListAll(int pn, int pageSize);
    
    /**
     * 获得总记录数
     * @return 记录数
     */
    public int queryCountAll();
    
    /**
     * 根据查询条件查询对象列表
     * @param query 查询条件
     * 
     * @return 记录列表
     */
    public List<M> queryListByCondition(final ConditionQuery query);
    
    /**
     * 根据查询条件查询对象列表
     * @param query 查询条件
     * @param orderBy 排序方式
     * 
     * @return 记录列表
     */
    public List<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy);
    
    /**
     * 根据查询条件查询对象列表
     * @param query 查询条件
     * @param pn 页码
     * @param pageSize 每页记录数executeSQL
     * 
     * @return 记录列表
     */
    public List<M> queryListByCondition(final ConditionQuery query, final int pn, final int pageSize);
    
    /**
     * 根据查询条件查询对象列表
     * @param query 查询条件
     * @param orderBy 排序方式
     * @param pn 页码
     * @param pageSize 每页记录数
     * 
     * @return 记录列表
     */
    public List<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy, final int pn, final int pageSize);
 
    /**
     * 根据条件查询总记录数
     * @param query 查询条件
     * 
     * @return 记录数
     */
    public int queryCountByCondition(final ConditionQuery query);
    
  
    
    /************************hibernate缓存处理*******************************/
    /**
     * 强制刷新缓存(使内存与数据库一致)
     */
    public void flush();
    
    /**
     * 清空缓存
     */
    public void clear();
    /**
     * 建议直接使用session
     */
    public Session getCurrentSession();
    
  //----------------------------------------zhousiliang:add----------------------------------begin
     public List executeSQL(final String sql,final Map<String, Object> params);
     public List executeSQL(final String sql,final Map<String, Object> params,final int pn, final int pageSize);
	 public List<Map> findBySQLForPage(final String sql,final Map<String, Object> params,final int page,final int rows);
	 public List<M> find(final String hql, final Map<String, Object> params);
	 public List<M> find(final String hql);
	 public M get(final String hql,final Map<String, Object> params);
	 public List findPropertys(final String hql, final Object[] param);
	 public List executeSQL(final String sql,final Map<String, Object> params,final Class cla);
	 public M find(ConditionQuery query);
 //----------------------------------------zhousiliang:add----------------------------------end
	 
	 
	 

}
