package com.ibb.sys.service;

import com.ibb.common.service.IBaseService;
import com.ibb.sys.model.RoleBean;

public interface IRoleService extends IBaseService<RoleBean, Integer> {
	/**
	 * 级联删除(同时删除用户角色、角色资源)
	 * @param id
	 */
	public void deleteCasecade(int id);
}
