package com.ibb.service.score.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.impl.BaseService;
import com.ibb.dao.score.IProScoreRuleDao;
import com.ibb.model.patch.CheckLoad;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.score.ProScore;
import com.ibb.model.score.ProScoreRule;
import com.ibb.model.stDoc.StdocDowncheck;
import com.ibb.model.stDoc.StdocUploadcheck;
import com.ibb.service.patch.ICheckLoadService;
import com.ibb.service.patch.IUploadCheckService;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.service.score.IProScoreService;
import com.ibb.service.stDoc.IStdocDowncheckService;
import com.ibb.service.stDoc.IStdocUploadcheckService;

@Service
public class ProScoreRuleService extends BaseService<ProScoreRule, Integer> implements IProScoreRuleService{

	@Autowired
	private IUploadCheckService uploadCheckService;
	@Autowired
	private IStdocUploadcheckService stdocUploadcheckService;
	@Autowired
	private IStdocDowncheckService stdocDowncheckService;
	@Autowired
	private ICheckLoadService checkLoadService;
	@Autowired
	private IProScoreService proScoreService;
	
	private IProScoreRuleDao proScoreRuleDao;
	@Autowired
	@Qualifier("proScoreRuleDao")
	@Override
	public void setBaseDao(IBaseDao<ProScoreRule,Integer> baseDao){
		this.baseDao=baseDao;
		this.proScoreRuleDao=(IProScoreRuleDao)baseDao;
	}
	//计算个人上传的方法
		public Integer getUp(Integer userId){
			Integer upScore=0;//上传总积分
			Integer patScore=0;//成果上传总积分
			Integer docScore=0;//文档上传总积分
			int score=this.get(1).getScore();//单个上传类型所得积分
			//成果部分
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("oper", userId));
			query.add(Restrictions.eq("isOk", 1));
			List<UploadCheck> list=uploadCheckService.queryListByCondition(query);
			if(list!=null&&list.size()>0){
				int num=list.size();//成果上传成功的个数
				patScore=num*score;
				upScore+=patScore;
			}
			//文档部分
			List<StdocUploadcheck> list1=stdocUploadcheckService.queryListByCondition(query);
			if(list1!=null&&list1.size()>0){
				int num=list1.size();//成果上传成功的个数
				docScore=num*score;
				upScore+=docScore;
			}
			return upScore;
		}

		
		//计算下载积分的方法
		public Integer getDown(Integer userId){
			Integer downScore=0;//下载总积分
			Integer patScore=0;//成果下载总积分
			Integer docScore=0;//文档下载总积分
			int score=this.get(2).getScore();//单个下载类型所得积分
			//成果部分
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("oper", userId));
			query.add(Restrictions.eq("isOk", 1));
			List<CheckLoad> list=checkLoadService.queryListByCondition(query);
			if(list!=null&&list.size()>0){
				int num=list.size();//成果上传成功的个数
				patScore=num*score;
				downScore+=patScore;
			}
			//文档部分
			List<StdocDowncheck> list1=stdocDowncheckService.queryListByCondition(query);
			if(list1!=null&&list1.size()>0){
				int num=list1.size();//成果上传成功的个数
				docScore=num*score;
				downScore+=docScore;
			}
			return downScore;
		}
		
		public void getAllScore(){
			/*-------------更新总积分------------------*/
			//1.查询出所有积分表数据list
			Integer score=0;//总积分
			/*int score1=proScoreRuleService.get(2).getScore();//单个上传类型所得积分
	*/				ConditionQuery query = new ConditionQuery();
			List<ProScore> list=proScoreService.queryListByCondition(query);
			if(list!=null&&list.size()>0){
				for (ProScore proScore : list) {//逐个对象进行更新
					Integer userId=proScore.getUserId();//取得当前对象的userid，用于下面方法的参数传递
					score=this.getUp(userId)+this.getDown(userId);//计算总积分
					proScore.setScore(score);//保存最新总积分在对象中
					proScoreService.update(proScore);//更新对象
				}
			}
		}
		
		
		
		
}
