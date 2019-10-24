package com.ibb.web.controller.projectMessage;


import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.web.controller.BaseController;
import com.ibb.service.projectMessage.IproAttachService;

@Controller
@RequestMapping(value="/proAttach/*")
public class ProAttachController extends BaseController implements ServletContextAware{
	
	@Autowired
	private ServletContext servletContext;
	private IproAttachService proAttachService;
	private Logger logger=Logger.getLogger(this.getClass());
	


	
	/**
	 * 实现了ServletContextAware接口，就可以通过这样获得servletContext
	 */
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext =context;
	}
	/*@RequestMapping(value="/proAttach")
	public String toCompanyInfoPage() {
		return "proInfoMessage/proAttach";
	}*/
	
	

	
}
