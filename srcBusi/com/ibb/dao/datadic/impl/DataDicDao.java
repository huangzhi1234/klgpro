package com.ibb.dao.datadic.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.sys.dao.IRoleDao;
import com.ibb.sys.model.RoleBean;
/**
 * 角色管理Dao
 * @author huang zhi
 */
@Component
public class DataDicDao extends BaseHibernateDao<RoleBean, Integer> implements IRoleDao {
}
