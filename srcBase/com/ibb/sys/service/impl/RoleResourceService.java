package com.ibb.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.sys.dao.IRoleResourceDao;
import com.ibb.sys.dao.impl.RoleResourceDao;
import com.ibb.sys.model.RoleResourceBean;
import com.ibb.sys.model.RoleResourcePk;
import com.ibb.sys.service.IRoleResourceService;

/**
 * 角色权限管理services
 * 
 * @author kin wong
 */
@Component
public class RoleResourceService extends BaseService<RoleResourceBean, RoleResourcePk> implements IRoleResourceService {
	private IRoleResourceDao roleResourceDao;
	
	@Autowired
	@Qualifier("roleResourceDao")
	@Override
	public void setBaseDao(IBaseDao<RoleResourceBean, RoleResourcePk> baseDao) {
		this.baseDao = baseDao;
        this.roleResourceDao = (RoleResourceDao) baseDao;
	}
}
