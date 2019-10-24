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
import com.ibb.common.util.DateUtil;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.patch.PatchInfo;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.model.stDoc.StdocUploadcheck;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.service.score.IProScoreService;
import com.ibb.service.stDoc.IStDocService;
import com.ibb.service.stDoc.IStdocUploadcheckService;
import com.ibb.sys.model.UserBean;

@Controller
public class StdocUploadCheckController implements ServletContextAware{
	@Autowired
	private IStdocUploadcheckService stdocUploadcheckService;
	@Autowired
	private IProScoreService proScoreService;
	@Autowired
	private IProScoreRuleService proScoreRuleService;
	@Autowired
	private IStDocService sdocService;

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
	@RequestMapping(value = "/stdocUploadcheck")
	public String toPatchInfoPage() {
		return "stDoc/stdocUploadcheck";
	}
	//实施
	@RequestMapping(value = "/stdocUploadcheck2")
	public String toPatchInfoPage2() {
		return "stDoc/stdocUploadcheck2";
	}
	
	//经典案例
	@RequestMapping(value = "/stdocUploadcheck4")
	public String toPatchInfoPage4() {
		return "case/classicCaseUploadcheck";
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询  开发
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocUploadcheck/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocUploadcheck> list(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
	
		
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 1));
		
		Page<StdocUploadcheck> page=stdocUploadcheckService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<StdocUploadcheck>(page);
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询  实施
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocUploadcheck/list2.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocUploadcheck> list2(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
	
		
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 2));
		
		Page<StdocUploadcheck> page=stdocUploadcheckService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<StdocUploadcheck>(page);
	}
	
	/**
	 * 查询申请列表 分页查询+模糊查询  经典案例
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocUploadcheck/list4.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocUploadcheck> list4(HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("time"));
		
	
		
		//进行过滤，处于审核状态的信息列表才显示出来
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 4));
		
		Page<StdocUploadcheck> page=stdocUploadcheckService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<StdocUploadcheck>(page);
	}
	
	/**
	 * 进行单个审核通过
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocUploadcheck/agree", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel agree(HttpServletRequest request) {
			Integer score=proScoreRuleService.get(1).getScore();
			Integer uploadId=Integer.parseInt(request.getParameter("uploadId"));
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			StdocUploadcheck stdocUploadcheck=stdocUploadcheckService.get(uploadId);
			if(stdocUploadcheck!=null){
				stdocUploadcheck.setIsOk(1);
				stdocUploadcheck.setState(0);
				stdocUploadcheck.setAgreeOper(userBean.getUserId());
				stdocUploadcheckService.update(stdocUploadcheck);
				proScoreService.addUpScore(stdocUploadcheck.getOper(),score);//获得积分
				return new CommonJsonModel(true,"操作成功");
			}
		return new CommonJsonModel(false,"操作失败！");
	}
	
	/**
	 * 进行单个申请拒绝
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocUploadcheck/refuse", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel refuse(HttpServletRequest request) {
		
			Integer uploadId=Integer.parseInt(request.getParameter("uploadId"));
			String remark=request.getParameter("remark");
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			StdocUploadcheck stdocUploadcheck=stdocUploadcheckService.get(uploadId);
			if(stdocUploadcheck!=null){
				stdocUploadcheck.setIsOk(2);
				stdocUploadcheck.setState(0);
				stdocUploadcheck.setAgreeOper(userBean.getUserId());
				stdocUploadcheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
				stdocUploadcheck.setRemark(remark);
				stdocUploadcheckService.update(stdocUploadcheck);
				//将该文档信息状态改为被删除状态
				if(stdocUploadcheck.getStdocId()!=null){
					StdocInfo stoInfo=sdocService.get(stdocUploadcheck.getStdocId());
					if(stoInfo!=null){
						stoInfo.setState(0);
						sdocService.update(stoInfo);
					}
				}
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
	@RequestMapping(value = "/stdocUploadcheck/agreeMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel agreeMore(String uploadIdArr, HttpServletRequest request) {
		Integer score=proScoreRuleService.get(1).getScore();
		if(uploadIdArr!=null){
			String[] uploadIds = uploadIdArr.split("#");
			try {
				for (String uploadId : uploadIds) {
					if (StringUtils.isNotEmpty(uploadId)) {
						StdocUploadcheck stdocUploadcheck=stdocUploadcheckService.get(Integer.valueOf(uploadId));
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						stdocUploadcheck.setIsOk(1);
						stdocUploadcheck.setState(0);
						stdocUploadcheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						stdocUploadcheck.setAgreeOper(userBean.getUserId());
						stdocUploadcheckService.update(stdocUploadcheck);
						proScoreService.addUpScore(stdocUploadcheck.getOper(),score);//获得积分
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
	 * @param uploadIdArr
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stdocUploadcheck/refuseMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel refuseMore(String uploadIdArr,HttpServletRequest request) {
		
		if(uploadIdArr!=null){
			String[] uploadIds = uploadIdArr.split("#");
			try {
				int uploadId=0;
				String remark="";
				for (int i = 0; i < uploadIds.length; i++) {
					if(i%2==0){
						uploadId=Integer.parseInt(uploadIds[i]);
					}else{
						remark=uploadIds[i];
						StdocUploadcheck stdocUploadcheck=stdocUploadcheckService.get(uploadId);
						UserBean userBean = (UserBean) request.getSession().getAttribute("user");
						stdocUploadcheck.setIsOk(2);
						stdocUploadcheck.setState(0);
						stdocUploadcheck.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
						stdocUploadcheck.setAgreeOper(userBean.getUserId());
						stdocUploadcheck.setRemark(remark);
						stdocUploadcheckService.update(stdocUploadcheck);
						
						//将该文档信息状态改为被删除状态
						if(stdocUploadcheck.getStdocId()!=null){
							StdocInfo stoInfo=sdocService.get(stdocUploadcheck.getStdocId());
							if(stoInfo!=null){
								stoInfo.setState(0);
								sdocService.update(stoInfo);
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
