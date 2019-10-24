package com.ibb.service.score;

import com.ibb.common.service.IBaseService;
import com.ibb.model.score.ProScore;

public interface IProScoreService extends IBaseService<ProScore, Integer>{
	public void addUpScore(Integer userId,Integer score);
}
