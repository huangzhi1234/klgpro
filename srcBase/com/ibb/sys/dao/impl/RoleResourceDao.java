package com.ibb.sys.dao.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.sys.dao.IRoleResourceDao;
import com.ibb.sys.model.RoleResourceBean;
import com.ibb.sys.model.RoleResourcePk;
/**
 * 角色权限管理Dao
 * @author kin wong
 */
@Component
public class RoleResourceDao extends BaseHibernateDao<RoleResourceBean, RoleResourcePk> implements IRoleResourceDao {
}
