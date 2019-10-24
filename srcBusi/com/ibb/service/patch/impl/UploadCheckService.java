package com.ibb.service.patch.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import antlr.collections.List;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.patch.IUploadCheckDao;
import com.ibb.model.patch.UploadCheck;
import com.ibb.service.patch.IUploadCheckService;
@Service
public class UploadCheckService extends BaseService<UploadCheck, Integer> implements IUploadCheckService{
	private IUploadCheckDao uploadCheckDao;
	@Autowired
	@Qualifier("uploadCheckDao")
	@Override
	public void setBaseDao(IBaseDao<UploadCheck, Integer> baseDao) {
		this.baseDao=baseDao;
		this.uploadCheckDao=(IUploadCheckDao) baseDao;
	}
	

	
	/**
	 * 查询是否处于审批状态
	 * @param id
	 * @return
	 */
	public Boolean isShenHe(Integer id){
		if(id!=null){
			java.util.List<UploadCheck> list;
			try {
				ConditionQuery query = new ConditionQuery();
				query.add(Restrictions.eq("patId", id));
				query.add(Restrictions.eq("state", 1));
				list = this.queryListByCondition(query);
				if(list!=null && list.size()>0){
					return true;
				}
			} catch (Exception e) {
				System.out.println("查询成功审核表过程中报错！！！");
			}
			
		}
		return false;
	}

}
