package com.ibb.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.sys.dao.IUserRoleDao;
import com.ibb.sys.dao.impl.UserRoleDao;
import com.ibb.sys.model.UserRoleBean;
import com.ibb.sys.model.UserRolePk;
import com.ibb.sys.service.IUserRoleService;

/**
 * 用户管理services
 * 
 * @author kin wong
 */
@Component
public class UserRoleService extends BaseService<UserRoleBean, UserRolePk> implements IUserRoleService {
	private IUserRoleDao userRoleDao;
	
	@Autowired
	@Qualifier("userRoleDao")
	@Override
	public void setBaseDao(IBaseDao<UserRoleBean, UserRolePk> baseDao) {
		this.baseDao = baseDao;
        this.userRoleDao = (UserRoleDao) baseDao;
	}
}
