package com.ibb.common.service.impl;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.service.IBaseService;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.util.pagination.PageUtil;

import java.util.List;

/**
 * 通用Service层实现:有了通用代码后CURD就不用再写了
 * 
 * @author kin wong
 */
public abstract class BaseService<M extends java.io.Serializable, PK extends java.io.Serializable> implements IBaseService<M, PK> {
    
    protected IBaseDao<M, PK> baseDao;
    
    public abstract void setBaseDao(IBaseDao<M, PK> baseDao);
    
    @Override
    public M save(M model) {
        baseDao.save(model);
        return model;
    }
    
    @Override
    public void merge(M model) {
        baseDao.merge(model);
    }

    @Override
    public void saveOrUpdate(M model) {
        baseDao.saveOrUpdate(model);
    }

    @Override
    public void update(M model) {
        baseDao.update(model);
    }
    
    @Override
    public void delete(PK id) {
        baseDao.delete(id);
    }

    @Override
    public void deleteObject(M model) {
        baseDao.deleteObject(model);
    }

    @Override
    public M get(PK id) {
        return baseDao.get(id);
    }

    @Override
    public List<M> queryListAll() {
        return baseDao.queryListAll();
    }
    @Override
    public Page<M> queryListAll(int pn) {
        return this.queryListAll(pn, IPageContext.DEFAULT_PAGE_SIZE);
    }
    
    @Override
    public Page<M> queryListAll(int pn, int pageSize) {
        Integer count = baseDao.queryCountAll();
        List<M> items = baseDao.queryListAll(pn, pageSize);
        return PageUtil.getPage(count, pn, items, pageSize);
    }
    
    @Override
    public List<M> queryListByCondition(final ConditionQuery query){
        return baseDao.queryListByCondition(query);
    }
    
    @Override
    public List<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy){
        return baseDao.queryListByCondition(query, orderBy);
    }
    
    @Override
    public Page<M> queryListByCondition(final ConditionQuery query, final int pn){
        return this.queryListByCondition(query, pn, IPageContext.DEFAULT_PAGE_SIZE);
    }
    
    @Override
    public Page<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy, final int pn){
        return this.queryListByCondition(query, orderBy, pn, IPageContext.DEFAULT_PAGE_SIZE);
    }
    
    @Override
    public Page<M> queryListByCondition(final ConditionQuery query, final int pn, final int pageSize){
        Integer count = baseDao.queryCountByCondition(query);
        List<M> items = baseDao.queryListByCondition(query, pn, pageSize);
        return PageUtil.getPage(count, pn, items, pageSize);
    }
    
    @Override
    public Page<M> queryListByCondition(final ConditionQuery query, final OrderBy orderBy, final int pn, final int pageSize){
        Integer count = baseDao.queryCountByCondition(query);
        List<M> items = baseDao.queryListByCondition(query, orderBy, pn, pageSize);
        return PageUtil.getPage(count, pn, items, pageSize);
    }
}
