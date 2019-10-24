package com.ibb.service.patch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.patch.ICheckLoadDao;
import com.ibb.model.patch.CheckLoad;
import com.ibb.service.patch.ICheckLoadService;

@Service
public class CheckLoadService extends BaseService<CheckLoad, Integer> implements ICheckLoadService{
	private ICheckLoadDao checkLoadDao;
	@Autowired
	@Qualifier("checkLoadDao")
	
	@Override
	public void setBaseDao(IBaseDao<CheckLoad, Integer> baseDao) {
		this.baseDao = baseDao; // 注入到BaseService里的baseDao
		this.checkLoadDao=(ICheckLoadDao) baseDao;
	}
	
}
