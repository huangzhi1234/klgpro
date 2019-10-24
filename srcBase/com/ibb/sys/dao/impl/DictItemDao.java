package com.ibb.sys.dao.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.sys.dao.IDictItemDao;
import com.ibb.sys.model.DictItemBean;
/**
 * 数据字典项目管理Dao
 * @author kin wong
 */
@Component
public class DictItemDao extends BaseHibernateDao<DictItemBean, Integer> implements IDictItemDao {
}
