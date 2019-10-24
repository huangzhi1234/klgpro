package com.ibb.web.controller.patch;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.DateUtil;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.patch.CheckLoad;
import com.ibb.model.patch.PatchInfo;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.score.ProScore;
import com.ibb.service.patch.ICheckLoadService;
import com.ibb.service.patch.IPatchInfoService;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.service.score.IProScoreService;
import com.ibb.sys.model.UserBean;

@Controller
public class CheckLoadController implements ServletContextAware{
	@Autowired
	private ICheckLoadService checkLoadService;
	@Autowired
	private IPatchInfoService patchInfoService;
	@Autowired
	private IProScoreRuleService proScoreRuleService;
	@Autowired
	private IProScoreService proScoreService;
	@SuppressWarnings("unused")
	private ServletContext servletContext;
	@Autowired
	@Qualifier("hibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Override
	public void setServletContext(ServletContext arg0) {
		this.servletContext = arg0;
	}
	
	@RequestMapping(value = "/checkLoad")
	public String toPatchInfoPage() {
		return "patch/checkLoad";
	}
	
	@RequestMapping(value = "/scheckLoad")
	public String tosPatchInfoPage() {
		return "patch/scheckLoad";
	}
	
	/**
	 * 查询研发申请列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<CheckLoad> list(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
		
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 1));//研发
		Page<CheckLoad> page=checkLoadService.queryListByCondition(query,order, pn, pageSize);
		
		return new EasyUIGridJsonModel<CheckLoad>(page);
	}
	
	/**
	 * 查询实施申请列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/list2.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<CheckLoad> list2(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 2));//实施
		Page<CheckLoad> page=checkLoadService.queryListByCondition(query,order, pn, pageSize);
		
		return new EasyUIGridJsonModel<CheckLoad>(page);
	}
	
	
	/**
	 * 查询未申请列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/listByNoApply.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<CheckLoad> listByNoApply(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		Page<CheckLoad> page=checkLoadService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<CheckLoad>(page);
	}
	
	
	/**
	 * 根据checkId查询成果 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/getPatInfoById.json", method = RequestMethod.POST)
	@ResponseBody
	public PatchInfo getPatNameById(HttpServletRequest request){
		PatchInfo patchInfo=null;
		if(request.getParameter("patId")!=null){
			Integer patId=Integer.parseInt(request.getParameter("patId"));
			patchInfo= patchInfoService.get(patId);
			if(patchInfo.getFileName()!=null)
			patchInfo.setFileName(patchInfo.getFileName().substring(0,patchInfo.getFileName().indexOf(".")));
		}
		return patchInfo;
	}
	
	@RequestMapping(value = "/checkLoad/getCheckLoadById.json", method = RequestMethod.POST)
	@ResponseBody
	public CheckLoad getCheckLoadById(HttpServletRequest request){
		CheckLoad checkLoad=null;
		if(StringUtils.isNotEmpty(request.getParameter("patId"))){
			Integer patId=Integer.parseInt(request.getParameter("patId"));
			OrderBy order = new OrderBy();
			order.add(Order.desc("time"));
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("patId", patId));
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");//当前用户登录的对象
			query.add(Restrictions.eq("oper", userBean.getUserId()));
			List<CheckLoad> list=checkLoadService.queryListByCondition(query, order);
			if(list!=null&&list.size()!=0){
				checkLoad=list.get(0);
			}
		}
		return checkLoad;
	}
	
	/**
	 * 进行单个审核通过
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/agree", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel agree(HttpServletRequest request) {
			Integer score=proScoreRuleService.get(2).getScore();
			Integer checkId=Integer.parseInt(request.getParameter("checkId"));
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			CheckLoad checkLoad=checkLoadService.get(checkId);
			if(checkLoad!=null){
				checkLoad.setIsOk(1);
				checkLoad.setState(0);
				checkLoad.setAgreeOper(userBean.getUserId());
				checkLoad.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
				checkLoadService.update(checkLoad);
				proScoreService.addUpScore(checkLoad.getOper(),score);//获得积分
				return new CommonJsonModel(true,"操作成功");
			}
		return new CommonJsonModel(false,"操作失败！");
	}
	
	
	
	
	
	/**
	 * 进行单个申请拒绝
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/refuse", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel refuse(HttpServletRequest request) {
		
			Integer checkId=Integer.parseInt(request.getParameter("checkId"));
			String remark=request.getParameter("remark");
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			CheckLoad checkLoad=checkLoadService.get(checkId);
			if(checkLoad!=null){
				checkLoad.setIsOk(2);
				checkLoad.setState(0);
				checkLoad.setAgreeOper(userBean.getUserId());
				checkLoad.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
				if(StringUtils.isNotEmpty(remark)){
					checkLoad.setRemark(remark);
				}
				checkLoadService.update(checkLoad);
				return new CommonJsonModel(true,"操作成功");
			}
		return new CommonJsonModel(false,"操作失败！");
	}
	
	
	/**
	 * 进行批量申请通过
	 * @param checkIdArr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/agreeMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel agreeMore(String checkIdArr, HttpServletRequest request) {
		Integer score=proScoreRuleService.get(2).getScore();
		if(checkIdArr!=null){
			String[] checkIds = checkIdArr.split("#");
			try {
				for (String checkId : checkIds) {
					if (StringUtils.isNotEmpty(checkId)) {
						CheckLoad checkLoad=checkLoadService.get(Integer.valueOf(checkId));
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						checkLoad.setIsOk(1);
						checkLoad.setState(0);
						checkLoad.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						checkLoad.setAgreeOper(userBean.getUserId());
						checkLoadService.update(checkLoad);
						proScoreService.addUpScore(checkLoad.getOper(),score);//获得积分
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				return new CommonJsonModel(false,"操作失败！");
			}
			return new CommonJsonModel(true,"操作成功！");
		}
		return new CommonJsonModel(false,"操作失败！");
	}
	/**
	 * 进行批量拒绝
	 * @param checkIdArr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/refuseMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel refuseMore(String checkIdArr, HttpServletRequest request) {
		if(checkIdArr!=null){
			String[] checkIds = checkIdArr.split("#");
			try {
				for (String checkId : checkIds) {
					if (StringUtils.isNotEmpty(checkId)) {
						CheckLoad checkLoad=checkLoadService.get(Integer.valueOf(checkId));
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						checkLoad.setIsOk(2);
						checkLoad.setState(0);
						checkLoad.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						checkLoad.setAgreeOper(userBean.getUserId());
						checkLoadService.update(checkLoad);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				return new CommonJsonModel(false,"操作失败！");
			}
			return new CommonJsonModel(true,"操作成功！");
		}
		return new CommonJsonModel(false,"操作失败！");
	}
	
	
	/**
	 * 通过patId获取到审核表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/getStateByPatId", method = RequestMethod.POST)
	@ResponseBody
	public CheckLoad getStateByPatId(HttpServletRequest request) {
		
		Integer patId=Integer.parseInt(request.getParameter("patId"));
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("patId", patId));
		List<CheckLoad> list=checkLoadService.queryListByCondition(query);
		CheckLoad checkLoad=null;
		if(list!=null&&list.size()!=0){
			checkLoad=list.get(0);
		}else{
		}
		return checkLoad;
	}
	
	/**
	 * 进行单个研发申请
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/getIsOk", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel getIsOk(HttpServletRequest request) {
		String cause = request.getParameter("cause");
		if(StringUtils.isNotEmpty(request.getParameter("patId"))){
			Integer patId=Integer.parseInt(request.getParameter("patId"));
			CheckLoad checkLoad=new CheckLoad();
			checkLoad.setPatId(patId);
			checkLoad.setState(1);
			checkLoad.setIsOk(0);
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			checkLoad.setOper(userBean.getUserId());
			checkLoad.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
			checkLoad.setType(1);//研发
			checkLoad.setCause(cause);//申请下载原因或用途
			checkLoadService.save(checkLoad);
			return new CommonJsonModel(true,"操作成功！");
		}
		return new CommonJsonModel(false,"提交申请失败！");
	}
	
	/**
	 * 进行单个实施申请
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/getIsOk2", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel getIsOk2(HttpServletRequest request) {
		String cause = request.getParameter("cause");
		if(StringUtils.isNotEmpty(request.getParameter("patId"))){
			Integer patId=Integer.parseInt(request.getParameter("patId"));
			CheckLoad checkLoad=new CheckLoad();
			checkLoad.setPatId(patId);
			checkLoad.setState(1);
			checkLoad.setIsOk(0);
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			checkLoad.setOper(userBean.getUserId());
			checkLoad.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
			checkLoad.setType(2);//实施
			checkLoad.setCause(cause);//申请下载原因或用途
			checkLoadService.save(checkLoad);
			return new CommonJsonModel(true,"操作成功！");
		}
		return new CommonJsonModel(false,"提交申请失败！");
	}
	
	/**
	 * 进行批量申请下载（研发）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/applyMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel applyMore(String patchIdArr,HttpServletRequest request) {
		if(patchIdArr!=null){
			String[] patIds = patchIdArr.split("#");
			//获取成果id和cause，同时判断申请数据中有无包含已申请过的数据		
			int patId=0;
			String cause="";
			for (int i = 0; i < patIds.length; i++) {
				if(i%2==0){
					patId=Integer.parseInt(patIds[i]);
				}else{
					cause=patIds[i];
					CheckLoad checkLoad=new CheckLoad();
					checkLoad.setPatId(patId);
					checkLoad.setState(1);
					checkLoad.setIsOk(0);
					checkLoad.setType(1);//研发
					checkLoad.setCause(cause);//申请下载的原因或用途
					UserBean userBean = (UserBean) request.getSession().getAttribute("user");
					checkLoad.setOper(userBean.getUserId());
					checkLoad.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					checkLoadService.save(checkLoad);
				}
				
			}
		}
		return new CommonJsonModel(true,"操作成功");
	}
	
	//检查是否包含已申请下载信息
	@RequestMapping(value = "/checkLoad/isApply.json", method = RequestMethod.POST)
	@ResponseBody
	public Integer isApply(String idArr,HttpServletRequest request){
		if(null == idArr){
			return 0;
		}
		String[] patIds = idArr.split("aaa");
		for (int i = 0; i < patIds.length; i++) {
			/*--判断是否包含申请过patId----*/
			OrderBy order = new OrderBy();
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("patId", Integer.parseInt(patIds[i])));
			order.add(Order.desc("time"));
			List<CheckLoad> list=checkLoadService.queryListByCondition(query, order);
			Integer isOk=0;
			Integer state=0;
			if(list!=null&&list.size()>0){
				CheckLoad checkLoad=list.get(0);
				isOk=checkLoad.getIsOk();
				state=checkLoad.getState();
			}
			if(isOk==1||state==1){
				return 0;
			}
		}
		return 1;
	}
		


	/**
	 * 进行批量申请下载（实施）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLoad/applyMore2.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel applyMore2(String patchIdArr,HttpServletRequest request) {
		if(patchIdArr!=null){
			String[] patIds = patchIdArr.split("#");
			for (String patId : patIds) {
				/*--判断是否包含申请过patId----*/
				ConditionQuery query = new ConditionQuery();
				query.add(Restrictions.eq("patId", Integer.parseInt(patId)));
				OrderBy order = new OrderBy();
				order.add(Order.desc("time"));
				List<CheckLoad> list=checkLoadService.queryListByCondition(query, order);
				Integer isOk=0;
				Integer state=0;
				if(list!=null&&list.size()>0){
					CheckLoad checkLoad=list.get(0);
					isOk=checkLoad.getIsOk();
					state=checkLoad.getState();
				}
				if(isOk==1||state==1){
					return new CommonJsonModel(false,"请选择未申请过的数据");
				}
				/*--------------*/
				CheckLoad checkLoad=new CheckLoad();
				checkLoad.setPatId(Integer.parseInt(patId));
				checkLoad.setState(1);
				checkLoad.setIsOk(0);
				checkLoad.setType(2);//实施
				UserBean userBean = (UserBean) request.getSession().getAttribute("user");
				checkLoad.setOper(userBean.getUserId());
				checkLoad.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
				checkLoadService.save(checkLoad);
			}
			return new CommonJsonModel(true,"操作成功");
		}
		return new CommonJsonModel(false,"操作失败");
	}

}
