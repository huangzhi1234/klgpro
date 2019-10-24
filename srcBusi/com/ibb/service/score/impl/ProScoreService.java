package com.ibb.service.score.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.score.IProScoreDao;
import com.ibb.model.score.ProScore;
import com.ibb.service.score.IProScoreService;
@Service
public class ProScoreService extends BaseService<ProScore, Integer> implements IProScoreService{
	private IProScoreDao proScoreDao;
	@Autowired
	@Qualifier("proScoreDao")
	@Override
	public void setBaseDao(IBaseDao<ProScore, Integer> baseDao){
		this.baseDao=baseDao;
		this.proScoreDao=(IProScoreDao)baseDao;
	}
	/**
	 * 通过所得积分方法
	 */
	public void addUpScore(Integer userId,Integer score){
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("userId", userId));
		List<ProScore> list=this.queryListByCondition(query);
		ProScore proScore=null;
		if(list!=null&&list.size()>0){
			proScore=list.get(0);
		}
		if(proScore!=null){
			//设置保存积分
			proScore.setScore(proScore.getScore()+score);
			//更新对象
			this.update(proScore);
		}
		
	}
	
}
