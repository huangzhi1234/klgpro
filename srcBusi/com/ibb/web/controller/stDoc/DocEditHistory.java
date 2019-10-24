package com.ibb.web.controller.stDoc;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.DateUtil;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.model.patch.CheckLoad;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.score.ProScore;
import com.ibb.model.stDoc.StdocDowncheck;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.model.stDoc.StdocUploadcheck;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.service.score.IProScoreService;
import com.ibb.service.stDoc.IStDocService;
import com.ibb.service.stDoc.IStdocDowncheckService;
import com.ibb.service.stDoc.IStdocUploadcheckService;
import com.ibb.sys.model.UserBean;
import com.sun.org.apache.bcel.internal.generic.ISTORE;

@Controller
public class DocEditHistory implements ServletContextAware{
	@Autowired
	private IStdocDowncheckService stdocDowncheckService;
	@Autowired
	private IStdocUploadcheckService stdocUploadcheckService;
	@Autowired
	private IProScoreRuleService proScoreRuleService;
	@Autowired
	private IProScoreService proScoreService;
	@Autowired
	private IStDocService stDocService;
	
	@Autowired
	@Qualifier("hibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@SuppressWarnings("unused")
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext arg0) {
		this.servletContext = arg0;
	}

	@RequestMapping(value = "/docEditHistory")
	public String toPatchInfoPage() {
		return "stDoc/docEditHistory";
	}
	
	
	@RequestMapping(value = "/caseDocEditHistory")
	public String toPatchInfoPage4() {
		return "case/caseDocEditHistory";
	}
	
	
	/**
	 * 下载审批更新
	 * @param checkLoad
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/docEditHistory/downUpdata.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel downUpdata(StdocDowncheck stdocDowncheck, HttpServletRequest request) {
		if (stdocDowncheck != null) {
			if(stdocDowncheck.getCheckId()!=null){
				try{
					StdocDowncheck stdoc=stdocDowncheckService.get(stdocDowncheck.getCheckId());
					UserBean userBean = (UserBean) request.getSession().getAttribute("user");
					stdoc.setAgreeOper(userBean.getUserId());
					stdoc.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					stdoc.setRemark(stdocDowncheck.getRemark());
					stdoc.setIsOk(stdocDowncheck.getIsOk());
					stdocDowncheckService.update(stdoc);
					
					/*-------------更新总积分------------------*/
					proScoreRuleService.getAllScore();
					
					
				} catch(Exception e){
					e.printStackTrace();
					return new CommonJsonModel(false,"更新失败！");
				}
				return new CommonJsonModel();
			}
		}
		return new CommonJsonModel(false,"更新失败！");
	}
	/**
	 * 
	 * 上传审批更新
	 * @param uploadCheck
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/docEditHistory/upUpdata.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel upUpdata(StdocUploadcheck stdocUploadcheck, HttpServletRequest request) {
		if (stdocUploadcheck != null) {
			if(stdocUploadcheck.getUploadId()!=null){
				try{
					StdocUploadcheck stdoc=stdocUploadcheckService.get(stdocUploadcheck.getUploadId());
					UserBean userBean = (UserBean) request.getSession().getAttribute("user");
					stdoc.setAgreeOper(userBean.getUserId());
					stdoc.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					stdoc.setRemark(stdocUploadcheck.getRemark());
					stdoc.setIsOk(stdocUploadcheck.getIsOk());
					stdocUploadcheckService.update(stdoc);
					//将文档信息状态进行修改
					if(stdoc.getStdocId()!=null){
						StdocInfo stdocInfo=stDocService.get(stdoc.getStdocId());
						if(stdocInfo!=null){
							if(stdoc.getIsOk()==2){
								stdocInfo.setState(0);
							}else if(stdoc.getIsOk()==1){
								stdocInfo.setState(1);
							}
							stDocService.update(stdocInfo);
						}
					
						
					}
					
					/*-------------更新总积分------------------*/
					proScoreRuleService.getAllScore();
					
				} catch(Exception e){
					e.printStackTrace();
					return new CommonJsonModel(false,"更新失败！");
				}
				return new CommonJsonModel();
			}
		}
		return new CommonJsonModel(false,"更新失败！");
	}
}
