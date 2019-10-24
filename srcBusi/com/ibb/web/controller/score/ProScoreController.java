package com.ibb.web.controller.score;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.StringUtils;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.depart.DepartDicInfo;
import com.ibb.model.patch.CheckLoad;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.score.ProScore;
import com.ibb.model.score.ProScoreRule;
import com.ibb.model.stDoc.StdocDowncheck;
import com.ibb.model.stDoc.StdocUploadcheck;
import com.ibb.service.depart.IDepartDicService;
import com.ibb.service.patch.ICheckLoadService;
import com.ibb.service.patch.IUploadCheckService;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.service.score.IProScoreService;
import com.ibb.service.stDoc.IStdocDowncheckService;
import com.ibb.service.stDoc.IStdocUploadcheckService;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.service.IUserService;

@Controller
public class ProScoreController implements ServletContextAware{
	@Autowired
	private IProScoreService proScoreService;
	@Autowired
	private IDepartDicService departDicService;
	@Autowired
	private IProScoreRuleService proScoreRuleService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IUploadCheckService uploadCheckService;
	@Autowired
	private ICheckLoadService checkLoadService;
	@Autowired
	private IStdocDowncheckService stdocDowncheckService;
	@Autowired
	private IStdocUploadcheckService stdocUploadcheckService;
	@Autowired
	private ServletContext servletContext;
	@Autowired
    @Qualifier("hibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	/**
	 * 实现了ServletContextAware接口，就可以通过这样获得servletContext
	 */
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	@RequestMapping(value = "/proScore")
	public String toCompanyInfoPage() {
		return "score/proScore";
	}
	/**
	 * 查询信息列表	根据项目编号查询+分页查询
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proScore/ListByPerson.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProScore> querListByPerson(DepartDicInfo depart,UserBean userBean,ProScore proScore, HttpServletRequest request){
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("score"));
		
		//部门模糊查询
		//1.拿到部门编号dicNum
		if(StringUtils.isNotEmpty(depart.getDicNum())){
			//2.拿到部门id：departId
			ConditionQuery query2=new ConditionQuery();
			query2.add(Restrictions.eq("dicNum", depart.getDicNum()));
			java.util.List<DepartDicInfo> list=departDicService.queryListByCondition(query2);
			if(list!=null&&list.size()>0){
				Integer departId=list.get(0).getDepartId();
				//3.拿到userId
				ConditionQuery query3=new ConditionQuery();
				query3.add(Restrictions.eq("departId", departId));
				List<UserBean> list2=userService.queryListByCondition(query3);
				if(list2!=null&&list2.size()>0){
					ArrayList<Integer> arr=new ArrayList<Integer>();
					for (UserBean userBean2 : list2) {
						arr.add(userBean2.getUserId());
					}
					query.add(Restrictions.in("userId", arr));
				}
			}
		}
		
		
		
		
		
		//姓名模糊查询
		if (StringUtils.isNotEmpty(userBean.getUserName())) {
			ConditionQuery query1 = new ConditionQuery();
			query1.add(Restrictions.like("userName", "%"+userBean.getUserName().trim()+"%"));
			java.util.List<UserBean> list=userService.queryListByCondition(query1);
			ArrayList<Integer> arr=new ArrayList<Integer>();
			if(list!=null&&list.size()>0){
				for (UserBean userBean1 : list) {
					arr.add(userBean1.getUserId());
				}
				query.add(Restrictions.in("userId", arr));
			}else{
				query.add(Restrictions.eq("userId", 0));
			}
		}
		//分数模糊查询
		if (proScore.getScore()!=null) {
			query.add(Restrictions.ge("score", proScore.getScore()));
		}
		if (proScore.getScoreId()!=null) {
			query.add(Restrictions.le("score", proScore.getScoreId()));
		}
		Page<ProScore> page=proScoreService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<ProScore>(page);
	}
	
	
	/**
	 * 通过userId获得部门对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proScore/getdepart.json", method = RequestMethod.POST)//处理请求地址映射的注解
	@ResponseBody
	public DepartDicInfo getdepart(HttpServletRequest request){
		String userId=request.getParameter("userId");
		DepartDicInfo depart=null;//令depart为空
		if(StringUtils.isNotEmpty(userId)){
			UserBean user=userService.get(Integer.parseInt(userId));
			Integer departId=0;//
			if(user!=null){//
				departId=user.getDepartId();
			}
			if(departId!=0){
				depart=departDicService.get(departId);
			}
		}
		return depart;
	}
	@RequestMapping(value = "/proScore/getUserBean.json", method = RequestMethod.POST)//处理请求地址映射的注解
	@ResponseBody
	public UserBean getUserBean(HttpServletRequest request){
		String userId=request.getParameter("userId");
		UserBean userBean=null;//令depart为空
		if(StringUtils.isNotEmpty(userId)){
			userBean=userService.get(Integer.parseInt(userId));
		}
		return userBean;
	}
	
	/**
	 * 通过用户登录的userbean获得该用户的积分表对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proScore/getProScore.json", method = RequestMethod.POST)//处理请求地址映射的注解
	@ResponseBody
	public ProScore getProScore(HttpServletRequest request){
		ProScore proScore=null;
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");//当前用户登录的对象
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("userId", userBean.getUserId()));
		java.util.List<ProScore> list=proScoreService.queryListByCondition(query);
		if(list!=null&&list.size()>0){
			proScore=list.get(0);
		}
		return proScore;
	}
	
	@RequestMapping(value = "/proScore/getScoreRule.json", method = RequestMethod.POST)//处理请求地址映射的注解
	@ResponseBody
	public ProScoreRule getScoreRule(HttpServletRequest request){
		
		ProScoreRule proScoreRule=null;
		if(StringUtils.isNotEmpty(request.getParameter("ruleId"))){
			Integer ruleId=Integer.parseInt(request.getParameter("ruleId"));
			proScoreRule=proScoreRuleService.get(ruleId);
		}
		return proScoreRule;
	}
	/**
	 * 计算个人上传部分所得积分
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proScore/getUpScore.json", method = RequestMethod.POST)//处理请求地址映射的注解
	@ResponseBody
	public Integer getUpScore(HttpServletRequest request){
		
		Integer upScore=0;//上传总积分
		if(StringUtils.isNotEmpty(request.getParameter("userId"))){
			Integer userId=Integer.parseInt(request.getParameter("userId"));
			upScore=proScoreRuleService.getUp(userId);
		}
		return upScore;
	}
	//计算个人上传的方法
	/*public Integer getUp(Integer userId){
		Integer upScore=0;//上传总积分
		Integer patScore=0;//成果上传总积分
		Integer docScore=0;//文档上传总积分
		int score=proScoreRuleService.get(1).getScore();//单个上传类型所得积分
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
		ConditionQuery query1 = new ConditionQuery();
		query1.add(Restrictions.eq("oper", userId));
		query1.add(Restrictions.eq("isOk", 1));
		List<StdocUploadcheck> list1=stdocUploadcheckService.queryListByCondition(query);
		if(list1!=null&&list1.size()>0){
			int num=list.size();//成果上传成功的个数
			docScore=num*score;
			upScore+=docScore;
		}
		return upScore;
	}
	*/
	
	/**
	 * 计算个人下载部分所得积分
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proScore/getDownScore.json", method = RequestMethod.POST)//处理请求地址映射的注解
	@ResponseBody
	public Integer getDownScore(HttpServletRequest request){
		Integer downScore=0;//下载总积分
		Integer patScore=0;//成果下载总积分
		Integer docScore=0;//文档下载总积分
		if(StringUtils.isNotEmpty(request.getParameter("userId"))){
			Integer userId=Integer.parseInt(request.getParameter("userId"));
			downScore=proScoreRuleService.getDown(userId);
		}
		return downScore;
	}
	//计算下载积分的方法
	/*public Integer getDown(Integer userId){
		Integer downScore=0;//下载总积分
		Integer patScore=0;//成果下载总积分
		Integer docScore=0;//文档下载总积分
		int score=proScoreRuleService.get(2).getScore();//单个下载类型所得积分
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
		ConditionQuery query1 = new ConditionQuery();
		query1.add(Restrictions.eq("oper", userId));
		query1.add(Restrictions.eq("isOk", 1));
		List<StdocDowncheck> list1=stdocDowncheckService.queryListByCondition(query);
		if(list1!=null&&list1.size()>0){
			int num=list.size();//成果上传成功的个数
			docScore=num*score;
			downScore+=docScore;
		}
		return downScore;
	}
	*/
	
	/**
	 * 计算当前排名
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proScore/getCount.json", method = RequestMethod.POST)
	@ResponseBody
	public Integer getCount(HttpServletRequest request){
		int count=0;
		if(StringUtils.isNotEmpty(request.getParameter("scoreId"))){
			Integer scoreId=Integer.parseInt(request.getParameter("scoreId"));
			ConditionQuery query = new ConditionQuery();
			OrderBy order = new OrderBy();
			order.add(Order.desc("score"));
			List<ProScore> list=proScoreService.queryListByCondition(query,order);//查询出按分数排名的list集合
			for (int i = 0; i < list.size(); i++) {
				int id=list.get(i).getScoreId();
				if(id==scoreId){//找到对应id
					count=i+1;
					break;
				}
			}
		}
		return count;
	}
	
	/**
	 * 我的排名
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proScore/getMyCount.json", method = RequestMethod.POST)
	@ResponseBody
	public Integer getMyCount(HttpServletRequest request){
		int count=0;
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");//当前用户登录的对象
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("userId", userBean.getUserId()));
		OrderBy order = new OrderBy();
		order.add(Order.desc("score"));
		List<ProScore> list=proScoreService.queryListByCondition(query);
		for (int i = 0; i < list.size(); i++) {
			int userId=list.get(i).getUserId();
			if(userId==userBean.getUserId()){//找到对应id
				count=i+1;
				break;
			}
		}
		return count;
	}
	
	/**
	 * 计算个人在部门中的排名
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proScore/countInDepart.json", method = RequestMethod.POST)
	@ResponseBody
	public Integer countInDepart(HttpServletRequest request){
		int count=0;
		if(StringUtils.isNotEmpty(request.getParameter("scoreId"))){
			Integer scoreId=Integer.parseInt(request.getParameter("scoreId"));
			//拿到userId所在部门的所有userId
			Integer userId=proScoreService.get(scoreId).getUserId();//拿到userId
			Integer departId=userService.get(userId).getDepartId();//拿到所在部门编号
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("departId", departId));
			List<UserBean> userArr=userService.queryListByCondition(query);
			ArrayList<Integer> arr=new ArrayList<Integer>();
			if(userArr!=null&&userArr.size()>0){
				for (UserBean userBean : userArr) {
					arr.add(userBean.getUserId());//将所在部门的成员编号加载在数组里
				}
			}
			
			ConditionQuery query1 = new ConditionQuery();
			query1.add(Restrictions.in("userId", arr));//条件：过滤为所在部门的所有积分表数据
			OrderBy order = new OrderBy();
			order.add(Order.desc("score"));
			List<ProScore> list=proScoreService.queryListByCondition(query1,order);//查询出按分数排名的list集合
			for (int i = 0; i < list.size(); i++) {
				int id=list.get(i).getScoreId();
				if(id==scoreId){//找到对应id
					count=i+1;
					break;
				}
			}
		}
		return count;
	}
	
	
	
}
