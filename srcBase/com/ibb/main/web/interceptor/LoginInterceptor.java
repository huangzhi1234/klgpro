package com.ibb.main.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ibb.sys.model.UserBean;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		String contextPath=request.getContextPath();
        HttpSession session = request.getSession();
        String url = request.getRequestURI().replace(request.getContextPath(), "");
        
        UserBean user = (UserBean)session.getAttribute("user");
        Map<String,Boolean> urlMap = (Map<String, Boolean>) session.getAttribute("urlMap");
        
    	if(request.getRequestURI().contains("/appAmount")){
			return true;
		}
        
    	if(request.getRequestURI().contains("/nowpay")){
			return true;
		}
    	
        if(request.getRequestURI().contains("/image/design")){
			return true;
		}

        if(request.getRequestURI().contains("/lanlingcheck")){
			return true;
		}		
        if(user == null){
        	response.sendRedirect(contextPath+"/mgr/login");
        	return false;
        }
        
        if(urlMap != null){
        	if(urlMap.containsKey(url) && !urlMap.get(url)){
        		request.getRequestDispatcher("/mgr/error/forbid").forward(request, response);
            	return false;
        	}
        }
        
        request.setAttribute("resourceUrl", request.getServletPath() + request.getPathInfo());
		return true;
	}

}
