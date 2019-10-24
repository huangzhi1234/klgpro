package com.ibb.service.projectMessage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.projectMessage.IProInfoDao;
import com.ibb.dao.projectMessage.impl.ProInfoDao;
import com.ibb.model.projectMessage.ProInfo;
import com.ibb.service.projectMessage.IProInfoService;

/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午2:36:12
 *
 */
@Service
public class ProInfoService extends BaseService<ProInfo, Integer> implements IProInfoService {
	private IProInfoDao proInfoDao;
		
	@Autowired
	@Qualifier("proInfoDao")
	@Override
	public void setBaseDao(IBaseDao<ProInfo, Integer> baseDao) {
		this.baseDao = baseDao;		//注入到BaseService里的baseDao
        this.proInfoDao = (ProInfoDao) baseDao;
	}

}
