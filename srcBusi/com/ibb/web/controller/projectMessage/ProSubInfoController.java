package com.ibb.web.controller.projectMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
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
import com.ibb.model.projectMessage.ProSubInfo;
import com.ibb.service.projectMessage.IProSubInfoService;
import com.ibb.utils.FileUtil;

@Controller
public class ProSubInfoController implements ServletContextAware {

	@Autowired
	private IProSubInfoService proSubInfoService;

	@SuppressWarnings("unused")
	private ServletContext servletContext;

	@Autowired
	@Qualifier("hibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.servletContext = arg0;
	}

	@RequestMapping(value = "/proSubInfo")
	public String toProInfoMessagePage() {
		return "proInfoMessage/proSubInfo";
	}

	@RequestMapping(value = "/proSubInfo/getList.json", method = RequestMethod.POST)
	@ResponseBody
	public List<ProSubInfo> getList(HttpServletRequest request) {
		List<ProSubInfo> list = proSubInfoService.queryListAll();
		return list;
	}

	/**
	 * 查询信息列表 根据项目编号查询+分页查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proSubInfo/listByProNum.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProSubInfo> queryListByProNum(
			@RequestParam(value = "proNum", required = false) String proNum, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("signTime"));
		// 项目编号
		if (StringUtils.isNotEmpty(proNum)) {
			query.add(Restrictions.eq("proNum", proNum));
		}
		query.add(Restrictions.eq("state", 1));
		Page<ProSubInfo> page = proSubInfoService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<ProSubInfo>(page);
	}

	/**
	 * 增加信息
	 * 
	 * @param proFileInfo
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proSubInfo/add.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView add(@RequestParam(value = "fileOne6", required = false) CommonsMultipartFile fileOne6,
			ProSubInfo proSubInfo, HttpServletRequest request) {
		String pro = request.getParameter("pro");
		if (proSubInfo != null) {

			if (proSubInfo.getProNum() != null) {

				String attachAddress = "";
				
				String serverDir = LoadPrefix.prefix + "proSubInfo/";
				String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileOne6.getOriginalFilename());
				attachAddress = serverDir+fileName;
				try {
					if(!fileOne6.isEmpty()){
						File file = FileUtil.multipartToFile(fileOne6);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (attachAddress != null && !"".equals(attachAddress.trim()))
							proSubInfo.setFilePath(attachAddress);
						proSubInfo.setFileName(fileOne6.getOriginalFilename());
					}
					proSubInfo.setState(1);
					proSubInfoService.save(proSubInfo);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return new ModelAndView("redirect:/mgr/" + pro);
			}
		}
		return new ModelAndView("redirect:/mgr/" + pro);
	}

	/**
	 * 更新信息
	 * 
	 * @param proFileInfo
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proSubInfo/update.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView update(@RequestParam(value = "fileTwo", required = false) CommonsMultipartFile fileTwo,
			ProSubInfo proSubInfo, HttpServletRequest request) {
		String pro = request.getParameter("pro");
		if (proSubInfo != null) {
			if (proSubInfo.getProNum() != null) {

				String attachAddress = "";
				
				if (!fileTwo.isEmpty()) {
					ProSubInfo tempObject = proSubInfoService.get(proSubInfo.getSubId());
					// 先删除原来的文件，然后再上传新的文件
					String attachAddress2 = tempObject.getFilePath();
					if (attachAddress2 != null && !"".equals(attachAddress2)) {
						deleteFile(String.valueOf(proSubInfo.getSubId()));
					}
					String serverDir = LoadPrefix.prefix + "proSubInfo/";
					String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileTwo.getOriginalFilename());
					attachAddress = serverDir+fileName;
					try {
						File file = FileUtil.multipartToFile(fileTwo);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (attachAddress != null && !"".equals(attachAddress.trim()))
							proSubInfo.setFilePath(attachAddress);
						proSubInfo.setFileName(fileTwo.getOriginalFilename());
						proSubInfo.setState(1);
						proSubInfoService.update(proSubInfo);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					ConditionQuery query = new ConditionQuery();
					query.add(Restrictions.like("subId", proSubInfo.getSubId()));
					List<ProSubInfo> lst  = proSubInfoService.queryListByCondition(query);
					proSubInfo.setFilePath(lst.get(0).getFilePath());
					proSubInfo.setFileName(lst.get(0).getFileName());
					proSubInfoService.update(proSubInfo);
				}
				
				return new ModelAndView("redirect:/mgr/" + pro);
			}
		}
		return new ModelAndView("redirect:/mgr/" + pro);
	}

	/**
	 * 删除记录
	 * 
	 * @param proSubInfoIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proSubInfo/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String proSubInfoIdArr, HttpServletRequest request) {
		if (proSubInfoIdArr != null) {
			String[] proSubInfoIds = proSubInfoIdArr.split("#");
			try {
				for (String proSubInfoId : proSubInfoIds) {
					if (StringUtils.isNotEmpty(proSubInfoId)) {
						/*---------数据备份----------*/
						ProSubInfo proSubInfo=proSubInfoService.get(Integer.parseInt(proSubInfoId));
						proSubInfo.setState(0);
						proSubInfoService.update(proSubInfo);
						//deleteFile(proSubInfoId);
						//proSubInfoService.delete(Integer.valueOf(proSubInfoId));
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

	@RequestMapping(value = "/proSubInfo/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel deleteFile(@RequestParam("subId") String subId) {
		// 把数据中file字段清空
		ProSubInfo proSubInfo = proSubInfoService.get(Integer.parseInt(subId));
		String fileStr = proSubInfo.getFilePath();
		String filePath = fileStr.substring(0, fileStr.lastIndexOf("/")+1);
		String filename = fileStr.substring(fileStr.lastIndexOf("/") + 1);
		FTPChannel.deleteFile(filePath, filename);
		return new CommonJsonModel();
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/proSubInfo/downloadFile", method = RequestMethod.GET)
	@ResponseBody
	public void downloadReport(@RequestParam("filePath")String filePath, @RequestParam("subId") String subId,
			HttpServletResponse response) {
		ProSubInfo psi = proSubInfoService.get(Integer.valueOf(subId));
		String fileUrl = psi.getFilePath();
		String fileName = psi.getFileName();
		String destPath = this.servletContext.getRealPath("/") + fileName;
		FTPChannel.downloadFile(fileUrl, destPath, fileName, response);
	}
	
	
}
