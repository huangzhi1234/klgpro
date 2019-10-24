package com.ibb.web.controller.stDoc;

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
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.model.stDoc.StdocUploadcheck;
import com.ibb.service.stDoc.IStDocService;
import com.ibb.service.stDoc.IStdocUploadcheckService;
import com.ibb.sys.model.RoleBean;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.model.UserRoleBean;
import com.ibb.sys.model.UserRolePk;
import com.ibb.sys.service.IRoleService;
import com.ibb.sys.service.IUserRoleService;

@Controller
public class StUploadHistoryController implements ServletContextAware{
	@Autowired
	private IStdocUploadcheckService stdocUploadcheckService;
	@Autowired
	private IStDocService stDocService;
	@SuppressWarnings("unused")
	private ServletContext servletContext;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private IRoleService roleService;
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
	@RequestMapping(value = "/stUpHistory")
	public String toPatchInfoPage() {
		return "stDoc/stUpHistory";
	}
	//实施
	@RequestMapping(value = "/stUpHistory2")
	public String toPatchInfoPage2() {
		return "stDoc/stUpHistory2";
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询  开发和实施
	 * 
	 * @param stdocDowncheck
	 * @param stdocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stUpHistory/list0.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocUploadcheck> list0(StdocInfo stdocInfo,StdocUploadcheck stdocUploadcheck,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		/*---------------审批记录：过滤掉“正在申请状态”的数据-------------------*/
		String stus=request.getParameter("stus");
		if("hasEdit".equals(stus)){
			query.add(Restrictions.eq("state", 0));
		}
		
		/*---------积分详情模块，进行过滤--------------*/
		if(StringUtils.isNotEmpty(request.getParameter("isOk"))&&request.getParameter("isOk")=="1"){
			/*Integer isOk=Integer.parseInt(request.getParameter("isOk"));*/
			query.add(Restrictions.eq("isOk", 1));
		}
		
		
		
		ConditionQuery query1 = new ConditionQuery();
		//文件名称模糊查询
		if(StringUtils.isNotEmpty(stdocInfo.getFileName())){
			query1.add(Restrictions.like("fileName", "%"+stdocInfo.getFileName().trim()+"%"));
		}
		//判断文档类型，若为空则都查出来
		if(StringUtils.isNotEmpty(request.getParameter("doctype"))){
			Integer doctype=Integer.parseInt(request.getParameter("doctype"));
			query1.add(Restrictions.eq("type", doctype));
		}else{
			Integer[] types = new Integer[2];
			types[0]=1;
			types[1]=2;
			//add 文档标识 1 为开发  2为实施  
			query1.add(Restrictions.in("type", types));
		}
				
		List<StdocInfo> list=stDocService.queryListByCondition(query1);
		List<Integer> arr=new ArrayList<Integer>();
		if(list!=null&&list.size()>0){
			for (StdocInfo stdocInfo1 : list) {
				arr.add(stdocInfo1.getStdocId());
			}
			query.add(Restrictions.in("stdocId", arr));
		}else{
			query.add(Restrictions.eq("stdocId", 0));//用于模糊查询结果没有的情况
		}
		
		//申请的起始时间模糊查询
		if(StringUtils.isNotEmpty(stdocUploadcheck.getTime())){
			query.add(Restrictions.like("time", "%"+stdocUploadcheck.getTime().trim()+"%"));
		}
		//申请的截止时间模糊查询
		/*if(StringUtils.isNotEmpty(stdocUploadcheck.getAgreeTime())){
			query.add(Restrictions.le("time", stdocUploadcheck.getAgreeTime()));
		}*/
		//状态
		if (stdocUploadcheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", stdocUploadcheck.getIsOk()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		
		Page<StdocUploadcheck> page=stdocUploadcheckService.queryListByCondition(query, order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocUploadcheck>(page);
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询  开发
	 * 
	 * @param stdocDowncheck
	 * @param stdocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stUpHistory/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocUploadcheck> list(StdocInfo stdocInfo,StdocUploadcheck stdocUploadcheck,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		/*---------------审批记录：过滤掉“正在申请状态”的数据-------------------*/
		String stus=request.getParameter("stus");
		if("hasEdit".equals(stus)){
			query.add(Restrictions.eq("state", 0));
		}
		
		/*---------积分详情模块，进行过滤--------------*/
		if(StringUtils.isNotEmpty(request.getParameter("isOk"))&&request.getParameter("isOk")=="1"){
			/*Integer isOk=Integer.parseInt(request.getParameter("isOk"));*/
			query.add(Restrictions.eq("isOk", 1));
		}
		
		
		
		ConditionQuery query1 = new ConditionQuery();
		//文件名称模糊查询
		if(StringUtils.isNotEmpty(stdocInfo.getFileName())){
			query1.add(Restrictions.like("fileName", "%"+stdocInfo.getFileName().trim()+"%"));
		}
		
		query1.add(Restrictions.eq("type",1));
		List<StdocInfo> list=stDocService.queryListByCondition(query1);
		List<Integer> arr=new ArrayList<Integer>();
		if(list!=null&&list.size()>0){
			for (StdocInfo stdocInfo1 : list) {
				arr.add(stdocInfo1.getStdocId());
			}
			query.add(Restrictions.in("stdocId", arr));
		}else{
			query.add(Restrictions.eq("stdocId", 0));//用于模糊查询结果没有的情况
		}
		
		//申请的起始时间模糊查询
		if(StringUtils.isNotEmpty(stdocUploadcheck.getTime())){
			query.add(Restrictions.like("time", "%"+stdocUploadcheck.getTime().trim()+"%"));
		}
		//申请的截止时间模糊查询
		/*if(StringUtils.isNotEmpty(stdocUploadcheck.getAgreeTime())){
			query.add(Restrictions.le("time", stdocUploadcheck.getAgreeTime()));
		}*/
		//状态
		if (stdocUploadcheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", stdocUploadcheck.getIsOk()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		
		Page<StdocUploadcheck> page=stdocUploadcheckService.queryListByCondition(query, order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocUploadcheck>(page);
	}
	
	
	/**
	 * 查询申请列表 分页查询+模糊查询  实施
	 * 
	 * @param stdocDowncheck
	 * @param stdocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stUpHistory/list2.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocUploadcheck> list2(StdocInfo stdocInfo,StdocUploadcheck stdocUploadcheck,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		/*---------------审批记录：过滤掉“正在申请状态”的数据-------------------*/
		String stus=request.getParameter("stus");
		if("hasEdit".equals(stus)){
			query.add(Restrictions.eq("state", 0));
		}
		
		/*---------积分详情模块，进行过滤--------------*/
		if(StringUtils.isNotEmpty(request.getParameter("isOk"))&&request.getParameter("isOk")=="1"){
			/*Integer isOk=Integer.parseInt(request.getParameter("isOk"));*/
			query.add(Restrictions.eq("isOk", 1));
		}
		
		
		
		ConditionQuery query1 = new ConditionQuery();
		//文件名称模糊查询
		if(StringUtils.isNotEmpty(stdocInfo.getFileName())){
			query1.add(Restrictions.like("fileName", "%"+stdocInfo.getFileName().trim()+"%"));
			
		}
		
		query1.add(Restrictions.eq("type",2));
		List<StdocInfo> list=stDocService.queryListByCondition(query1);
		List<Integer> arr=new ArrayList<Integer>();
		if(list!=null&&list.size()>0){
			for (StdocInfo stdocInfo1 : list) {
				arr.add(stdocInfo1.getStdocId());
			}
			query.add(Restrictions.in("stdocId", arr));
		}else{
			query.add(Restrictions.eq("stdocId", 0));//用于模糊查询结果没有的情况
		}
		
		
		//申请的起始时间模糊查询
		if(StringUtils.isNotEmpty(stdocUploadcheck.getTime())){
			query.add(Restrictions.like("time", "%"+stdocUploadcheck.getTime().trim()+"%"));
		}
		//申请的截止时间模糊查询
		/*if(StringUtils.isNotEmpty(stdocUploadcheck.getAgreeTime())){
			query.add(Restrictions.le("time", stdocUploadcheck.getAgreeTime()));
		}*/
		//状态
		if (stdocUploadcheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", stdocUploadcheck.getIsOk()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		
		Page<StdocUploadcheck> page=stdocUploadcheckService.queryListByCondition(query, order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocUploadcheck>(page);
	}
	
	
	/**
	 * 查询申请列表 分页查询+模糊查询  经典案例
	 * 
	 * @param stdocDowncheck
	 * @param stdocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stUpHistory/list4.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocUploadcheck> list4(StdocInfo stdocInfo,StdocUploadcheck stdocUploadcheck,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		/*---------------审批记录：过滤掉“正在申请状态”的数据-------------------*/
		String stus=request.getParameter("stus");
		if("hasEdit".equals(stus)){
			query.add(Restrictions.eq("state", 0));
		}
		
		/*---------积分详情模块，进行过滤--------------*/
		if(StringUtils.isNotEmpty(request.getParameter("isOk"))&&request.getParameter("isOk")=="1"){
			/*Integer isOk=Integer.parseInt(request.getParameter("isOk"));*/
			query.add(Restrictions.eq("isOk", 1));
		}
		
		
		
		ConditionQuery query1 = new ConditionQuery();
		//文件名称模糊查询
		if(StringUtils.isNotEmpty(stdocInfo.getFileName())){
			query1.add(Restrictions.like("fileName", "%"+stdocInfo.getFileName().trim()+"%"));
			
		}
		
		query1.add(Restrictions.eq("type",4));
		List<StdocInfo> list=stDocService.queryListByCondition(query1);
		List<Integer> arr=new ArrayList<Integer>();
		if(list!=null&&list.size()>0){
			for (StdocInfo stdocInfo1 : list) {
				arr.add(stdocInfo1.getStdocId());
			}
			query.add(Restrictions.in("stdocId", arr));
		}else{
			query.add(Restrictions.eq("stdocId", 0));//用于模糊查询结果没有的情况
		}
		
		
		//申请的起始时间模糊查询
		if(StringUtils.isNotEmpty(stdocUploadcheck.getTime())){
			query.add(Restrictions.like("time", "%"+stdocUploadcheck.getTime().trim()+"%"));
		}
		//申请的截止时间模糊查询
		/*if(StringUtils.isNotEmpty(stdocUploadcheck.getAgreeTime())){
			query.add(Restrictions.le("time", stdocUploadcheck.getAgreeTime()));
		}*/
		//状态
		if (stdocUploadcheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", stdocUploadcheck.getIsOk()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		
		Page<StdocUploadcheck> page=stdocUploadcheckService.queryListByCondition(query, order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocUploadcheck>(page);
	}
	
	
	/**
	 * 根据stdocId查询 作者
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stUpHistory/getAuthorById.json", method = RequestMethod.POST)
	@ResponseBody
	public StdocInfo getAuthorById(HttpServletRequest request){
		StdocInfo stdocInfo=null;
		if(request.getParameter("stdocId")!=null){
			Integer stdocId=Integer.parseInt(request.getParameter("stdocId"));
			stdocInfo= stDocService.get(stdocId);
			stdocInfo.setFileName(stdocInfo.getFileName().substring(0,stdocInfo.getFileName().indexOf(".")));
		}
		return stdocInfo;
	}
	
	//检查用户是否拥有对应角色
	private boolean chackRole(Integer userId, String rolecode) {
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("userId",userId));
		List<UserRoleBean> userRolelist = userRoleService.queryListByCondition(query);
		if(userRolelist!=null && userRolelist.size()>0){
			Integer[] roleIds = new Integer[userRolelist.size()];
			for(int i=0;i<userRolelist.size();i++){
				UserRolePk userRolePk = userRolelist.get(0).getPk();
				Integer roleId = userRolePk.getRoleId();
				roleIds[i] = roleId;
			}
			ConditionQuery query2 = new ConditionQuery();
			query2.add(Restrictions.in("roleId",roleIds));
			List<RoleBean> roleBeanList = roleService.queryListByCondition(query2);
			if(roleBeanList!=null && roleBeanList.size()>0){
				for(RoleBean roleBean : roleBeanList){
					if(rolecode.equals(roleBean.getRoleCode())){
						return false;
					}
				}
			}
		}
		return true;
	}
	
}
