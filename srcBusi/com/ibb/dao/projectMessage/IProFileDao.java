package com.ibb.dao.projectMessage;

import com.alibaba.fastjson.JSONArray;
import com.ibb.common.dao.IBaseDao;
import com.ibb.model.projectMessage.ProFile;

public interface IProFileDao extends IBaseDao<ProFile, Integer>{
	JSONArray getMenu(int parentId,String proNum);
}
