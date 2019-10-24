package com.ibb.sys.dao.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.sys.dao.IDictDefDao;
import com.ibb.sys.model.DictDefBean;
/**
 * 数据字典定义管理Dao
 * @author kin wong
 */
@Component
public class DictDefDao extends BaseHibernateDao<DictDefBean, Integer> implements IDictDefDao {
}
