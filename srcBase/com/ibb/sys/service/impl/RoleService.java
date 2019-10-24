package com.ibb.sys.service.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.impl.BaseService;
import com.ibb.sys.dao.IRoleDao;
import com.ibb.sys.dao.IRoleResourceDao;
import com.ibb.sys.dao.IUserRoleDao;
import com.ibb.sys.dao.impl.RoleDao;
import com.ibb.sys.model.RoleBean;
import com.ibb.sys.model.RoleResourceBean;
import com.ibb.sys.model.UserRoleBean;
import com.ibb.sys.service.IRoleService;

/**
 * 角色管理services
 * 
 * @author kin wong
 */
@Component
public class RoleService extends BaseService<RoleBean, Integer> implements IRoleService {
	private IRoleDao roleDao;
	
	@Autowired
	private IUserRoleDao userRoleDao;
	
	@Autowired
	private IRoleResourceDao roleResourceDao;
	
	@Autowired
	@Qualifier("roleDao")
	@Override
	public void setBaseDao(IBaseDao<RoleBean, Integer> baseDao) {
		this.baseDao = baseDao;
        this.roleDao = (RoleDao) baseDao;
	}
	
	@Override
	public void deleteCasecade(int id) {
		delete(id);
		
		//用户角色
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("pk.roleId", id));
		List<UserRoleBean> userRoleList = userRoleDao.queryListByCondition(query);
		for (UserRoleBean userRole : userRoleList)
			userRoleDao.deleteObject(userRole);
		
		//角色资源
		query = new ConditionQuery();
		query.add(Restrictions.eq("pk.roleId", id));
		List<RoleResourceBean> roleResourceBeanList = roleResourceDao.queryListByCondition(query);
		for (RoleResourceBean roleResourceBean : roleResourceBeanList)
			roleResourceDao.deleteObject(roleResourceBean);
	}
}
