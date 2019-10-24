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
import com.ibb.model.patch.PatchInfo;
import com.ibb.model.stDoc.StdocDowncheck;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.service.stDoc.IStDocService;
import com.ibb.service.stDoc.IStdocDowncheckService;
import com.ibb.sys.model.UserBean;

@Controller
public class StDownHistoryController implements ServletContextAware{
	@Autowired
	private IStdocDowncheckService stdocDowncheckService;
	@Autowired
	private IStDocService stDocService;
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
	@RequestMapping(value = "/stHistory")
	public String toPatchInfoPage() {
		return "stDoc/stHistory";
	}
	//实施
	@RequestMapping(value = "/stHistory2")
	public String toPatchInfoPage2() {
		return "stDoc/stHistory2";
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询   开发和实施
	 * 
	 * @param stdocDowncheck
	 * @param stdocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stHistory/list0.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocDowncheck> list0(StdocInfo stdocInfo,StdocDowncheck stdocDowncheck,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
		String stus=request.getParameter("stus");
		if("hasEdit".equals(stus)){
			query.add(Restrictions.eq("state", 0));
		}
		
		/*---------积分详情模块，进行过滤--------------*/
		if(StringUtils.isNotEmpty(request.getParameter("isOk"))&&request.getParameter("isOk")=="1"){
			Integer isOk=Integer.parseInt(request.getParameter("isOk"));
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
		if(StringUtils.isNotEmpty(stdocDowncheck.getTime())){
			query.add(Restrictions.like("time", "%"+stdocDowncheck.getTime().trim()+"%"));
		}
		//申请的截止时间模糊查询
		/*if(StringUtils.isNotEmpty(stdocDowncheck.getAgreeTime())){
			query.add(Restrictions.le("time", stdocDowncheck.getAgreeTime()));
		}*/
		//状态
		if (stdocDowncheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", stdocDowncheck.getIsOk()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		
		Page<StdocDowncheck> page=stdocDowncheckService.queryListByCondition(query, order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocDowncheck>(page);
	}
	
	
	/**
	 * 查询申请列表 分页查询+模糊查询   开发
	 * 
	 * @param stdocDowncheck
	 * @param stdocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stHistory/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocDowncheck> list(StdocInfo stdocInfo,StdocDowncheck stdocDowncheck,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
		String stus=request.getParameter("stus");
		if("hasEdit".equals(stus)){
			query.add(Restrictions.eq("state", 0));
		}
		
		/*---------积分详情模块，进行过滤--------------*/
		if(StringUtils.isNotEmpty(request.getParameter("isOk"))&&request.getParameter("isOk")=="1"){
			Integer isOk=Integer.parseInt(request.getParameter("isOk"));
			query.add(Restrictions.eq("isOk", 1));
		}
		
		
		ConditionQuery query1 = new ConditionQuery();
		//文件名称模糊查询
		if(StringUtils.isNotEmpty(stdocInfo.getFileName())){
			query1.add(Restrictions.like("fileName", "%"+stdocInfo.getFileName().trim()+"%"));
		}
		//add 文档标识 1 为开发  2为实施  
		query1.add(Restrictions.eq("type", 2));
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
		if(StringUtils.isNotEmpty(stdocDowncheck.getTime())){
			query.add(Restrictions.like("time", "%"+stdocDowncheck.getTime().trim()+"%"));
		}
		//申请的截止时间模糊查询
		/*if(StringUtils.isNotEmpty(stdocDowncheck.getAgreeTime())){
			query.add(Restrictions.le("time", stdocDowncheck.getAgreeTime()));
		}*/
		//状态
		if (stdocDowncheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", stdocDowncheck.getIsOk()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		
		Page<StdocDowncheck> page=stdocDowncheckService.queryListByCondition(query, order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocDowncheck>(page);
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询   实施
	 * 
	 * @param stdocDowncheck
	 * @param stdocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stHistory/list2.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocDowncheck> list2(StdocInfo stdocInfo,StdocDowncheck stdocDowncheck,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
		String stus=request.getParameter("stus");
		if("hasEdit".equals(stus)){
			query.add(Restrictions.eq("state", 0));
		}
		
		/*---------积分详情模块，进行过滤--------------*/
		if(StringUtils.isNotEmpty(request.getParameter("isOk"))&&request.getParameter("isOk")=="1"){
			Integer isOk=Integer.parseInt(request.getParameter("isOk"));
			query.add(Restrictions.eq("isOk", 1));
		}
		
		
		ConditionQuery query1 = new ConditionQuery();
		//文件名称模糊查询
		if(StringUtils.isNotEmpty(stdocInfo.getFileName())){
			query1.add(Restrictions.like("fileName", "%"+stdocInfo.getFileName().trim()+"%"));
			
		}
		
		//add 文档标识 1 为开发  2为实施  
		query1.add(Restrictions.eq("type", 2));
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
		if(StringUtils.isNotEmpty(stdocDowncheck.getTime())){
			query.add(Restrictions.like("time", "%"+stdocDowncheck.getTime().trim()+"%"));
		}
		//申请的截止时间模糊查询
		/*if(StringUtils.isNotEmpty(stdocDowncheck.getAgreeTime())){
			query.add(Restrictions.le("time", stdocDowncheck.getAgreeTime()));
		}*/
		//状态
		if (stdocDowncheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", stdocDowncheck.getIsOk()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		
		Page<StdocDowncheck> page=stdocDowncheckService.queryListByCondition(query, order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocDowncheck>(page);
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询   实施
	 * 
	 * @param stdocDowncheck
	 * @param stdocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stHistory/list4.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocDowncheck> list4(StdocInfo stdocInfo,StdocDowncheck stdocDowncheck,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
		String stus=request.getParameter("stus");
		if("hasEdit".equals(stus)){
			query.add(Restrictions.eq("state", 0));
		}
		
		/*---------积分详情模块，进行过滤--------------*/
		if(StringUtils.isNotEmpty(request.getParameter("isOk"))&&request.getParameter("isOk")=="1"){
			Integer isOk=Integer.parseInt(request.getParameter("isOk"));
			query.add(Restrictions.eq("isOk", 1));
		}
		
		
		ConditionQuery query1 = new ConditionQuery();
		//文件名称模糊查询
		if(StringUtils.isNotEmpty(stdocInfo.getFileName())){
			query1.add(Restrictions.like("fileName", "%"+stdocInfo.getFileName().trim()+"%"));
			
		}
		
		//add 文档标识 经典案例
		query1.add(Restrictions.eq("type", 4));
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
		if(StringUtils.isNotEmpty(stdocDowncheck.getTime())){
			query.add(Restrictions.like("time", "%"+stdocDowncheck.getTime().trim()+"%"));
		}
		//申请的截止时间模糊查询
		/*if(StringUtils.isNotEmpty(stdocDowncheck.getAgreeTime())){
			query.add(Restrictions.le("time", stdocDowncheck.getAgreeTime()));
		}*/
		//状态
		if (stdocDowncheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", stdocDowncheck.getIsOk()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		
		Page<StdocDowncheck> page=stdocDowncheckService.queryListByCondition(query, order, pn, pageSize);
		
		return new EasyUIGridJsonModel<StdocDowncheck>(page);
	}
}
