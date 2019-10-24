package com.ibb.service.projectMessage.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.projectMessage.impl.ProAttachDao;
import com.ibb.model.projectMessage.ProAttach;
import com.ibb.service.datadic.TreeNode;
import com.ibb.service.projectMessage.IproAttachService;
/**
 * 
 * @author Administrator
 *
 */
@Service
public class ProAttachService extends BaseService<ProAttach, Integer> implements IproAttachService{
	
	private ProAttachDao proAttachDao;
	@Autowired
	@Qualifier("proAttachDao")
	@Override
	public void setBaseDao(IBaseDao<ProAttach, Integer> baseDao) {
		this.baseDao=baseDao;
		this.proAttachDao=(ProAttachDao) baseDao;
	}
	
}
