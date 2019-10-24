package com.ibb.common.web.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibb.sys.model.RoleBean;
import com.ibb.sys.model.UserBean;

/**
 * 控制器基类，主要实现以下功能

 * @author kin wong
 */
public abstract class BaseController {
	private UserBean user;
	
	/**
	 * 得到session对象
	 * @return session对象
	 */
	public HttpSession getSession(){
		HttpSession session = getRequest().getSession();
		return session;
	}
	
	/**
	 * 得到request对象
	 * @return request对象
	 */
	public HttpServletRequest getRequest(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	/**
	 * 得到当前登陆用户信息
	 * @return 当前登陆用户对象
	 */
	public UserBean getUser() {
		if(user == null)
			user = (UserBean) getSession().getAttribute("user");
		return user;
	}
	
	/**
	 * 当前登陆用户名
	 * @return 登陆用户名
	 */
	public String getUserName(){
		return getUser().getUserName();
	}
	
	/**
	 * 当前登陆用户帐号
	 * @return
	 */
	public String getUserAccount(){
		return getUser().getUserAct();
	}
	
	/**
	 * 当前用户角色列表
	 * @return 当前用户角色列表对象
	 */
	public Set<RoleBean> getRoleSet() {
		return null;
	}
}
