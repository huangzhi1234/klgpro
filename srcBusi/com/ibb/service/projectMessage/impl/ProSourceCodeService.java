package com.ibb.service.projectMessage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.projectMessage.IProSourceCodeDao;
import com.ibb.dao.projectMessage.impl.ProSourceCodeDao;
import com.ibb.model.projectMessage.ProSourceCode;
import com.ibb.service.projectMessage.IProSourceCodeService;

/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午3:03:20
 *
 */
@Service
public class ProSourceCodeService extends BaseService<ProSourceCode, Integer> implements IProSourceCodeService {
	private IProSourceCodeDao proSourceCodeDao;
		
	@Autowired
	@Qualifier("proSourceCodeDao")
	@Override
	public void setBaseDao(IBaseDao<ProSourceCode, Integer> baseDao) {
		this.baseDao = baseDao;		//注入到BaseService里的baseDao
        this.proSourceCodeDao = (ProSourceCodeDao) baseDao;
	}

}
