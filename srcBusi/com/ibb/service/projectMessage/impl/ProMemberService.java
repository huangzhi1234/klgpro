package com.ibb.service.projectMessage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.projectMessage.IProMemberDao;
import com.ibb.dao.projectMessage.impl.ProMemberDao;
import com.ibb.model.projectMessage.ProMember;
import com.ibb.service.projectMessage.IProMemberService;

/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午3:03:20
 *
 */
@Service
public class ProMemberService extends BaseService<ProMember, Integer> implements IProMemberService {
	private IProMemberDao proMemberDao;
		
	@Autowired
	@Qualifier("proMemberDao")
	@Override
	public void setBaseDao(IBaseDao<ProMember, Integer> baseDao) {
		this.baseDao = baseDao;		//注入到BaseService里的baseDao
        this.proMemberDao = (ProMemberDao) baseDao;
	}

}
