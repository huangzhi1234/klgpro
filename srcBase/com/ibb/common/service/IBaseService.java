package com.ibb.common.service;

import java.util.List;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.pagination.Page;

/**
 * 通用Service层接口
 * 
 * @author kin wong
 */
public interface IBaseService<M extends java.io.Serializable, PK extends java.io.Serializable> {
    /**************************单表增、删、改**********************************/
	/**
	 * 保存一条记录
	 * @param model 对象实例
	 * @return 对象实例
	 */
    public M save(M model);

    /**
     * 保存或更新一条记录：无则保存，有则更新
     * @param model 对象实例
     */
    public void saveOrUpdate(M model);
    
    /**
     * 更新一条记录
     * @param model 对象实例
     */
    public void update(M model);
    
    /**
     * 根据对象合并一条记录
     * @param model 对象实例
     */
    public void merge(M model);

    /**
     * 根据主键删除一条记录  
     * @param id 对象主键
     */
    public void delete(PK id);

    /**
     * 根据对象删除一条记录
     * @param model 对象实例
     */
    public void deleteObject(M model);
 
    
    /**************************单表简单查（主键记录、全部记录）**********************************/
    /**
     * 根据主键获得一条记录
     * @param id 主键
     * @return 对象实例
     */
    public M get(PK id);
    
    /**
     * 获得全部记录
     * @return 记录列表
     */
    public List<M> queryListAll();
    
    /**
     * 根据分页信息（页码）获得记录列表
     * @param pn 页码
     * @return 一页记录列表
     */
    public Page<M> queryListAll(int pn);
    
    /**
     * 根据分页信息（页码，每页记录数）、主键获得分页列表
     * @param pn 页码
     * @param pageSize 每页记录数
     * @return 一页记录列表
     */
    public Page<M> queryListAll(int pn, int pageSize);
    
    
    /**************************单表复杂查（查询条件、排序信息）**********************************/
    
    /**
     * 将参数封装到Map中，并查询列表
     * @param query 参数
     * 
     * @return 查询列表
     */
    public List<M> queryListByCondition(final ConditionQuery query);
    
    /**
     * 将参数封装到Map中，并查询列表
     * @param query 参数
     * @param orderBy 排序信息
     * 
     * @return 查询列表
     */
    public List<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy);
    
    /**
     * 将参数封装到Map中，查询分页列表
     * @param query 参数
     * @param pn 页码
     * 
     * @return 分页列表
     */
    public Page<M> queryListByCondition(final ConditionQuery query, final int pn);
    
    /**
     * 将参数封装到Map中，查询分页列表
     * @param query 参数
     * @param orderBy 排序信息
     * @param pn 页码
     * 
     * @return 分页列表
     */
    public Page<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy, final int pn);
    
    /**
     * 将参数封装到Map中，查询分页列表
     * @param query 参数
     * @param pn 页码
     * @param pageSize 每页记录数
     * @return 分页列表
     */
    public Page<M> queryListByCondition(final ConditionQuery query, final int pn, final int pageSize);
    
    /**
     * 将参数封装到Map中，查询分页列表
     * @param query 参数
     * @param orderBy 排序信息
     * @param pn 页码
     * @param pageSize 每页记录数
     * @return 分页列表
     */
    public Page<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy, final int pn, final int pageSize);
}
