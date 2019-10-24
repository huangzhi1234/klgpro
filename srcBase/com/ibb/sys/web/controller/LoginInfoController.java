package com.ibb.sys.web.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.sys.model.LoginInfo;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.service.ILoginInfoService;
@Controller
public class LoginInfoController implements ServletContextAware{
	@Autowired
	private ILoginInfoService loginInfoService;
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

	/**
	 * 获得登录用户上次登录时间的对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login/getLoginInfo.json", method = RequestMethod.POST)//处理请求地址映射的注解
	@ResponseBody
	public LoginInfo getLoginInfo(HttpServletRequest request){
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		LoginInfo loginInfo=null;//令depart为空
		ConditionQuery query=new ConditionQuery();
		query.add(Restrictions.eq("userId", userBean.getUserId()));
		OrderBy oreder=new OrderBy();
		oreder.add(Order.desc("loginTime"));
		List<LoginInfo> list=loginInfoService.queryListByCondition(query, oreder);
		if(list!=null&&list.size()>0){
			loginInfo=list.get(0);
		}
		return loginInfo;
	}
}
