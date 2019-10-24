package com.ibb.service.stDoc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.stDoc.IStdocDowncheckDao;
import com.ibb.model.stDoc.StdocDowncheck;
import com.ibb.service.stDoc.IStdocDowncheckService;
@Service
public class StdocDowncheckService extends BaseService<StdocDowncheck, Integer> implements IStdocDowncheckService{
	private IStdocDowncheckDao stdocDowncheckDao;
	@Autowired
	@Qualifier("stdocDowncheckDao")
	@Override
	public void setBaseDao(IBaseDao<StdocDowncheck, Integer> baseDao) {
		this.baseDao=baseDao;
		this.stdocDowncheckDao=(IStdocDowncheckDao) baseDao;
	}
}
