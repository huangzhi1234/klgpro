package com.ibb.sys.dao.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.sys.dao.IUserRoleDao;
import com.ibb.sys.model.UserRoleBean;
import com.ibb.sys.model.UserRolePk;
/**
 * 用户角色管理Dao
 * @author kin wong
 */
@Component
public class UserRoleDao extends BaseHibernateDao<UserRoleBean, UserRolePk> implements IUserRoleDao {
}
