package com.ibb.web.controller.patch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
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
import com.ibb.dao.patch.IUploadCheckDao;
import com.ibb.ftp.FTPChannel;
import com.ibb.ftp.LoadPrefix;
import com.ibb.ftp.SFTPChannel;
import com.ibb.model.patch.PatDownNum;
import com.ibb.model.patch.PatUpNum;
import com.ibb.model.patch.PatchDlRc;
import com.ibb.model.patch.PatchInfo;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.projectMessage.ProInfo;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.service.patch.IPatchDlRcService;
import com.ibb.service.patch.IPatchInfoService;
import com.ibb.service.patch.IUploadCheckService;
import com.ibb.sys.model.UserBean;
import com.ibb.utils.FileUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

@Controller
public class PatchInfoController implements ServletContextAware {

	@Autowired
	private IPatchInfoService patchInfoService;
	
	@Autowired
	private IUploadCheckService uploadCheckService;
	
	
	@Autowired
	private IPatchDlRcService patchDlRcService;

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

	@RequestMapping(value = "/patchInfo")
	public String toPatchInfoPage() {
		return "patch/patInfo";
	}
	
	@RequestMapping(value = "/spatchInfo")
	public String toSPatchInfoPage() {
		return "patch/spatInfo";
	}

	@RequestMapping(value = "/patchInfo/getList.json", method = RequestMethod.POST)
	@ResponseBody
	public List<PatchInfo> getList(ProInfo proInfo, HttpServletRequest request) {
		List<PatchInfo> list = patchInfoService.queryListAll();
		return list;
	}

