package com.ibb.service.stDoc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.stDoc.IStDocDao;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.service.stDoc.IStDocService;
@Service
public class StDocService extends BaseService<StdocInfo, Integer> implements IStDocService{
	private IStDocDao stDocDao;
	@Autowired
	@Qualifier("stDocDao")
	@Override
	public void setBaseDao(IBaseDao<StdocInfo, Integer> baseDao) {
		this.baseDao=baseDao;
		this.stDocDao=(IStDocDao) baseDao;
	}

}
