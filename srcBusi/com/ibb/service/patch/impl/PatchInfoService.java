package com.ibb.service.patch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.patch.IPatchInfoDao;
import com.ibb.dao.patch.impl.PatchInfoDao;
import com.ibb.model.patch.PatchInfo;
import com.ibb.service.patch.IPatchInfoService;

@Service
public class PatchInfoService extends BaseService<PatchInfo, Integer> implements IPatchInfoService {

	IPatchInfoDao patchInfoDao;

	@Autowired
	@Qualifier("patchInfoDao")
	@Override
	public void setBaseDao(IBaseDao<PatchInfo, Integer> baseDao) {
		this.baseDao = baseDao; // 注入到BaseService里的baseDao
		this.patchInfoDao = (PatchInfoDao) baseDao;
	}

}
