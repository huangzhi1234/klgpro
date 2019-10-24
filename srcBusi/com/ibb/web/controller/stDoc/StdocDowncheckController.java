package com.ibb.web.controller.stDoc;

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
import com.ibb.model.stDoc.StdocDowncheck;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.service.score.IProScoreService;
import com.ibb.service.stDoc.IStDocService;
import com.ibb.service.stDoc.IStdocDowncheckService;
import com.ibb.sys.model.UserBean;

@Controller
public class StdocDowncheckController implements ServletContextAware{
	@Autowired
	private IStdocDowncheckService stdocDowncheckService;
	@Autowired
	private IStDocService stDocService;
	@Autowired
	private IProScoreService proScoreService;
	@Autowired
	private IProScoreRuleService proScoreRuleService;

	@SuppressWarnings("unused")
	private ServletContext servletContext;
	@Autowired
    @Qualifier("hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	//开发
	@RequestMapping(value = "/stdocDowncheck")
	public String toPatchInfoPage() {
		return "stDoc/stdocDowncheck";
	}
	//实施
	@RequestMapping(value = "/stdocDowncheck2")
	public String toPatchInfoPage2() {
		return "stDoc/stdocDowncheck2";
	}
	//经典案例
	@RequestMapping(value = "/stdocDowncheck4")
	public String toPatchInfoPage4() {
		return "case/classicCaseDowncheck";
	}
	
	/**
	 * 根据checkId查询标准文档 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/getFileNameById.json", method = RequestMethod.POST)
	@ResponseBody
	public StdocInfo getFileNameById(HttpServletRequest request){
		StdocInfo stdocInfo=null;
		if(request.getParameter("stdocId")!=null){
			Integer stdocId=Integer.parseInt(request.getParameter("stdocId"));
			stdocInfo= stDocService.get(stdocId);
			stdocInfo.setFileName(stdocInfo.getFileName().substring(0,stdocInfo.getFileName().indexOf(".")));
		}
		return stdocInfo;
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询  开发
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocDowncheck> list(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 1));
		Page<StdocDowncheck> page=stdocDowncheckService.queryListByCondition(query,order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocDowncheck>(page);
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询  实施
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/list2.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocDowncheck> list2(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 2));
		Page<StdocDowncheck> page=stdocDowncheckService.queryListByCondition(query,order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocDowncheck>(page);
	}

	/**
	 * 查询申请列表 分页查询+模糊查询  经典案例
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/list4.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocDowncheck> list4(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 4));
		Page<StdocDowncheck> page=stdocDowncheckService.queryListByCondition(query,order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocDowncheck>(page);
	}
	
	/**
	 * 进行单个审核通过
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/agree", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel agree(HttpServletRequest request) {
			Integer checkId=Integer.parseInt(request.getParameter("checkId"));
			Integer score=proScoreRuleService.get(2).getScore();
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			StdocDowncheck stdocDowncheck=stdocDowncheckService.get(checkId);
			if(stdocDowncheck!=null){
				stdocDowncheck.setIsOk(1);
				stdocDowncheck.setState(0);
				stdocDowncheck.setAgreeOper(userBean.getUserId());
				stdocDowncheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
				stdocDowncheckService.update(stdocDowncheck);
				proScoreService.addUpScore(stdocDowncheck.getOper(),score);//获得积分
				return new CommonJsonModel(true,"操作成功");
			}
		return new CommonJsonModel(false,"操作失败！");
	}
	
	/**
	 * 进行单个申请拒绝
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/refuse", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel refuse(HttpServletRequest request) {
			Integer checkId=Integer.parseInt(request.getParameter("checkId"));
			String remark=request.getParameter("remark");
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			StdocDowncheck stdocDowncheck=stdocDowncheckService.get(checkId);
			if(stdocDowncheck!=null){
				stdocDowncheck.setIsOk(2);
				stdocDowncheck.setState(0);
				stdocDowncheck.setAgreeOper(userBean.getUserId());
				stdocDowncheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
				if(StringUtils.isNotEmpty(remark)){
					stdocDowncheck.setRemark(remark);
				}
				stdocDowncheckService.update(stdocDowncheck);
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
	@RequestMapping(value = "/stdocDowncheck/agreeMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel agreeMore(String checkIdArr, HttpServletRequest request) {
		Integer score=proScoreRuleService.get(2).getScore();
		if(checkIdArr!=null){
			String[] checkIds = checkIdArr.split("#");
			try {
				for (String checkId : checkIds) {
					if (StringUtils.isNotEmpty(checkId)) {
						StdocDowncheck stdocDowncheck=stdocDowncheckService.get(Integer.valueOf(checkId));
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						stdocDowncheck.setIsOk(1);
						stdocDowncheck.setState(0);
						stdocDowncheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						stdocDowncheck.setAgreeOper(userBean.getUserId());
						stdocDowncheckService.update(stdocDowncheck);
						proScoreService.addUpScore(stdocDowncheck.getOper(),score);//获得积分
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
	@RequestMapping(value = "/stdocDowncheck/refuseMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel refuseMore(String checkIdArr, HttpServletRequest request) {
		if(checkIdArr!=null){
			String[] checkIds = checkIdArr.split("#");
			try {
				for (String checkId : checkIds) {
					if (StringUtils.isNotEmpty(checkId)) {
						StdocDowncheck stdocDowncheck=stdocDowncheckService.get(Integer.valueOf(checkId));
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						stdocDowncheck.setIsOk(2);
						stdocDowncheck.setState(0);
						stdocDowncheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						stdocDowncheck.setAgreeOper(userBean.getUserId());
						stdocDowncheckService.update(stdocDowncheck);
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
	 * 进行单个申请  开发
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/getIsOk", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel getIsOk(HttpServletRequest request) {
		if(StringUtils.isNotEmpty(request.getParameter("stdocId"))){
			Integer stdocId=Integer.parseInt(request.getParameter("stdocId"));
			String cause=request.getParameter("cause");
			StdocDowncheck stdocDowncheck=new StdocDowncheck();
			stdocDowncheck.setStdocId(stdocId);
			stdocDowncheck.setState(1);
			stdocDowncheck.setIsOk(0);
			stdocDowncheck.setType(1);
			stdocDowncheck.setCause(cause);
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			stdocDowncheck.setOper(userBean.getUserId());
			stdocDowncheck.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
			stdocDowncheckService.save(stdocDowncheck);
			return new CommonJsonModel(true,"操作失败");
		}
		return new CommonJsonModel(false,"提交申请失败！");
	}
	
	/**
	 * 进行单个申请  实施
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/getIsOk2", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel getIsOk2(HttpServletRequest request) {
		if(StringUtils.isNotEmpty(request.getParameter("stdocId"))){
			Integer stdocId=Integer.parseInt(request.getParameter("stdocId"));
			String cause=request.getParameter("cause");
			StdocDowncheck stdocDowncheck=new StdocDowncheck();
			stdocDowncheck.setStdocId(stdocId);
			stdocDowncheck.setState(1);
			stdocDowncheck.setIsOk(0);
			stdocDowncheck.setType(2);
			stdocDowncheck.setCause(cause);
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			stdocDowncheck.setOper(userBean.getUserId());
			stdocDowncheck.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
			stdocDowncheckService.save(stdocDowncheck);
			return new CommonJsonModel(true,"操作失败");
		}
		return new CommonJsonModel(false,"提交申请失败！");
	}
	
	/**
	 * 进行单个申请  经典案例
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/getIsOk4", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel getIsOk4(HttpServletRequest request) {
		if(StringUtils.isNotEmpty(request.getParameter("stdocId"))){
			Integer stdocId=Integer.parseInt(request.getParameter("stdocId"));
			String cause=request.getParameter("cause");
			StdocDowncheck stdocDowncheck=new StdocDowncheck();
			stdocDowncheck.setStdocId(stdocId);
			stdocDowncheck.setState(1);
			stdocDowncheck.setIsOk(0);
			stdocDowncheck.setType(4);
			stdocDowncheck.setCause(cause);
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			stdocDowncheck.setOper(userBean.getUserId());
			stdocDowncheck.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
			stdocDowncheckService.save(stdocDowncheck);
			return new CommonJsonModel(true,"操作失败");
		}
		return new CommonJsonModel(false,"提交申请失败！");
	}
	
	
	/**
	 * 通过stdocId获取到审核表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/getStateByPatId", method = RequestMethod.POST)
	@ResponseBody
	public StdocDowncheck getStateByPatId(HttpServletRequest request) {
		
		Integer stdocId=Integer.parseInt(request.getParameter("stdocId"));
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("stdocId", stdocId));
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");//当前用户登录的对象
		query.add(Restrictions.eq("oper", userBean.getUserId()));
		List<StdocDowncheck> list=stdocDowncheckService.queryListByCondition(query);
		StdocDowncheck stdocDowncheck=null;
		if(list!=null&&list.size()!=0){
			stdocDowncheck=list.get(0);
		}else{
		}
		return stdocDowncheck;
	}
	
	
	
	/**
	 * 检查是否包含已申请下载信息
	 * @param idArr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocDowncheck/isApply.json", method = RequestMethod.POST)
	@ResponseBody
	public Integer isApply(String idArr,HttpServletRequest request){
		if(null == idArr){
			return 0;
		}
		String[] stdocIds = idArr.split(",");
		for (int i = 0; i < stdocIds.length; i++) {
			/*--判断是否包含申请过patId----*/
			OrderBy order = new OrderBy();
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("stdocId", Integer.parseInt(stdocIds[i])));
			order.add(Order.desc("time"));
			List<StdocDowncheck> list=stdocDowncheckService.queryListByCondition(query, order);
			Integer isOk=0;
			Integer state=0;
			if(list!=null&&list.size()>0){
				StdocDowncheck checkLoad=list.get(0);
				isOk=checkLoad.getIsOk();
				state=checkLoad.getState();
			}
			if(isOk==1||state==1){
				return 0;
			}
		}
		return 1;
	}
	
	
}
