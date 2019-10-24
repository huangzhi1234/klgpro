package com.ibb.web.controller.patch;

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

import com.ibb.common.util.DateUtil;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.model.patch.CheckLoad;
import com.ibb.model.patch.PatchInfo;
import com.ibb.model.patch.UploadCheck;
import com.ibb.service.patch.ICheckLoadService;
import com.ibb.service.patch.IPatchInfoService;
import com.ibb.service.patch.IUploadCheckService;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.sys.model.UserBean;
@Controller
public class PatEditController implements ServletContextAware{
	@Autowired
	private ICheckLoadService checkLoadService;
	@Autowired
	private IUploadCheckService uploadCheckService;
	@Autowired
	private IProScoreRuleService proScoreRuleService;
	@Autowired
	private IPatchInfoService patchInfoService;
	
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

	@RequestMapping(value = "/patEditHistory")
	public String toPatchInfoPage() {
		return "patch/patEditHistory";
	}
	/**
	 * 下载审批更新
	 * @param checkLoad
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/patEditHistory/downUpdata.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel downUpdata(CheckLoad checkLoad, HttpServletRequest request) {
		if (checkLoad != null) {
			if(checkLoad.getCheckId()!=null){
				try{
					CheckLoad check=checkLoadService.get(checkLoad.getCheckId());
					UserBean userBean = (UserBean) request.getSession().getAttribute("user");
					check.setAgreeOper(userBean.getUserId());
					check.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					check.setRemark(checkLoad.getRemark());
					check.setIsOk(checkLoad.getIsOk());
					checkLoadService.update(check);
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
	 * 
	 * @param uploadCheck
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/patEditHistory/upUpdata.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel upUpdata(UploadCheck uploadCheck, HttpServletRequest request) {
		if (uploadCheck != null) {
			if(uploadCheck.getUploadId()!=null){
				try{
					UploadCheck upload=uploadCheckService.get(uploadCheck.getUploadId());
					UserBean userBean = (UserBean) request.getSession().getAttribute("user");
					upload.setAgreeOper(userBean.getUserId());
					upload.setAgreeTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					upload.setRemark(uploadCheck.getRemark());
					upload.setIsOk(uploadCheck.getIsOk());
					uploadCheckService.update(upload);
					//将该成果信息状态进行修改
					if(upload.getPatId()!=null){
						PatchInfo patchInfo=patchInfoService.get(upload.getPatId());
						if(patchInfo!=null){
							if(upload.getIsOk()==2){
								patchInfo.setState(0);
							}else if(upload.getIsOk()==1){
								patchInfo.setState(1);
							}
							
							patchInfoService.update(patchInfo);
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