	/**
	 * 查询研发成果信息列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/patchInfo/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<PatchInfo> queryList(PatchInfo patchInfo, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("upTime"));
		
		//批量申请下载弹窗页面信息
		
		/*---------进行过滤：查看是否上传通过审批------------*/
		ConditionQuery query1=new ConditionQuery();
		query1.add(Restrictions.eq("isOk", 1));
		query1.add(Restrictions.eq("type", 1));
		List<UploadCheck> list=uploadCheckService.queryListByCondition(query1);
		List<Integer> str=new ArrayList<Integer>();
		if(list!=null&&list.size()>0){
			for (UploadCheck uploadCheck : list) {
				str.add(uploadCheck.getPatId());
			}
		}
		if(str!=null&&str.size()>0){
			query.add(Restrictions.in("patId", str));
		}else{
			query.add(Restrictions.eq("patId", 0));
		}
		/*-----------------------*/
		
		
		// 补丁名称
		if (StringUtils.isNotEmpty(patchInfo.getPatName())) {
			query.add(Restrictions.like("patName", "%" + patchInfo.getPatName().trim() + "%"));
		}
		// 补丁所属业务模块
		if (StringUtils.isNotEmpty(patchInfo.getBusiMod())) {
			query.add(Restrictions.like("busiMod", "%" + patchInfo.getBusiMod().trim() + "%"));
		}
		// 补丁对应NC版本
		if (StringUtils.isNotEmpty(patchInfo.getNcVersion())) {
			query.add(Restrictions.like("ncVersion", "%" + patchInfo.getNcVersion().trim() + "%"));
		}
		//
		if (StringUtils.isNotEmpty(patchInfo.getPatMan())) {
			query.add(Restrictions.like("patMan", "%" + patchInfo.getPatMan().trim() + "%"));
		}
		//
		if (StringUtils.isNotEmpty(patchInfo.getDicNum())) {
			query.add(Restrictions.like("dicNum", patchInfo.getDicNum().trim() + "%"));
		}
	

		
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 1));//1表示研发
		Page<PatchInfo> page = patchInfoService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<PatchInfo>(page);
	}
	
	/**
	 * 查询下载申请列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/patchInfo/applyDownList.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<PatchInfo> applyDownList(PatchInfo patchInfo, HttpServletRequest request){
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("upTime"));
		String patIdArr=request.getParameter("patIdArr");
		if(patIdArr!=null){
			String[] patIds=patIdArr.split("aaa");
			List<Integer> idList=new ArrayList<Integer>();
			if (patIds!=null) {
				for (String patId : patIds) {
					idList.add(Integer.parseInt(patId));
				}
				if(idList!=null&&idList.size()>0){
					query.add(Restrictions.in("patId", idList));
				}
			}
		}
		Page<PatchInfo> page = patchInfoService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<PatchInfo>(page);
	}
	
	
	/**
	 * 查询实施成果信息列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/patchInfo/list2.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<PatchInfo> queryList2(PatchInfo patchInfo, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("upTime"));

		/*---------进行过滤：查看是否上传通过审批------------*/
		ConditionQuery query1=new ConditionQuery();
		query1.add(Restrictions.eq("isOk", 1));
		query1.add(Restrictions.eq("type", 2));
		List<UploadCheck> list=uploadCheckService.queryListByCondition(query1);
		List<Integer> str=new ArrayList<Integer>();
		if(list!=null&&list.size()>0){
			for (UploadCheck uploadCheck : list) {
				str.add(uploadCheck.getPatId());
			}
		}
		if(str!=null&&str.size()>0){
			query.add(Restrictions.in("patId", str));
		}else{
			query.add(Restrictions.eq("patId", 0));
		}
		/*-----------------------*/
		
		// 补丁名称
		if (StringUtils.isNotEmpty(patchInfo.getPatName())) {
			query.add(Restrictions.like("patName", "%" + patchInfo.getPatName().trim() + "%"));
		}
		// 补丁所属业务模块
		if (StringUtils.isNotEmpty(patchInfo.getBusiMod())) {
			query.add(Restrictions.like("busiMod", "%" + patchInfo.getBusiMod().trim() + "%"));
		}
		// 补丁对应NC版本
		if (StringUtils.isNotEmpty(patchInfo.getNcVersion())) {
			query.add(Restrictions.like("ncVersion", "%" + patchInfo.getNcVersion().trim() + "%"));
		}
		//
		if (StringUtils.isNotEmpty(patchInfo.getPatMan())) {
			query.add(Restrictions.like("patMan", "%" + patchInfo.getPatMan().trim() + "%"));
		}
		//
		if (StringUtils.isNotEmpty(patchInfo.getDicNum())) {
			query.add(Restrictions.like("dicNum", patchInfo.getDicNum().trim() + "%"));
		}
		query.add(Restrictions.eq("state", 1));
		query.add(Restrictions.eq("type", 2));//2表示实施
		Page<PatchInfo> page = patchInfoService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<PatchInfo>(page);
	}
	
	
	/**
	 * 
	 * 研发成果上传部分
	 * @param fileOne
	 * @param patchInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/patchInfo/add.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView add(@RequestParam(value = "fileOne", required = false) CommonsMultipartFile fileOne,
			PatchInfo patchInfo, HttpServletRequest request) {
		String patch = request.getParameter("patch");

		if (patchInfo != null) {

			String patName = patchInfo.getPatName();
			String eamil = patchInfo.getEmail();
			String patDis = patchInfo.getPatDis();
			if (patName != null && !"".equals(patName.trim()) 
					&& patDis != null && !"".equals(patDis.trim())) {
				patchInfo.setUpTime(DateUtil.getToday(DateUtil.DATE_PATTERN));
				UserBean userBean = (UserBean) request.getSession().getAttribute("user");
				patchInfo.setUpMan(userBean.getUserId());
				String fileAddress = "";
				String serverDir = LoadPrefix.prefix + "patchInfo/";
				String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileOne.getOriginalFilename());
				fileAddress = serverDir+fileName;
				try {
					if(!fileOne.isEmpty()){
						File file = FileUtil.multipartToFile(fileOne);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (fileAddress != null && !"".equals(fileAddress.trim()))
							patchInfo.setFile(fileAddress);
						patchInfo.setFileName(fileOne.getOriginalFilename());
					}
					patchInfo.setType(1);//表明该成果为研发成果
					patchInfo.setState(1);
					patchInfoService.save(patchInfo);
					/*----------添加上传审批申请数据------------*/
					UploadCheck uploadCheck=new UploadCheck();
					uploadCheck.setIsOk(0);
					uploadCheck.setState(1);//表示发起申请
					uploadCheck.setOper(userBean.getUserId());
					uploadCheck.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					uploadCheck.setPatId(patchInfo.getPatId());//上传成果的id
					uploadCheck.setType(1);//表示是研发
					uploadCheckService.save(uploadCheck);
				} catch (IOException e) {
					e.printStackTrace();
					return new ModelAndView("redirect:/mgr/" + patch+"?a=2");
				}
				
				//return new CommonJsonModel(true,"操作成功，请耐心等候审批");
				return new ModelAndView("redirect:/mgr/" + patch+"?a=1");
			}
		}
		//return new CommonJsonModel();
		return new ModelAndView("redirect:/mgr/" + patch+"?a=2");
	}

	/**
	 * 
	 * 实施成果上传部分
	 * @param fileOne
	 * @param patchInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/patchInfo/add2.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView add2(@RequestParam(value = "fileOne", required = false) CommonsMultipartFile fileOne,
			PatchInfo patchInfo, HttpServletRequest request) {
		String patch = request.getParameter("patch");

		if (patchInfo != null) {

			String patName = patchInfo.getPatName();
			String eamil = patchInfo.getEmail();
			String patDis = patchInfo.getPatDis();
			if (patName != null && !"".equals(patName.trim()) 
					&& patDis != null && !"".equals(patDis.trim())) {
				patchInfo.setUpTime(DateUtil.getToday(DateUtil.DATE_PATTERN));
				UserBean userBean = (UserBean) request.getSession().getAttribute("user");
				patchInfo.setUpMan(userBean.getUserId());
				String fileAddress = "";
				String serverDir = LoadPrefix.prefix + "patchInfo/";
				String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileOne.getOriginalFilename());
				fileAddress = serverDir+fileName;
				try {
					if(!fileOne.isEmpty()){
						File file = FileUtil.multipartToFile(fileOne);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (fileAddress != null && !"".equals(fileAddress.trim()))
							patchInfo.setFile(fileAddress);
						patchInfo.setFileName(fileOne.getOriginalFilename());
					}
					
					patchInfo.setState(1);
					patchInfo.setType(2);//表明该成果为实施成果
					patchInfoService.save(patchInfo);
					/*----------添加上传审批申请数据------------*/
					UploadCheck uploadCheck=new UploadCheck();
					uploadCheck.setIsOk(0);
					uploadCheck.setState(1);//表示发起申请
					uploadCheck.setOper(userBean.getUserId());
					uploadCheck.setTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
					uploadCheck.setPatId(patchInfo.getPatId());//上传成果的id
					uploadCheck.setType(2);//表示是实施
					uploadCheckService.save(uploadCheck);
				} catch (IOException e) {
					e.printStackTrace();
					return new ModelAndView("redirect:/mgr/" + patch+"?a=2");
				}
				
				//return new CommonJsonModel(true,"操作成功，请耐心等候审批");
				return new ModelAndView("redirect:/mgr/" + patch+"?a=1");
			}
		}
		//return new CommonJsonModel();
		return new ModelAndView("redirect:/mgr/" + patch+"?a=2");
	}
	
	
	/**
	 * 更新一条项目基本信息
	 * 
	 * @param ProInfo
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/patchInfo/update.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView update(@RequestParam(value = "fileTwo", required = false) CommonsMultipartFile fileTwo,
			PatchInfo patchInfo, HttpServletRequest request) {
		String patch = request.getParameter("patch");
		if (patchInfo != null) {
			String patName = patchInfo.getPatName();
			String eamil = patchInfo.getEmail();
			String patDis = patchInfo.getPatDis();
			if (patName != null && !"".equals(patName.trim()) && eamil != null && !"".equals(eamil.trim())
					&& patDis != null && !"".equals(patDis.trim())) {

				String fileAddress = "";
				
				
				if (!fileTwo.isEmpty()) {
					PatchInfo tempPatch = patchInfoService.get(patchInfo.getPatId());
					// 判断原来有没有上传文件，有则先删除原来上传的文件
					String tempFilePath = tempPatch.getFile();
					if (tempFilePath != null && !"".equals(tempFilePath.trim())) {
						deleteFile(String.valueOf(patchInfo.getPatId()));
					}
					String serverDir = LoadPrefix.prefix + "patchInfo/";
					String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileTwo.getOriginalFilename());
					fileAddress = serverDir+fileName;
					try {
						File file = FileUtil.multipartToFile(fileTwo);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (fileAddress != null && !"".equals(fileAddress.trim()))
							patchInfo.setFile(fileAddress);
						patchInfo.setFileName(fileTwo.getOriginalFilename());
						patchInfo.setState(1);
						patchInfo.setType(1);
						patchInfoService.update(patchInfo);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					ConditionQuery query = new ConditionQuery();
					query.add(Restrictions.like("patId", patchInfo.getPatId()));
					List<PatchInfo> lst  = patchInfoService.queryListByCondition(query);
					patchInfo.setFile(lst.get(0).getFile());
					patchInfo.setFileName(lst.get(0).getFileName());
					patchInfo.setState(1);
					patchInfo.setType(1);
					patchInfoService.update(patchInfo);
				}
				
				return new ModelAndView("redirect:/mgr/" + patch);
			}
		}
		return new ModelAndView("redirect:/mgr/" + patch);
	}
	
	
	/**
	 * 更新一条项目基本信息
	 * 
	 * @param ProInfo
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/patchInfo/update2.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView update2(@RequestParam(value = "fileTwo", required = false) CommonsMultipartFile fileTwo,
			PatchInfo patchInfo, HttpServletRequest request) {
		String patch = request.getParameter("patch");
		if (patchInfo != null) {
			String patName = patchInfo.getPatName();
			String eamil = patchInfo.getEmail();
			String patDis = patchInfo.getPatDis();
			if (patName != null && !"".equals(patName.trim()) && eamil != null && !"".equals(eamil.trim())
					&& patDis != null && !"".equals(patDis.trim())) {

				String fileAddress = "";
				
				
				if (!fileTwo.isEmpty()) {
					PatchInfo tempPatch = patchInfoService.get(patchInfo.getPatId());
					// 判断原来有没有上传文件，有则先删除原来上传的文件
					String tempFilePath = tempPatch.getFile();
					if (tempFilePath != null && !"".equals(tempFilePath.trim())) {
						deleteFile(String.valueOf(patchInfo.getPatId()));
					}
					String serverDir = LoadPrefix.prefix + "patchInfo/";
					String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileTwo.getOriginalFilename());
					fileAddress = serverDir+fileName;
					try {
						File file = FileUtil.multipartToFile(fileTwo);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (fileAddress != null && !"".equals(fileAddress.trim()))
							patchInfo.setFile(fileAddress);
						patchInfo.setFileName(fileTwo.getOriginalFilename());
						patchInfo.setState(1);
						patchInfo.setType(1);
						patchInfoService.update(patchInfo);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					ConditionQuery query = new ConditionQuery();
					query.add(Restrictions.like("patId", patchInfo.getPatId()));
					List<PatchInfo> lst  = patchInfoService.queryListByCondition(query);
					patchInfo.setFile(lst.get(0).getFile());
					patchInfo.setFileName(lst.get(0).getFileName());
					patchInfo.setState(1);
					patchInfo.setType(2);
					patchInfoService.update(patchInfo);
				}
				
				return new ModelAndView("redirect:/mgr/" + patch);
			}
		}
		return new ModelAndView("redirect:/mgr/" + patch);
	}

	/**
	 * 删除记录 联表删除
	 * 
	 * @param proIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/patchInfo/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String patchIdArr, HttpServletRequest request) {
		if (patchIdArr != null) {
			String[] patchIds = patchIdArr.split("#");
			try {
				for (String patchId : patchIds) {
					if (StringUtils.isNotEmpty(patchId)) {
						/*-------做数据备份：不删除信息和文件，将对象的标识改为0-----------*/
						PatchInfo patchInfo= patchInfoService.get(Integer.parseInt(patchId));
						patchInfo.setState(0);
						patchInfoService.update(patchInfo);
						//deleteFile(patchId);
						//patchInfoService.delete(Integer.valueOf(patchId));
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

	@RequestMapping(value = "/patchInfo/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel deleteFile(@RequestParam("patId") String patId) {
		// 把数据中file字段清空
		PatchInfo patchInfo = patchInfoService.get(Integer.parseInt(patId));
		String fileStr = patchInfo.getFile();
		/*patchInfo.setFile(null);
		patchInfoService.update(patchInfo);

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
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/patchInfo/downloadFile", method = RequestMethod.GET)
	@ResponseBody
	public void downloadReport(@RequestParam("filePath") String filePath, @RequestParam("patId") String patId,@RequestParam("flag") String flag,
			HttpServletRequest request, HttpServletResponse response) {
		/*ServletOutputStream os = null;
		try {
			SFTPChannel channel = new SFTPChannel();

			ChannelSftp chSftp = channel.getChannel();

			String filetype = filePath.substring(filePath.indexOf(".") + 1);
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

			String temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + "." + filetype;
			OutputStream out = new FileOutputStream(temppath);
			// 先把文件下载到容器里，然后用浏览器的流下载
			chSftp.get(filePath, out);
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
			 UserBean userBean = (UserBean)
			 request.getSession().getAttribute("user");
			 savePDR(patId,userBean);

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
		
		PatchInfo pi = patchInfoService.get(Integer.valueOf(patId));
		String fileUrl = pi.getFile();
		String fileName = pi.getFileName();
		String destPath = this.servletContext.getRealPath("/") + fileName;
		FTPChannel.downloadFile(fileUrl, destPath, fileName, response);
		
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		if(!"a".equals(flag)){
			addDlNum(patId, userBean);
		}
		
	}

	private void savePDR(String patId, UserBean userBean) {
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("dlMan", userBean.getUserId()));
		query.add(Restrictions.eq("patId", Integer.parseInt(patId)));
		List<PatchDlRc> lst = patchDlRcService.queryListByCondition(query);
		if (lst == null || lst.size() == 0) {
			PatchDlRc pdr = new PatchDlRc();
			pdr.setPatId(Integer.parseInt(patId));
			pdr.setDlTime(DateUtil.getToday(DateUtil.DATE_PATTERN));
			pdr.setDlMan(userBean.getUserId());
			patchDlRcService.save(pdr);
		} else {
			PatchDlRc pdr = lst.get(0);
			pdr.setDlTime(DateUtil.getToday(DateUtil.DATE_PATTERN));
			patchDlRcService.update(pdr);
		}
	}

	
	private void addDlNum(String patId, UserBean userBean){
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("dlMan", userBean.getUserId()));
		query.add(Restrictions.eq("patId", Integer.parseInt(patId)));
		List<PatchDlRc> lst = patchDlRcService.queryListByCondition(query);
		if (lst == null || lst.size() == 0) {
			PatchDlRc pdr = new PatchDlRc();
			pdr.setPatId(Integer.parseInt(patId));
			pdr.setDlTime(DateUtil.getToday(DateUtil.DATE_PATTERN));
			pdr.setDlMan(userBean.getUserId());
			pdr.setDlNum(1);
			patchDlRcService.save(pdr);
		} else {
			PatchDlRc pdr = lst.get(0);
			pdr.setDlTime(DateUtil.getToday(DateUtil.DATE_PATTERN));
			pdr.setDlNum(pdr.getDlNum()+1);
			patchDlRcService.update(pdr);
		}
	}
	
}
