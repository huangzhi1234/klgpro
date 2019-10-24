package com.ibb.service.projectMessage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.projectMessage.IProSubInfoDao;
import com.ibb.dao.projectMessage.impl.ProSubInfoDao;
import com.ibb.model.projectMessage.ProSubInfo;
import com.ibb.service.projectMessage.IProSubInfoService;

@Service
public class ProSubInfoService extends BaseService<ProSubInfo, Integer> implements IProSubInfoService {
	private IProSubInfoDao proSubInfoDao;
	
	@Autowired
	@Qualifier("proSubInfoDao")
	@Override
	public void setBaseDao(IBaseDao<ProSubInfo, Integer> baseDao) {
		this.baseDao = baseDao;		//注入到BaseService里的baseDao
        this.proSubInfoDao = (ProSubInfoDao) baseDao;
	}

}
