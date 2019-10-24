package com.ibb.sys.dao;

import java.util.List;

import com.ibb.common.dao.IBaseDao;
import com.ibb.sys.model.UserBean;

public interface IUserDao extends IBaseDao<UserBean, Integer>{
	
	public List<UserBean> getUserListWithProId(String sql);
}
