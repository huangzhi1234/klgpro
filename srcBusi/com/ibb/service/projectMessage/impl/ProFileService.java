package com.ibb.service.projectMessage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.projectMessage.IProFileDao;
import com.ibb.model.projectMessage.ProFile;
import com.ibb.service.projectMessage.IProFileService;

@Service
public class ProFileService extends BaseService<ProFile, Integer> implements IProFileService{
	
	private IProFileDao proFileDao;
	@Autowired
	@Qualifier("proFileDao")
	
	
	@Override
	public void setBaseDao(IBaseDao<ProFile, Integer> baseDao) {
		this.baseDao=baseDao;
		this.proFileDao=(IProFileDao) baseDao;
	}
	@Override
	public JSONArray getMenu(int parentId,String proNum) {
		return (JSONArray)proFileDao.getMenu(parentId,proNum);
	}

}
