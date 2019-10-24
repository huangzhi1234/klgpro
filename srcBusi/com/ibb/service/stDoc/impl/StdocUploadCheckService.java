package com.ibb.service.stDoc.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.stDoc.IStdocUploadcheckDao;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.stDoc.StdocUploadcheck;
import com.ibb.service.stDoc.IStdocUploadcheckService;

@Service
public class StdocUploadCheckService extends BaseService<StdocUploadcheck, Integer> implements IStdocUploadcheckService{
	private IStdocUploadcheckDao stdocUploadcheckDao;
	@Autowired
	@Qualifier("stdocUploadcheckDao")
	@Override
	public void setBaseDao(IBaseDao<StdocUploadcheck, Integer> baseDao) {
		this.baseDao=baseDao;
		this.stdocUploadcheckDao=(IStdocUploadcheckDao) baseDao;
	}

	

	/**
	 * 查询是否处于审批状态
	 * @param id
	 * @return
	 */
	public Boolean isShenHe(Integer id){
		if(id!=null){
	
			java.util.List<StdocUploadcheck> list;
			try {
				ConditionQuery query = new ConditionQuery();
				query.add(Restrictions.eq("stdocId", id));
				query.add(Restrictions.eq("state", 1));
				list = this.queryListByCondition(query);
				if(list!=null && list.size()>0){
					return true;
				}
			} catch (Exception e) {
				System.out.println("查询审核表过程中报错！！！");
			}
			
		}
		return false;
	}

	
	
}
