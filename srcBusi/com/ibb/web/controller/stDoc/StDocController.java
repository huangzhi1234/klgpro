package com.ibb.web.controller.stDoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.DateUtil;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.ftp.FTPChannel;
import com.ibb.ftp.LoadPrefix;
import com.ibb.model.stDoc.DocDlRc;
import com.ibb.model.stDoc.StdocDowncheck;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.model.stDoc.StdocUploadcheck;
import com.ibb.service.stDoc.IDocDlRcService;
import com.ibb.service.stDoc.IStdocDowncheckService;
import com.ibb.service.stDoc.IStdocUploadcheckService;
import com.ibb.service.stDoc.impl.StDocService;
import com.ibb.sys.model.UserBean;
import com.ibb.utils.FileUtil;

/**
 * 开发--标准文档
 * @author baobq
 *
 */
@Controller
public class StDocController implements ServletContextAware{
	@Autowired
	private IStdocUploadcheckService stdocUploadcheckService;
	@Autowired
	private StDocService stDocService;
	@Autowired
	private IDocDlRcService docDlRcService;
	@Autowired
	private IStdocDowncheckService stdocDowncheckService;
	
	
	@SuppressWarnings("unused")
	private ServletContext servletContext;
	@Autowired
    @Qualifier("hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	@RequestMapping(value = "/stDocInfo")
	public String toProInfoMessagePage() {
		return "stDoc/stDocInfo";
	}
	
	
	/**
	 * 查询下载申请列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stDocInfo/applyDownList.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocInfo> applyDownList(HttpServletRequest request){
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("createTime"));
		String stdocIdArr=request.getParameter("stdocIdArr");
		if(stdocIdArr!=null){
			String[] stdocIds=stdocIdArr.split(",");
			List<Integer> idList=new ArrayList<Integer>();
			if (stdocIds!=null) {
				for (String stdocId : stdocIds) {
					idList.add(Integer.parseInt(stdocId));
				}
				if(idList!=null&&idList.size()>0){
					query.add(Restrictions.in("stdocId", idList));
				}
			}
		}
		Page<StdocInfo> page = stDocService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<StdocInfo>(page);
	}
	
	
	/**
	 * 查询项目信息列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stDocInfo/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocInfo> queryList(StdocInfo stdocInfo, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("createTime"));
		
		
		/*---------进行过滤：查看是否上传通过审批------------*/
		ConditionQuery query1=new ConditionQuery();
		query1.add(Restrictions.eq("isOk", 1));
		List<StdocUploadcheck> list=stdocUploadcheckService.queryListByCondition(query1);
		List<Integer> str=new ArrayList<Integer>();
		if(list!=null&&list.size()>0){
			for (StdocUploadcheck stdocUploadcheck : list) {
				str.add(stdocUploadcheck.getStdocId());
			}
		}
		if(str!=null&&str.size()>0){
			query.add(Restrictions.in("stdocId", str));
		}else{
			query.add(Restrictions.eq("stdocId", 0));
		}
		/*-----------------------*/
		//add 查询条件添加type=1 查询开发的标准文档
		query.add(Restrictions.eq("type", 1));
		

		// 文件名称
		if (StringUtils.isNotEmpty(stdocInfo.getFileName())) {
			query.add(Restrictions.like("fileName", "%" + stdocInfo.getFileName().trim() + "%"));
		}
		if (StringUtils.isNotEmpty(stdocInfo.getAuthor())) {
			query.add(Restrictions.like("author", "%" + stdocInfo.getAuthor().trim() + "%"));
		}
		if (StringUtils.isNotEmpty(stdocInfo.getCreateOper())) {
			query.add(Restrictions.like("createOper", "%" + stdocInfo.getCreateOper().trim() + "%"));
		}
		if (StringUtils.isNotEmpty(stdocInfo.getDicNum())) {
			query.add(Restrictions.like("dicNum", stdocInfo.getDicNum().trim() + "%"));
		}
		query.add(Restrictions.eq("state", 1));
		Page<StdocInfo> page = stDocService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<StdocInfo>(page);
	}
	
