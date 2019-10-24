package com.ibb.sys.service;

import java.util.List;

import com.ibb.common.service.IBaseService;
import com.ibb.sys.model.UserBean;

public interface IUserService extends IBaseService<UserBean, Integer> {
	/**
	 * 级联删除(同时删除用户角色)
	 * @param id
	 */
	public void deleteCasecade(int id);
	
	public List<UserBean> getUserListWithProId(String proId);
	
	public List<UserBean> getUserListByCondition(String condition);
}
