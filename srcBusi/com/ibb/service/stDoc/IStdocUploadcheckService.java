package com.ibb.service.stDoc;

import com.ibb.common.service.IBaseService;
import com.ibb.model.stDoc.StdocUploadcheck;

public interface IStdocUploadcheckService extends IBaseService<StdocUploadcheck, Integer>{
	public Boolean isShenHe(Integer id);
}