	/**
	 * 删除记录 联表删除
	 * 
	 * @param proIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/stDocInfo/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String stdocIdArr, HttpServletRequest request) {
		if (stdocIdArr!= null) {
			String[] stdocIds = stdocIdArr.split("#");
			try {
				for (String stdocId : stdocIds) {
					if (StringUtils.isNotEmpty(stdocId)) {
						StdocInfo stdocInfo=stDocService.get(Integer.parseInt(stdocId));
						stdocInfo.setState(0);
						stDocService.update(stdocInfo);
						
						//deleteFile(stdocId);
						//stDocService.delete(Integer.valueOf(stdocId));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new CommonJsonModel(false, "删除失败！");
			}
			return new CommonJsonModel();
		}
		return new CommonJsonModel(false, "删除失败！");
	}
	@RequestMapping(value = "/stDocInfo/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel deleteFile(@RequestParam("patId") String patId) {
		// 把数据中file字段清空
		StdocInfo stdocInfo = stDocService.get(Integer.parseInt(patId));
		String fileStr = stdocInfo.getFileAddress();
		/*stdocInfo.setFileAddress(null);
		stDocService.update(stdocInfo);

		try {
			if (fileStr != null && !"".equals(fileStr.trim())) {
				SFTPChannel sftpchannel = new SFTPChannel();
				ChannelSftp channel = sftpchannel.getChannel();
				// 删除文件
				channel.rm(fileStr);
				// 删除文件夹
				StringBuffer fileBuf = new StringBuffer();
				String[] files = fileStr.split("/");
				for (int i = 0; i < files.length - 1; i++) {
					fileBuf.append(files[i]);
					if (i == files.length - 2)
						break;
					fileBuf.append("/");
				}
				channel.rmdir(fileBuf.toString());

				channel.quit();
				sftpchannel.closeChannel();
				return new CommonJsonModel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		String filePath = fileStr.substring(0, fileStr.lastIndexOf("/")+1);
		String filename = fileStr.substring(fileStr.lastIndexOf("/") + 1);
		FTPChannel.deleteFile(filePath, filename);	
		
		return new CommonJsonModel(false, "删除文件失败！");
	}
	/**
	 * 上传文件
	 * @param fileOne
	 * @param stDocInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stDocInfo/add.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView add(@RequestParam(value = "fileOne", required = false) CommonsMultipartFile fileOne,
			StdocInfo stDocInfo, HttpServletRequest request) {
		String stDoc = request.getParameter("stDoc");
		String comment=request.getParameter("comment");
		String author=request.getParameter("author");
		if (stDocInfo != null) {
				stDocInfo.setCreateTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));//上传时间
				UserBean userBean = (UserBean) request.getSession().getAttribute("user");
				stDocInfo.setCreateOper(userBean.getUserName());//创建人
				stDocInfo.setFileName(fileOne.getOriginalFilename());//文档名称
				stDocInfo.setComment(comment);
				stDocInfo.setAuthor(author);
				stDocInfo.setState(1);
				//add 添加标识，开发
				stDocInfo.setType(1);
				
				String fileAddress = "";
				/*try {
					if (!fileOne.isEmpty()) {
						SFTPChannel sftpchannel = new SFTPChannel();
						ChannelSftp channel = sftpchannel.getChannel();
						String serverDir = LoadPrefix.prefix + "stDocInfo/";
						try {
							channel.cd(serverDir);
						} catch (SftpException sException) {
							if (ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id) {
								channel.mkdir(serverDir);
							}
						}
						serverDir = serverDir + System.currentTimeMillis();
						channel.mkdir(serverDir);
						channel.cd(serverDir);
						fileAddress = serverDir + "/" + fileOne.getOriginalFilename();
						channel.put(fileOne.getInputStream(), fileAddress);
						channel.quit();
						sftpchannel.closeChannel();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (fileAddress != null && !"".equals(fileAddress.trim()))
					stDocInfo.setFileAddress(fileAddress);
				try {
					stDocService.save(stDocInfo);
				} catch (Exception e) {
					e.printStackTrace();
					return new ModelAndView("redirect:/mgr/" + stDoc);
				}*/
				
