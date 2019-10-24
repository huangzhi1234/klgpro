package com.ibb.service.patch;

import com.ibb.common.service.IBaseService;
import com.ibb.model.patch.UploadCheck;

public interface IUploadCheckService extends IBaseService<UploadCheck, Integer>{
	public Boolean isShenHe(Integer id);
}
