package com.ibb.service.score;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.IBaseService;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.score.ProScoreRule;
import com.ibb.model.stDoc.StdocUploadcheck;

public interface IProScoreRuleService extends IBaseService<ProScoreRule, Integer>{
	//计算个人上传的方法
		public Integer getUp(Integer userId);
	//计算下载积分的方法
		public Integer getDown(Integer userId);
		public void getAllScore();
}
