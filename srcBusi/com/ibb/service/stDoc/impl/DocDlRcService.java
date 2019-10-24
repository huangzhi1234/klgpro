package com.ibb.service.stDoc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.stDoc.IDocDlRcDao;
import com.ibb.model.stDoc.DocDlRc;
import com.ibb.service.stDoc.IDocDlRcService;
@Service
public class DocDlRcService extends BaseService<DocDlRc, Integer> implements IDocDlRcService{

	
	
	private IDocDlRcDao docDlRcDao;
	@Autowired
	@Qualifier("docDlRcDao")
	@Override
	public void setBaseDao(IBaseDao<DocDlRc, Integer> baseDao) {
		this.baseDao=baseDao;
		this.docDlRcDao=(IDocDlRcDao) baseDao;
	}

}
