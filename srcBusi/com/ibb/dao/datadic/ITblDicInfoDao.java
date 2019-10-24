package com.ibb.dao.datadic;

import java.util.List;

import com.ibb.common.dao.IBaseDao;
import com.ibb.model.datadic.TblDicInfo;


public interface ITblDicInfoDao extends IBaseDao<TblDicInfo, Integer> {
	/**
	 * 根据字典编码得到数据
	 *@param dicNum
	 *@return
	 *@author：zhousiliang
	 *@Create Date：2014-10-27
	 */
   public TblDicInfo get(String dicNum);
   /**
	 * 通过父节点获取所有子级节点
	 * @param upSystemId
	 * @return
	 */
	public List<TblDicInfo> findNodes(String upSystemId);

	/**
	 * 获取相同父节点的最大节点ID
	 * @param upSystemId
	 * @return
	 */
	public String findMaxSystemId(String upSystemId);
	/**
	 * 通过中文名及上级中文名获取字典信息
	 */
	public TblDicInfo findByNameAndParentName(String name, String pname);
}