				String serverDir = LoadPrefix.prefix + "stDocInfo/";
				String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileOne.getOriginalFilename());
				fileAddress = serverDir+fileName;
				try {
					if(!fileOne.isEmpty()){
						File file = FileUtil.multipartToFile(fileOne);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (fileAddress != null && !"".equals(fileAddress.trim()))
							stDocInfo.setFileAddress(fileAddress);
					}
					
					stDocService.save(stDocInfo);
					/*----------添加上传审批申请数据------------*/
					StdocUploadcheck stdocUploadcheck=new StdocUploadcheck();
					stdocUploadcheck.setIsOk(0);
					stdocUploadcheck.setState(1);//表示发起申请
					stdocUploadcheck.setType(1);//标识，1为开发，2为实施
					stdocUploadcheck.setOper(userBean.getUserId());
					stdocUploadcheck.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					stdocUploadcheck.setStdocId(stDocInfo.getStdocId());//上传成果的id
					stdocUploadcheckService.save(stdocUploadcheck);
				} catch (IOException e) {
					e.printStackTrace();
					return new ModelAndView("redirect:/mgr/" + stDoc+"?a=2");
				}
				
				return new ModelAndView("redirect:/mgr/" + stDoc+"?a=1");
			}
		return new ModelAndView("redirect:/mgr/" + stDoc+"?a=2");
	}
	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/stDocInfo/downloadFile", method = RequestMethod.GET)
	@ResponseBody
	public void downloadReport(@RequestParam("fileAddress") String fileAddress, @RequestParam("stdocId") String stdocId, @RequestParam("flag") String flag,
			HttpServletRequest request, HttpServletResponse response) {
		/*ServletOutputStream os = null;
		try {
			SFTPChannel channel = new SFTPChannel();

			ChannelSftp chSftp = channel.getChannel();

			String filetype = fileAddress.substring(fileAddress.indexOf(".") + 1);
			String fileName = fileAddress.substring(fileAddress.lastIndexOf("/") + 1);

			String temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + "." + filetype;
			OutputStream out = new FileOutputStream(temppath);
			// 先把文件下载到容器里，然后用浏览器的流下载
			chSftp.get(fileAddress, out);
			chSftp.quit();
			out.close();
			// 浏览器的输出流
			os = response.getOutputStream();
			byte buffer[] = new byte[1024];
			File fileLoad = new File(temppath);
			// 清空输出流
			response.reset();
			response.addHeader("Content-Disposition", "attachment; filename =" + URLEncoder.encode(fileName, "UTF-8"));

			// 设置文件长度
			response.setContentLength((int) fileLoad.length());

			FileInputStream fis = new FileInputStream(fileLoad);

			int len;
			while ((len = fis.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			if (fileLoad.exists()) {
				fileLoad.delete();
			}
			fis.close();
			// 文件传输完毕之后，把应用下的临时文件删除掉，要在输入流关闭之后删除，否则删不掉
			if (fileLoad.exists()) {
				fileLoad.delete();
			}
			os.flush();
			

		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		
		StdocInfo si = stDocService.get(Integer.valueOf(stdocId));
		String fileUrl = si.getFileAddress();
		String fileName = si.getFileName();
		String destPath = this.servletContext.getRealPath("/") + fileName;
		FTPChannel.downloadFile(fileUrl, destPath, fileName, response);
		
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		if(!"a".equals(flag)){
			addDlNum(stdocId, userBean);
		}
	}
	
	
	
	private void addDlNum(String stdocId, UserBean userBean){
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("dlMan", userBean.getUserId()));
		query.add(Restrictions.eq("stdocId", Integer.parseInt(stdocId)));
		List<DocDlRc> lst = docDlRcService.queryListByCondition(query);
		if (lst == null || lst.size() == 0) {
			DocDlRc pdr = new DocDlRc();
			pdr.setDlId(Integer.parseInt(stdocId));
			pdr.setDlTime(DateUtil.getToday(DateUtil.DATE_PATTERN));
			pdr.setDlMan(userBean.getUserId());
			pdr.setDlNum(1);
			docDlRcService.save(pdr);
		} else {
			DocDlRc dlr = lst.get(0);
			dlr.setDlTime(DateUtil.getToday(DateUtil.DATE_PATTERN));
			dlr.setDlNum(dlr.getDlNum()+1);
			docDlRcService.update(dlr);
		}
	}
	
	/**
	 * 进行批量申请  开发
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stDocInfo/applyMore.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel applyMore(String stdocIdArr,HttpServletRequest request) {
		if(stdocIdArr!=null){
			String[] stdocIds = stdocIdArr.split("#");
//			for (String stdocId : stdocIds) {
//				/*--判断是否包含申请过patId----*/
//				ConditionQuery query = new ConditionQuery();
//				query.add(Restrictions.eq("stdocId", Integer.parseInt(stdocId)));
//				OrderBy order = new OrderBy();
//				order.add(Order.desc("time"));
//				List<StdocDowncheck> list=stdocDowncheckService.queryListByCondition(query, order);
//				Integer isOk=0;
//				Integer state=0;
//				if(list!=null&&list.size()>0){
//					StdocDowncheck stdocDowncheck=list.get(0);
//					isOk=stdocDowncheck.getIsOk();
//					state=stdocDowncheck.getState();
//				}
//				if(isOk==1||state==1){
//					return new CommonJsonModel(false,"请选择未申请过的数据");
//				}
//				/*--------------*/
//				StdocDowncheck stdocDowncheck=new StdocDowncheck();
//				stdocDowncheck.setStdocId(Integer.parseInt(stdocId));
//				stdocDowncheck.setState(1);
//				stdocDowncheck.setIsOk(0);
//				stdocDowncheck.setType(1);//标识，1为开发，2为实施
//				UserBean userBean = (UserBean) request.getSession().getAttribute("user");
//				stdocDowncheck.setOper(userBean.getUserId());
//				stdocDowncheck.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
//				stdocDowncheckService.save(stdocDowncheck);
//			}
			
			//获取成果id和cause，同时判断申请数据中有无包含已申请过的数据		
			int stdocId=0;
			String cause="";
			for (int i = 0; i < stdocIds.length; i++) {
				if(i%2==0){
					stdocId=Integer.parseInt(stdocIds[i]);
				}else{
					cause=stdocIds[i];
					StdocDowncheck checkLoad=new StdocDowncheck();
					checkLoad.setStdocId(stdocId);
					checkLoad.setState(1);
					checkLoad.setIsOk(0);
					checkLoad.setType(1);//研发
					checkLoad.setCause(cause);//申请下载的原因或用途
					UserBean userBean = (UserBean) request.getSession().getAttribute("user");
					checkLoad.setOper(userBean.getUserId());
					checkLoad.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					stdocDowncheckService.save(checkLoad);
				}
			}
			return new CommonJsonModel(true,"操作成功");
		}
		return new CommonJsonModel(false,"操作失败");
	}
	
	/**
	 * 进行批量申请  实施
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/stDocInfo/applyMore2.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel applyMore2(String stdocIdArr,HttpServletRequest request) {
		if(stdocIdArr!=null){
			String[] stdocIds = stdocIdArr.split("#");
			
			//获取成果id和cause，同时判断申请数据中有无包含已申请过的数据		
			int stdocId=0;
			String cause="";
			for (int i = 0; i < stdocIds.length; i++) {
				if(i%2==0){
					stdocId=Integer.parseInt(stdocIds[i]);
				}else{
					cause=stdocIds[i];
					StdocDowncheck checkLoad=new StdocDowncheck();
					checkLoad.setStdocId(stdocId);
					checkLoad.setState(1);
					checkLoad.setIsOk(0);
					checkLoad.setType(2);//实施
					checkLoad.setCause(cause);//申请下载的原因或用途
					UserBean userBean = (UserBean) request.getSession().getAttribute("user");
					checkLoad.setOper(userBean.getUserId());
					checkLoad.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					stdocDowncheckService.save(checkLoad);
				}
			}
			return new CommonJsonModel(true,"操作成功");
		}
		return new CommonJsonModel(false,"操作失败");
	}
	
	
	
	
}
