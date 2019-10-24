package com.ibb.sys.service;

import java.util.List;

import com.ibb.common.service.IBaseService;
import com.ibb.service.datadic.TreeNode;
import com.ibb.sys.model.ResourceBean;

public interface IResourceService extends IBaseService<ResourceBean, Integer> {
	/**
	 * 根据父级编号查询树
	 * 
	 * @param pid 父级编号
	 * @return 树列表
	 */
	public List<ResourceBean> queryResourceByPid(int pid);
	
	/**
	 * 根据用户编号查询树
	 * 
	 * @param userId 用户编号
	 * @return 1-树列表，2-权限信息
	 */
	public Object[] queryResourceByUserId(int userId);
	
	/**
	 * 根据ID级联删除
	 * 
	 * @param id 结点ID
	 */
	public void deleteByIdCascade(int id);
	
	/**
	 * 调整记录排序
	 * @param itemId 记录id
	 * @prram orderType 调整类型
	 */
	public void updateOrder(int resourceId, String orderType);
	public List<TreeNode> getUserMenu(String uid,String pmid);
}
