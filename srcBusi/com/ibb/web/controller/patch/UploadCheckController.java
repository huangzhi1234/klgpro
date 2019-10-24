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
import com.ibb.service.patch.IPatchDlRcService;
import com.ibb.service.patch.IPatchInfoService;
import com.ibb.service.patch.IUploadCheckService;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.service.score.IProScoreService;
import com.ibb.sys.model.UserBean;

@Controller
public class UploadCheckController implements ServletContextAware{
	@Autowired
	private IUploadCheckService uploadCheckService;
	@Autowired
	private IProScoreRuleService proScoreRuleService;
	@Autowired
	private IProScoreService proScoreService;
	@Autowired
	private IPatchInfoService patchInfoService;
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
	@RequestMapping(value = "/uploadCheck")
	public String toPatchInfoPage() {
		return "patch/uploadCheck";
	}
	@RequestMapping(value = "/suploadCheck")
	public String tosPatchInfoPage() {
		return "patch/suploadCheck";
	}
	/**
	 * 查询申请列表 分页查询+模糊查询（研发）
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadCheck/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<UploadCheck> list(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
		String uploadIdArr=request.getParameter("uploadIdArr");
		if(uploadIdArr!=null){
			String[] uploadIds=uploadIdArr.split("aaa");
			List<Integer> list=new ArrayList<Integer>();
			if (uploadIds!=null) {
				for (String uploadId : uploadIds) {
					list.add(Integer.parseInt(uploadId));
				}
				if(list!=null&&list.size()>0){
					query.add(Restrictions.in("uploadId", list));
				}
			}
		}
		
		
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 1));//研发
		Page<UploadCheck> page=uploadCheckService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<UploadCheck>(page);
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询（实施）
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadCheck/list2.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<UploadCheck> list2(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
		String uploadIdArr=request.getParameter("uploadIdArr");
		if(uploadIdArr!=null){
			String[] uploadIds=uploadIdArr.split("aaa");
			List<Integer> list=new ArrayList<Integer>();
			if (uploadIds!=null) {
				for (String uploadId : uploadIds) {
					list.add(Integer.parseInt(uploadId));
				}
				if(list!=null&&list.size()>0){
					query.add(Restrictions.in("uploadId", list));
				}
			}
		}
		
		
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 2));//实施
		Page<UploadCheck> page=uploadCheckService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<UploadCheck>(page);
	}
	
	
	
	
	/**
	 * 本人上传项目通过的列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadCheck/upList.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<UploadCheck> upList(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		query.add(Restrictions.eq("oper", userBean.getUserName()));
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("isOk", 1));
		Page<UploadCheck> page=uploadCheckService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<UploadCheck>(page);
	}
	
	
	
	
	
	
	/**
	 * 进行单个审核通过
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadCheck/agree", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel agree(HttpServletRequest request) {
		
			Integer uploadId=Integer.parseInt(request.getParameter("uploadId"));
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			UploadCheck uploadCheck=uploadCheckService.get(uploadId);
			Integer score=proScoreRuleService.get(1).getScore();
			if(uploadCheck!=null){
				uploadCheck.setIsOk(1);
				uploadCheck.setState(0);
				uploadCheck.setAgreeOper(userBean.getUserId());
				uploadCheckService.update(uploadCheck);
				proScoreService.addUpScore(uploadCheck.getOper(),score);//获得积分
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
	@RequestMapping(value = "/uploadCheck/agreeMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel agreeMore(String uploadIdArr, HttpServletRequest request) {
		Integer score=proScoreRuleService.get(1).getScore();
		if(uploadIdArr!=null){
			String[] uploadIds = uploadIdArr.split("#");
			try {
				for (String uploadId : uploadIds) {
					if (StringUtils.isNotEmpty(uploadId)) {
						UploadCheck uploadCheck=uploadCheckService.get(Integer.valueOf(uploadId));
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						uploadCheck.setIsOk(1);
						uploadCheck.setState(0);
						uploadCheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						uploadCheck.setAgreeOper(userBean.getUserId());
						uploadCheckService.update(uploadCheck);
						proScoreService.addUpScore(uploadCheck.getOper(),score);//获得积分
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
	 * 进行单个申请拒绝
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadCheck/refuse", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel refuse(HttpServletRequest request) {
		
			Integer uploadId=Integer.parseInt(request.getParameter("uploadId"));
			String remark=request.getParameter("remark");
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			UploadCheck uploadCheck=uploadCheckService.get(uploadId);
			if(uploadCheck!=null){
				uploadCheck.setIsOk(2);
				uploadCheck.setState(0);
				uploadCheck.setAgreeOper(userBean.getUserId());
				uploadCheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
				uploadCheck.setRemark(remark);
				uploadCheckService.update(uploadCheck);
				
				//将该成果信息状态改为被删除状态
				if(uploadCheck.getPatId()!=null){
					PatchInfo patchInfo=patchInfoService.get(uploadCheck.getPatId());
					if(patchInfo!=null){
						patchInfo.setState(0);
						patchInfoService.update(patchInfo);
					}
				}
				
				
				
				return new CommonJsonModel(true,"操作成功");
			}
		return new CommonJsonModel(false,"操作失败！");
	}
	
	/**
	 * 进行批量拒绝
	 * @param uploadIdArr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadCheck/refuseMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel refuseMore(String uploadIdArr,HttpServletRequest request) {
		
		if(uploadIdArr!=null){
			String[] uploadIds = uploadIdArr.split("#");
			try {
				/*for (String uploadId : uploadIds) {
					if (StringUtils.isNotEmpty(uploadId)) {
						UploadCheck uploadCheck=uploadCheckService.get(Integer.valueOf(uploadId));
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						uploadCheck.setIsOk(2);
						uploadCheck.setState(0);
						uploadCheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						uploadCheck.setAgreeOper(userBean.getUserName());
						
						uploadCheckService.update(uploadCheck);
					}
				}*/
				int uploadId=0;
				String remark="";
				for (int i = 0; i < uploadIds.length; i++) {
					if(i%2==0){
						uploadId=Integer.parseInt(uploadIds[i]);
					}else{
						remark=uploadIds[i];
						UploadCheck uploadCheck=uploadCheckService.get(uploadId);
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						uploadCheck.setIsOk(2);
						uploadCheck.setState(0);
						uploadCheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						uploadCheck.setAgreeOper(userBean.getUserId());
						uploadCheck.setRemark(remark);
						uploadCheckService.update(uploadCheck);
						//将该成果信息状态改为被删除状态
						if(uploadCheck.getPatId()!=null){
							PatchInfo patchInfo=patchInfoService.get(uploadCheck.getPatId());
							if(patchInfo!=null){
								patchInfo.setState(0);
								patchInfoService.update(patchInfo);
							}
						}
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
	
	
	
	
}
