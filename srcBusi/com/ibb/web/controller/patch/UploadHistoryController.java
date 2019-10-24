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
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.patch.PatchInfo;
import com.ibb.model.patch.UploadCheck;
import com.ibb.service.patch.IPatchInfoService;
import com.ibb.service.patch.IUploadCheckService;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.service.IUserService;

@Controller
public class UploadHistoryController implements ServletContextAware{
	@Autowired
	private IUploadCheckService uploadCheckService;
	@Autowired
	private IPatchInfoService patchInfoService;
	@Autowired
	private IUserService userService;
	
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
	@RequestMapping(value = "/uploadHistory")
	public String toPatchInfoPage() {
		return "patch/uploadHistory";
	}
	/**
	 * 查询申请列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadHistory/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<UploadCheck> list(UploadCheck uploadCheck,PatchInfo patchInfo,HttpServletRequest request) {
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
			Integer isOk=Integer.parseInt(request.getParameter("isOk"));
			query.add(Restrictions.eq("isOk", 1));
		}
		
		//申请人模糊查询
		if(uploadCheck.getRemark()!=null){
			String userName=uploadCheck.getRemark();
			ConditionQuery query1 = new ConditionQuery();
			query1.add(Restrictions.like("userName","%"+userName.trim()+"%" ));
			List<UserBean> list1=userService.queryListByCondition(query1);
			ArrayList<Integer> arr=new ArrayList<Integer>();
			if(list1!=null&&list1.size()>0){
				for (UserBean userBean : list1) {
					arr.add(userBean.getUserId());
				}
				if(arr.size()==0){
					query.add(Restrictions.eq("oper", 0));
				}else{
					query.add(Restrictions.in("oper", arr));
				}
			}else{
				query.add(Restrictions.eq("oper", 0));
			}
		}
		
		
		//审批人模糊查询
		if(uploadCheck.getAgreeTime()!=null){
			String userName=uploadCheck.getAgreeTime();
			ConditionQuery query1 = new ConditionQuery();
			query1.add(Restrictions.like("userName","%"+userName.trim()+"%" ));
			List<UserBean> list1=userService.queryListByCondition(query1);
			ArrayList<Integer> arr=new ArrayList<Integer>();
			if(list1!=null&&list1.size()>0){
				for (UserBean userBean : list1) {
					arr.add(userBean.getUserId());
				}
				if(arr.size()==0){
					query.add(Restrictions.eq("agreeOper", 0));
				}else{
					query.add(Restrictions.in("agreeOper", arr));
				}
			}else{
				query.add(Restrictions.eq("agreeOper", 0));
			}
		}
		
		
		//成果名称模糊查询
		if(StringUtils.isNotEmpty(patchInfo.getPatName())){
			ConditionQuery query1 = new ConditionQuery();
			query1.add(Restrictions.like("patName", "%"+patchInfo.getPatName().trim()+"%"));
			List<PatchInfo> list=patchInfoService.queryListByCondition(query1);
			List<Integer> arr=new ArrayList<Integer>();
			if(list!=null&&list.size()>0){
				for (PatchInfo patchInfo1 : list) {
					arr.add(patchInfo1.getPatId());
				}
				query.add(Restrictions.in("patId", arr));
			}else{
				query.add(Restrictions.eq("patId", 0));
			}
		}
		//文件名称模糊查询
		if(StringUtils.isNotEmpty(patchInfo.getFileName())){
			ConditionQuery query1 = new ConditionQuery();
			query1.add(Restrictions.like("fileName", "%"+patchInfo.getFileName().trim()+"%"));
			List<PatchInfo> list=patchInfoService.queryListByCondition(query1);
			List<Integer> arr=new ArrayList<Integer>();
			if(list!=null&&list.size()>0){
				for (PatchInfo patchInfo1 : list) {
					arr.add(patchInfo1.getPatId());
				}
				query.add(Restrictions.in("patId", arr));
			}else{
				query.add(Restrictions.eq("patId", 0));
			}
		}
		//申请的起始时间模糊查询
		if(StringUtils.isNotEmpty(uploadCheck.getTime())){
			query.add(Restrictions.like("time", "%"+uploadCheck.getTime().trim()+"%"));
		}
		/*//申请的截止时间模糊查询
		if(StringUtils.isNotEmpty(uploadCheck.getAgreeTime())){
			query.add(Restrictions.le("time", uploadCheck.getAgreeTime()));
		}*/
		//状态
		if (uploadCheck.getIsOk()!=null) {
			query.add(Restrictions.eq("isOk", uploadCheck.getIsOk()));
		}
		//类型
		if (uploadCheck.getType()!=null) {
			query.add(Restrictions.eq("type", uploadCheck.getType()));
		}
		//带参数传递过来的是查询积分详情，其他情况为查询本人历史记录
		String oper=request.getParameter("userId");
		String nows=request.getParameter("nows");//用于识别是否应该过滤用户
		if(StringUtils.isNotEmpty(oper)){
			query.add(Restrictions.eq("oper",Integer.parseInt(oper)));
		}else if("noUser".equals(nows)){
			//不进行过滤
		}else{
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			query.add(Restrictions.eq("oper", userBean.getUserId()));
		}
		Page<UploadCheck> page=uploadCheckService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<UploadCheck>(page);
	}
	
}
