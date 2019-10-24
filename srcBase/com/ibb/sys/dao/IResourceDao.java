package com.ibb.sys.dao;

import java.util.List;

import com.ibb.common.dao.IBaseDao;
import com.ibb.sys.model.ResourceBean;

public interface IResourceDao extends IBaseDao<ResourceBean, Integer>{
	/**
	 * 根据用户编号查找权限列表
	 * 
	 * @param userId 用户编号
	 * @return
	 */
	public List<ResourceBean> queryListByUserId(int userId);
}
