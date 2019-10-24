package com.ibb.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.sys.dao.ILoginInfoDao;
import com.ibb.sys.model.LoginInfo;
import com.ibb.sys.service.ILoginInfoService;

@Service
public class LoginInfoService extends BaseService<LoginInfo, Integer> implements ILoginInfoService{

	private ILoginInfoDao loginInfoDao;
	
	@Autowired
	@Qualifier("loginInfoDao")
	public void setBaseDao(IBaseDao<LoginInfo, Integer> baseDao) {
		this.baseDao = baseDao;
        this.loginInfoDao = (ILoginInfoDao) baseDao;
	}
}
