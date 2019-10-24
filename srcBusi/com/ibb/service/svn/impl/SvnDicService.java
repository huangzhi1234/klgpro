package com.ibb.service.svn.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.ibb.common.dao.IBaseDao;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.svn.ISvnDicDao;
import com.ibb.model.svn.SvnDicInfo;
import com.ibb.service.svn.ISvnDicService;




@Service
public class SvnDicService extends  BaseService<SvnDicInfo, Integer> implements ISvnDicService{
	private ISvnDicDao svnDicDao;
	@Autowired
	@Qualifier("svnDicDao")
	
	@Override
	public void setBaseDao(IBaseDao<SvnDicInfo, Integer> baseDao) {
		this.baseDao=baseDao;
		this.svnDicDao=(ISvnDicDao) baseDao;
	}
	
	
	


}
