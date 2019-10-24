package com.ibb.main.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.DateUtil;
import com.ibb.common.util.SecureUtil;
import com.ibb.sys.model.LoginInfo;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.service.ILoginInfoService;
import com.ibb.sys.service.IUserService;

/**
 * 用户登陆(登出)控制
 * @author kin wong
 */
@Controller
public class LoginController {
	@Autowired
    private IUserService userService;
	@Autowired
	private ILoginInfoService loginInfoService;
	
	/**
	 * 初始化登陆页面
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String initPage(){
		return "main/login";
	}
	
	/**
	 * 登陆验证
	 * @return
	 */
	@RequestMapping(value = "/login/check",method=RequestMethod.POST)
	public String login(UserBean user,HttpServletRequest request,HttpServletResponse response){
		String rmPwd = request.getParameter("rmPwd");
		
		if(userService != null && user != null){
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("userAct", user.getUserAct().trim()));
			query.add(Restrictions.eq("userPwd", SecureUtil.encode("SHA", user.getUserPwd())));
			//query.add(Restrictions.eq("userPwd", user.getUserPwd()));
			List<UserBean> userList = userService.queryListByCondition(query);
			if(userList != null && userList.size() == 1){
				//记住密码
				if(StringUtils.isNotEmpty(rmPwd) && "1".equals(rmPwd)){
					Cookie nameCookie = new Cookie("userAct",user.getUserAct());
					Cookie pwdCookie = new Cookie("userPwd",user.getUserPwd());
					Cookie rmCookie = new Cookie("rmPwd",rmPwd);
					nameCookie.setMaxAge(24 * 60 * 60);
					pwdCookie.setMaxAge(24 * 60 * 60);
					rmCookie.setMaxAge(24 * 60 * 60);
					response.addCookie(nameCookie);
					response.addCookie(pwdCookie);
					response.addCookie(rmCookie);
				}else{
					Cookie[] cookies = request.getCookies(); 
					for(Cookie cookie : cookies){
						if("userAct".equals(cookie.getName()) || "userPwd".equals(cookie.getName()) || "rmPwd".equals(cookie.getName())){
							cookie.setMaxAge(0);
							response.addCookie(cookie);
						}
					}
				}
				
				//检查帐号是否过期
				if(userList.get(0).getActTime() != null && userList.get(0).getActTime().getTime() < new Date().getTime() ){
					request.setAttribute("msg", "用户帐户已失效,请联系管理员");
				}else{
					//写入session
					request.getSession().setAttribute("user", userList.get(0));
					//记录用户登录信息：userId和登录时间
					LoginInfo loginInfo=new LoginInfo();
					loginInfo.setUserId(userList.get(0).getUserId());
					loginInfo.setLoginTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					loginInfoService.save(loginInfo);
					return "redirect:/mgr/main";
				}
			}else{
				request.setAttribute("msg", "用户名密码不正确");
			}
		}
		return "main/login";
	}
	
	@RequestMapping(value = "/lanlingcheck",method=RequestMethod.GET)
	public String lanlingcheck(UserBean user,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String outsrt="系统错误";
		
		String lanlingUserNum = request.getParameter("lanlingUserNum");	
		//链接传输的秘钥
		String SecretKey = request.getParameter("SecretKey");
		//暂时定义的秘钥常量
		final String Sk = "abc";
		PrintWriter out = response.getWriter();
		
		String mess="{\"data\":"+"\""+outsrt+"\"}";
	
		if(userService != null && user != null){
			ConditionQuery query = new ConditionQuery();
			
			//query.add(Restrictions.eq("userPwd", user.getUserPwd()));
			List<UserBean> userList = userService.queryListByCondition(query);
			if(userList != null && userList.size() == 1 || Sk.equals(SecretKey)){

				
				//检查帐号是否过期
				if(userList.get(0).getActTime() != null && userList.get(0).getActTime().getTime() < new Date().getTime() ){
					request.setAttribute("msg", "用户帐户已失效,请联系管理员");
				}else{
					//写入session
					request.getSession().setAttribute("user", userList.get(0));
					return "redirect:/mgr/main";
				}
			}else{
				request.setAttribute("msg", "登录错误");
				out.print(outsrt);
				
				System.out.println(outsrt);
			}
		}
		return "main/login";
	}
	
	
	
	
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request){
		request.getSession().invalidate();
		return "redirect:/mgr/login";
	}
}
