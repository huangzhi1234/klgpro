package com.ibb.service.patch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.patch.IPatchDlRcDao;
import com.ibb.dao.patch.impl.PatchDlRcDao;
import com.ibb.model.patch.PatchDlRc;
import com.ibb.service.patch.IPatchDlRcService;

@Service
public class PatchDlRcService extends BaseService<PatchDlRc, Integer> implements IPatchDlRcService{

	IPatchDlRcDao patchDlRcDao;
	
	@Autowired
	@Qualifier("patchDlRcDao")
	@Override
	public void setBaseDao(IBaseDao<PatchDlRc, Integer> baseDao) {
		this.baseDao = baseDao;		//注入到BaseService里的baseDao
        this.patchDlRcDao = (PatchDlRcDao) baseDao;		
	}

}
