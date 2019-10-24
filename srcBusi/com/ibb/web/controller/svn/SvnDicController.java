package com.ibb.web.controller.svn;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.util.SVNXMLUtil;
import org.tmatesoft.svn.core.internal.wc.admin.SVNEntry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibb.common.web.controller.BaseController;
import com.ibb.ftp.SFTPChannel;
import com.ibb.model.projectMessage.ProFile;
import com.ibb.model.svn.SvnDicInfo;
import com.ibb.service.svn.impl.SvnDicService;
import com.javaniu.SVN;
import com.javaniu.SVNUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
@Controller
public class SvnDicController extends BaseController implements ServletContextAware{
	@Autowired
	private SvnDicService svnDicService;
	@SuppressWarnings("unused")
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	@RequestMapping(value = "/svn")
	public String toCompanyInfoPage() {
		return "svn/svnInfo";
	}
	/**
	 * 查询信息列表	
	 * @param request
	 * @return
	 * @throws SVNException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/svn/list.json", method = RequestMethod.POST)
	@ResponseBody
	public String list(@RequestParam(value = "pid", required = false)String pid,HttpServletRequest request) throws SVNException, UnsupportedEncodingException {
		Object svns=null;
		String username="fangzw";
		String password="fangzw";
		String path="https://10.22.2.201:5443/svn/Yonyou_Resource/SVN_ARCH/project";
		SVNUtil svnUtil=new SVNUtil(path,username,password);
		String json=null;
		if(pid==""){
			path="";
		}else{
			path=pid;
		}
		path= new String(path.getBytes("ISO-8859-1"),"UTF-8");
		if(svnUtil.login()){
			svns=svnUtil.listEntries(path);
			json = svnUtil.object2Json(svns);
			
		}
		return json;
		
	}
	
	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/svn/down", method = RequestMethod.GET)
	@ResponseBody
	public void down(@RequestParam("url")String url, HttpServletResponse response) {
		System.out.println("----------------------------------------------------"+url);
		String username="fangzw";
		String password="fangzw";
		String filePath="https://10.22.2.201/svn/yonyouCode";
		filePath=filePath+url;
		System.out.println(filePath);
		SVNUtil svnUtil=new SVNUtil(filePath, username, password);
		if(svnUtil.login())
		{
			Boolean bool=svnUtil.checkOut(filePath);
			if(bool){
				System.out.println("下载了");
			}
		}
	}
	
}
