package com.ibb.main.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.sys.model.UserBean;
import com.ibb.sys.service.IResourceService;

/**
 * 用户主页控制
 * @author kin wong
 */
@Controller
public class MainController {
	@Autowired
    private IResourceService resourceService;
	
	/**
	 * 初始化系统界面
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String initPage(){
		return "main/main";
	}
	
	/**
	 * 初始化系统头部
	 * @return
	 */
	@RequestMapping(value = "/main/top")
	public String initTopPage(){
		return "main/top";
	}
	
	/**
	 * 初始化菜单树
	 * @return
	 */
	@RequestMapping(value = "/main/tree")
	public String initTree(){
		return "main/tree";
	}
	
	/**
	 * 初始化系统左则
	 * @return
	 */
	@RequestMapping(value = "/main/left")
	public String initLeftPage(HttpServletRequest request){
		HttpSession session = request.getSession();
	    UserBean user = (UserBean)session.getAttribute("user");
	    
	    if(user != null){
	    	Object[] obj = resourceService.queryResourceByUserId(user.getUserId());
		    	
		    //写入session
			request.getSession().setAttribute("resource", obj[0]);
			request.getSession().setAttribute("urlMap", obj[1]);
			request.getSession().setAttribute("navMap", obj[2]);
			request.getSession().setAttribute("bntMap", obj[3]);
			request.getSession().setAttribute("noBntMap", obj[4]);
	    }else{
	    	 return "redirect:/mgr/main"; 
	     }
	     
		return "main/left";
	}
	
	/**
	 * 方法说明：根据Session中的用户，获取其权限菜单
	 * @author Ou
	 * 2013-8-21
	 */
	@RequestMapping(value = "/menu/tree")
	@ResponseBody
	public List tree(String id,HttpServletRequest request){
		try{
			
			//List<RoleDto> userRoleList = this.roleService.findUserRole(this.getSessionUserId());
			//this.setSessionAttribute(Constant.USER_ROLE_LIST_KEY,userRoleList);
			HttpSession session = request.getSession();
		    UserBean user = (UserBean)session.getAttribute("user");
			return this.resourceService.getUserMenu(Integer.toString(user.getUserId()),id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		 
	}
	
	/**
	 * 初始化系统主页
	 * @return
	 */
	@RequestMapping(value = "/main/right")
	public String initRightPage(){
		return "main/index";
	}
	
	/**
	 * 初始化禁止访问提示页面
	 * @return
	 */
	@RequestMapping(value = "/error/forbid")
	public String iniForbidPage(){
		return "common/forbid";
	}
}
