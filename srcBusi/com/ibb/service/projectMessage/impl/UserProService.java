package com.ibb.service.projectMessage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.projectMessage.IUserProDao;
import com.ibb.dao.projectMessage.impl.UserProDao;
import com.ibb.model.projectMessage.UserProBean;
import com.ibb.service.projectMessage.IUserProService;

/**
 * 用户查看项目权限管理services
 * 
 * @author kin wong
 */
@Component
public class UserProService extends BaseService<UserProBean, Integer> implements IUserProService {

	private IUserProDao userProDao;

	@Autowired
	@Qualifier("userProDao")
	@Override
	public void setBaseDao(IBaseDao<UserProBean, Integer> baseDao) {
		this.baseDao = baseDao;
		this.userProDao = (UserProDao) baseDao;
	}

}