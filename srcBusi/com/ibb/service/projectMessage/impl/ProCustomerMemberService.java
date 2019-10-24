package com.ibb.service.projectMessage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.projectMessage.IProCustomerMemberDao;
import com.ibb.dao.projectMessage.impl.ProCustomerMemberDao;
import com.ibb.model.projectMessage.ProCustomerMember;
import com.ibb.service.projectMessage.IProCustomerMemberService;

@Service
public class ProCustomerMemberService extends BaseService<ProCustomerMember, Integer> implements IProCustomerMemberService{
	private IProCustomerMemberDao proCustomerMemberDao;
	@Autowired
	@Qualifier("proCustomerMemberDao")
	@Override
	public void setBaseDao(IBaseDao<ProCustomerMember, Integer> baseDao) {
		this.baseDao=baseDao;
		this.proCustomerMemberDao=(ProCustomerMemberDao)baseDao;
	}
	
	
}
