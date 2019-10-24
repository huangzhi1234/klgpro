package com.ibb.service.projectMessage;

import com.alibaba.fastjson.JSONArray;
import com.ibb.common.service.IBaseService;
import com.ibb.model.projectMessage.ProFile;

public interface IProFileService extends IBaseService<ProFile, Integer>{
	JSONArray getMenu(int parentId,String proNum);
}
