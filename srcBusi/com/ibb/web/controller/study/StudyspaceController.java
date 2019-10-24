package com.ibb.web.controller.study;

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
public class StudyspaceController implements ServletContextAware{
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
	@RequestMapping(value = "/studyspace")
	public String toProInfoMessagePage() {
		return "study/studyspace";
	}
	
	/**
	 * 查询项目信息列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/studyspace/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<StdocInfo> queryList(StdocInfo stdocInfo, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("createTime"));
		
		/*-----------------------*/
		//add 查询条件添加type=3  乐学空间文档
		query.add(Restrictions.eq("type", 3));

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
	@RequestMapping(value = "/studyspace/delete.json", method = RequestMethod.POST)
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
	@RequestMapping(value = "/studysapce/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel deleteFile(@RequestParam("patId") String patId) {
		// 把数据中file字段清空
		StdocInfo stdocInfo = stDocService.get(Integer.parseInt(patId));
		String fileStr = stdocInfo.getFileAddress();

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
	@RequestMapping(value = "/studyspace/add.json", method = RequestMethod.POST)
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
				//add 添加标识，乐学空间
				stDocInfo.setType(3);
				
				String fileAddress = "";
				
				String serverDir = LoadPrefix.prefix + "stssDocInfo/";
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
				} catch (IOException e) {
					e.printStackTrace();
					return new ModelAndView("redirect:/mgr/" + stDoc);
				}
				
				return new ModelAndView("redirect:/mgr/" + stDoc);
			}
		return new ModelAndView("redirect:/mgr/" + stDoc);
	}
	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/studyspace/downloadFile", method = RequestMethod.GET)
	@ResponseBody
	public void downloadReport(@RequestParam("fileAddress") String fileAddress, @RequestParam("stdocId") String stdocId, @RequestParam("flag") String flag,
			HttpServletRequest request, HttpServletResponse response) {
		
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
	
	
	
	
	
	
}
