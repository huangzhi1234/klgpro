package com.ibb.web.controller.system;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.SecureUtil;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.service.IUserService;

/**
 * @author zhangjian 修改密码的controller
 * 
 */
@Controller
public class ModifyPasswordController implements ServletContextAware {
	@Autowired
    private IUserService userService;
			
	private ServletContext servletContext;

	@Override
	/**
	 * 实现了ServletContextAware接口，就可以通过这样获得servletContext
	 */
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	@RequestMapping(value = "/modifyPassword")
	public String toEvaluationProjectPage() {
		return "sys/modifyPassword";
	}
	
	/**
	 * 修改密码
	 * @param user 用户对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/modifyPassword/update.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel add(@RequestParam(value = "oldPassword", required = false)String oldPassword,
			@RequestParam(value = "newPassword", required = false)String newPassword,
			@RequestParam(value = "confirmNewPassword", required = false)String confirmNewPassword, HttpServletRequest request){
		
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("userAct", userBean.getUserAct().trim()));
			query.add(Restrictions.eq("userPwd", SecureUtil.encode("SHA", oldPassword)));
			List<UserBean> userList = userService.queryListByCondition(query);
			if(userList != null && userList.size() == 1){
				UserBean temp = userList.get(0);			
				temp.setUserPwd(SecureUtil.encode("SHA", newPassword));
				userService.update(temp);
			}else{
				return new CommonJsonModel(false,"原密码错误！");
			}
			
			return new CommonJsonModel(true,"密码修改成功！");
	}
	
}