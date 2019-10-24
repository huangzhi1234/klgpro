package com.ibb.web.controller.projectMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONArray;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.DateUtil;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.ftp.FTPChannel;
import com.ibb.ftp.LoadPrefix;
import com.ibb.ftp.SFTPChannel;
import com.ibb.model.datadic.TblDicInfo;
import com.ibb.model.projectMessage.ProFile;
import com.ibb.model.projectMessage.ProSubInfo;
import com.ibb.readonline.Office2Swf;
import com.ibb.service.datadic.impl.TblDicInfoService;
import com.ibb.service.projectMessage.impl.ProFileService;
import com.ibb.sys.model.UserBean;
import com.ibb.utils.FileUtil;
import com.jcraft.jsch.ChannelSftp;

@Controller
public class ProFileController implements ServletContextAware{
	@Autowired
	private ProFileService proFileService;
	@Autowired
	private TblDicInfoService tblDicInfoService;
	@SuppressWarnings("unused")
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	@RequestMapping(value = "/proFile")
	public String toCompanyInfoPage() {
		return "proInfoMessage/proFile";
	}
	
	
	/**
	 * 查询信息列表	根据项目编号查询
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proFile/listByProNum.json", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray listByProNum(@RequestParam(value = "proNum", required = false)String proNum, @RequestParam(value = "parentId", required = false)String parentId,HttpServletRequest request) {
		JSONArray array=new JSONArray();
		array=proFileService.getMenu(Integer.parseInt(parentId),proNum);
		return array;
	}
	/**
	 * 增加信息
	 * 
	 * @param proFileInfo
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proFile/add.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView add(@RequestParam(value = "fileOne", required = false) CommonsMultipartFile fileOne, ProFile proFile, HttpServletRequest request) {
		String pro = request.getParameter("pro");
		if(proFile!=null){
			if(proFile.getProNum()!=null&&proFile.getFileName()!=null){
				UserBean userBean = (UserBean) request.getSession().getAttribute("user");
				ProFile proFile1=new ProFile();
				proFile1.setSubmitTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));//上传时间
				proFile1.setSubmitOper(userBean.getUserName());//上传人
				proFile1.setParentId(Integer.parseInt(proFile.getFileId().toString()));//父节点id
				proFile1.setFileName(fileOne.getOriginalFilename());//文件名
				proFile1.setProNum(proFile.getProNum());
				proFile1.setState(1);
				/*=================================上传文件======================================*/
				String attachAddress = "";
				String serverDir = LoadPrefix.prefix + "proInfo/"+ proFile.getProNum();
				String stageDir=proFile.getFileName();//阶段名，以下是为了将阶段名改成与阶段文件夹名称一致
				List<TblDicInfo> lst  = tblDicInfoService.findChildListByDicNum("0106");
				if(lst!=null&&lst.size()>0){
					for(TblDicInfo tdi : lst){
						if(tdi.getDicName().equals(stageDir)){
							stageDir=tdi.getDicNum();
							break;
						}
					}
				}
				stageDir=serverDir+"/"+stageDir;
				String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileOne.getOriginalFilename());
				attachAddress = stageDir+"/"+fileName;
				try {
					File file = FileUtil.multipartToFile(fileOne);
					FTPChannel.uploadFile(file, stageDir, fileName);
					if (attachAddress != null && !"".equals(attachAddress.trim()))
						proFile1.setAddress(attachAddress);//上传路径
					proFile1.setFileName(fileOne.getOriginalFilename());
					proFileService.save(proFile1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return new ModelAndView("redirect:/mgr/"+pro);
			}
		}
		return new ModelAndView("redirect:/mgr/"+pro);
	}
	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/proFile/downloadFile", method = RequestMethod.GET)
	@ResponseBody
	public void downloadReport(@RequestParam("fileId")String fileId, HttpServletResponse response) {
		ProFile proFile=proFileService.get(Integer.parseInt(fileId));
		String fileUrl = proFile.getAddress();
		String fileName = proFile.getFileName();
		String destPath = this.servletContext.getRealPath("/") + fileName;
		FTPChannel.downloadFile(fileUrl, destPath, fileName, response);
	}
	/**
	 * 预览
	 * @param filePath
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/proFile/read", method = RequestMethod.GET)
	public ModelAndView read(@RequestParam("fileId")String fileId ,HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		ProFile proFile=proFileService.get(Integer.parseInt(fileId));
		String filePath=proFile.getAddress();
		try {
			SFTPChannel channel = new SFTPChannel();
			ChannelSftp chSftp = channel.getChannel();
			String filetype = filePath.substring(filePath.indexOf(".") + 1);//获取文件类型
			String temppath = "";
			if(filetype.equals("docx")||filetype.equals("doc")){
				temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + ".docx";
			}else if(filetype.equals("pdf")){
				temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + ".pdf";
			}else if(filetype.equals("xlsx")){
				temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + ".xlsx";
			}else if(filetype.equals("jpg")){
				temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + ".jpg";
			}
			OutputStream out = new FileOutputStream(temppath);
			chSftp.get(filePath, out);
			chSftp.quit();	
			out.close();
			String outFilePath = temppath.replace(new File(temppath).getName(), System.currentTimeMillis() + ".swf");
			outFilePath = Office2Swf.office2Swf(temppath, outFilePath);
			request.getSession().setAttribute("fileName", new File(outFilePath).getName());
				
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView();
		}
		return new ModelAndView("redirect:/mgr/readonline");
		//return new CommonJsonModel();
	}	
	
	
	
	/**
	 * 删除记录
	 * 
	 * @param proFileInfoIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proFile/delete", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String fileId, HttpServletRequest request) {	
			if (StringUtils.isNotEmpty(fileId)) {
				/*-----------做数据备份------------*/
				ProFile proFile=proFileService.get(Integer.parseInt(fileId));
				proFile.setState(0);
				proFileService.update(proFile);
				
				//deleteFile(fileId);
				//proFileService.delete(Integer.valueOf(fileId));
			}else{
				return new CommonJsonModel(false,"删除失败！");
			}
			return new CommonJsonModel();
	}
	
	/**
	 * 删除一个文件
	 * 
	 * @param 文件路径
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proFile/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel deleteFile(@RequestParam("fileId") String fileId) {
		// 把数据中file字段清空
		ProFile proFile=proFileService.get(Integer.parseInt(fileId));
		String fileStr =proFile.getAddress();
		String filePath = fileStr.substring(0, fileStr.lastIndexOf("/")+1);
		String filename = fileStr.substring(fileStr.lastIndexOf("/") + 1);
		FTPChannel.deleteFile(filePath, filename);
		return new CommonJsonModel();

	}
	
	

	
	
}
