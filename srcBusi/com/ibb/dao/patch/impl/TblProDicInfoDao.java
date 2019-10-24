package com.ibb.dao.patch.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.StringUtils;
import com.ibb.dao.patch.ITblProDicInfoDao;
import com.ibb.model.datadic.TblDicInfo;
import com.ibb.model.patch.TblProDicInfo;

import exception.IbbMgrException;

@Repository("tblProDicInfoDao")
public class TblProDicInfoDao extends BaseHibernateDao<TblProDicInfo, Integer> implements ITblProDicInfoDao {
	/**
	 * 根据字典编码得到数据
	 * 
	 * @param dicNum
	 * @return
	 * @author：zhousiliang
	 * @Create Date：2014-10-27
	 */
	public TblProDicInfo get(String dicNum) {
		TblProDicInfo dicInfo = null;

		ConditionQuery conditionQuery = new ConditionQuery();
		if (StringUtils.isNotBlank(dicNum)) {
			conditionQuery.add(Restrictions.eq("dicNum", dicNum));
			List<TblProDicInfo> lists = this.queryListByCondition(conditionQuery);
			if (lists.size() > 0) {
				dicInfo = lists.get(0);
			}
		} else {
			throw new IbbMgrException("字典编码不能为空");
		}

		return dicInfo;
	}

	/**
	 * 通过父节点获取所有子级节点
	 * 
	 * @param upSystemId
	 * @return
	 */
	public List<TblProDicInfo> findNodes(String upSystemId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = null;
		if (StringUtils.isNull(upSystemId)) {
			hql = "from TblProDicInfo dic where dic.operId='' or dic.operId is null ";
		} else {
			hql = "from TblProDicInfo dic where dic.operId= :operId";
			params.put("operId", upSystemId);
		}
		return this.find(hql, params);
	}

	/**
	 * 获取相同父节点的最大节点ID
	 * 
	 * @param upSystemId
	 * @return
	 */
	public String findMaxSystemId(String upSystemId) {
		String hql = "Select max(dic.dicNum) from TblProDicInfo dic where dic.operId=:upSystemId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("upSystemId", upSystemId);
		List list = this.find(hql, params);
		if (list.size() > 0)
			return (String) list.get(0);
		return null;
	}

	/**
	 * 通过中文名及上级中文名获取字典信息
	 */
	public TblProDicInfo findByNameAndParentName(String name, String pname) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select t from TblProDicInfo t,TblProDicInfo tp where t.operId=tp.dicNum and t.dicName= :name and tp.dicName= :pname";
		params.put("name", name);
		params.put("pname", pname);
		return this.get(hql, params);
	}

}
