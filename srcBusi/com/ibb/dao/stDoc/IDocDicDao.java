package com.ibb.dao.stDoc;

import java.util.List;

import com.ibb.common.dao.IBaseDao;
import com.ibb.model.stDoc.DocDicInfo;

public interface IDocDicDao extends IBaseDao<DocDicInfo, Integer>{
	/**
	 * 根据字典编码得到数据
	 * @param dicNum
	 * @return
	 */
	public DocDicInfo get(String dicNum);
	/**
	 * 根据父节点获取所有子级节点
	 * @param upSystemId
	 * @return
	 */
	public List<DocDicInfo> findNodes(String upSystemId);
	/**
	 * 获取相同父节点的最大节点ID
	 * @param upSystemId
	 * @return
	 */
	public String findMaxSystemId(String upSystemId);
	/**
	 * 通过中文名及上级中文名获取字典信息
	 * @param name
	 * @param pname
	 * @return
	 */
	public DocDicInfo findByNameAndParentName(String name, String pname);
}
