package com.ibb.web.controller.projectMessage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
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
import com.ibb.model.projectMessage.ProSourceCode;
import com.ibb.service.projectMessage.IProInfoService;
import com.ibb.service.projectMessage.IProMemberService;
import com.ibb.service.projectMessage.IProSourceCodeService;
import com.ibb.sys.model.UserBean;
import com.ibb.utils.FileUtil;

/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午3:08:31
 *
 */
@Controller
public class ProSourceCodeController implements ServletContextAware {
	@Autowired
	private IProInfoService proInfoService;
	@Autowired
	private IProMemberService proMemberService;
	@Autowired
	private IProSourceCodeService proSourceCodeService;
		
	private ServletContext servletContext;
	
	@Autowired
    @Qualifier("hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	/**
	 * 实现了ServletContextAware接口，就可以通过这样获得servletContext
	 */
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	
	@RequestMapping(value = "/proSourceCode")
	public String toCompanyInfoPage() {
		return "proInfoMessage/proSourceCode";
	}
	
	/**
	 * 查询信息列表	 分页查询+模糊查询
	 * @param proSourceCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proSourceCode/listByLike.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProSourceCode> queryListByLike(ProSourceCode proSourceCode, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("submittime"));
		// 源码名称
		if (StringUtils.isNotEmpty(proSourceCode.getCodeName())) {
			query.add(Restrictions.like("codeName", "%" + proSourceCode.getCodeName().trim() + "%"));
		}
		// 源码类型
		if (StringUtils.isNotEmpty(proSourceCode.getCodeType())) {
			query.add(Restrictions.like("codeType", "%" + proSourceCode.getCodeType().trim() + "%"));
		}
		query.add(Restrictions.eq("state", 1));
		Page<ProSourceCode> page = proSourceCodeService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<ProSourceCode>(page);
	}
	
	/**
	 * 查询信息列表	根据项目编号查询+分页查询
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proSourceCode/listByProNum.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProSourceCode> queryListByProNum(@RequestParam(value = "proNum", required = false)String proNum, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("submittime"));
		// 项目编号
		if (StringUtils.isNotEmpty(proNum)) {
			query.add(Restrictions.eq("proNum", proNum));
		}
		query.add(Restrictions.eq("state", 1));
		Page<ProSourceCode> page = proSourceCodeService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<ProSourceCode>(page);
	}
	
	/**
	 * 增加信息
	 * 
	 * @param proSourceCode
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proSourceCode/add.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView add(@RequestParam(value = "fileThree", required = false) CommonsMultipartFile fileThree, ProSourceCode proSourceCode, HttpServletRequest request) {
		String pro = request.getParameter("pro");
		if(proSourceCode!=null){
			if(proSourceCode.getCodeName()!=null&&proSourceCode.getCodeType()!=null){
				UserBean userBean = (UserBean) request.getSession().getAttribute("user");
				proSourceCode.setSubmittime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
				proSourceCode.setSubmitOper(userBean.getUserName());
				proSourceCode.setState(1);
				String codeDiscFile = "";
				String serverDir = LoadPrefix.prefix + "proSourceInfo/";
				String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileThree.getOriginalFilename());
				codeDiscFile = serverDir+fileName;
				try {
					if(!fileThree.isEmpty()){
						File file = FileUtil.multipartToFile(fileThree);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (codeDiscFile != null && !"".equals(codeDiscFile.trim()))
							proSourceCode.setCodeDiscFile(codeDiscFile);
						proSourceCode.setFileName(fileThree.getOriginalFilename());
					}
					String codeNum = "codeNum";
					String randomProNum;
					while(true){
						randomProNum = ""+System.currentTimeMillis()+new Random().nextInt(1000000);
						codeNum = codeNum + randomProNum;
						
						ConditionQuery query = new ConditionQuery();
						query.add(Restrictions.eq("proNum", codeNum));
						
						if(proSourceCodeService.queryListByCondition(query)==null||proSourceCodeService.queryListByCondition(query).size()<=0){
							break;
						}
					}
					proSourceCode.setCodeNum(codeNum);
					proSourceCodeService.save(proSourceCode);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new ModelAndView("redirect:/mgr/"+pro);
			}
		}
		return new ModelAndView("redirect:/mgr/"+pro);
	}
	
	/**
	 * 更新信息
	 * 
	 * @param proSourceCode
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proSourceCode/update.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView update(@RequestParam(value = "fileFour", required = false) CommonsMultipartFile fileFour, ProSourceCode proSourceCode, HttpServletRequest request) {
		String pro = request.getParameter("pro");
		if (proSourceCode != null) {
			if(proSourceCode.getCodeName()!=null&&proSourceCode.getCodeType()!=null){
				String codeDiscFile = "";
				if(!fileFour.isEmpty()){
					ProSourceCode tempObject = proSourceCodeService.get(proSourceCode.getFileId());
					String attachAddress2 = tempObject.getCodeDiscFile();
					if (attachAddress2 != null && !"".equals(attachAddress2)) {
						deleteFile(String.valueOf(proSourceCode.getFileId()));
					}
					String serverDir = LoadPrefix.prefix + "proSourceInfo/";
					String fileName = System.currentTimeMillis() + "." + FileUtil.getPostfix(fileFour.getOriginalFilename());
					codeDiscFile = serverDir+fileName;
					try {
						File file = FileUtil.multipartToFile(fileFour);
						FTPChannel.uploadFile(file, serverDir, fileName);
						if (codeDiscFile != null && !"".equals(codeDiscFile.trim()))
							proSourceCode.setCodeDiscFile(codeDiscFile);
						proSourceCode.setFileName(fileFour.getOriginalFilename());
						proSourceCode.setState(1);
						proSourceCodeService.update(proSourceCode);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					ConditionQuery query = new ConditionQuery();
					query.add(Restrictions.like("fileId", proSourceCode.getFileId()));
					List<ProSourceCode> lst  = proSourceCodeService.queryListByCondition(query);
					proSourceCode.setCodeDiscFile(lst.get(0).getCodeDiscFile());
					proSourceCode.setState(1);
					proSourceCode.setFileName(lst.get(0).getFileName());
					proSourceCodeService.update(proSourceCode);
				}
				return new ModelAndView("redirect:/mgr/"+pro);
			}
		}
		return new ModelAndView("redirect:/mgr/"+pro);
	}
	
	/**
	 * 删除记录
	 * 
	 * @param proSourceCodeIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proSourceCode/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String proSourceCodeIdArr, HttpServletRequest request) {
		if(proSourceCodeIdArr!=null){
			String[] proSourceCodeIds = proSourceCodeIdArr.split("#");
			try {
				for (String proSourceCodeId : proSourceCodeIds) {
					if (StringUtils.isNotEmpty(proSourceCodeId)) {
						ProSourceCode proSourceCode=proSourceCodeService.get(Integer.parseInt(proSourceCodeId));
						proSourceCode.setState(0);
						proSourceCodeService.update(proSourceCode);
						//deleteFile(proSourceCodeId);
						//proSourceCodeService.delete(Integer.valueOf(proSourceCodeId));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new CommonJsonModel(false,"更新失败！");
			}
			return new CommonJsonModel();
		}
		return new CommonJsonModel(false,"更新失败！");
	}
	
	/**
	 * 删除一个文件
	 * 
	 * @param 文件路径
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proSourceCode/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel deleteFile(@RequestParam("sourId") String sourId) {
		// 把数据中file字段清空
		ProSourceCode proSourceCode = proSourceCodeService.get(Integer.parseInt(sourId));
		String fileStr = proSourceCode.getCodeDiscFile();
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
	@RequestMapping(value = "/proSourceCode/downloadFile", method = RequestMethod.GET)
	@ResponseBody
	public void downloadReport(@RequestParam("filePath")String filePath, @RequestParam("fileId") String fileId,
			HttpServletResponse response) {
		ProSourceCode psc = proSourceCodeService.get(Integer.valueOf(fileId));
		String fileUrl = psc.getCodeDiscFile();
		String fileName = psc.getFileName();
		String destPath = this.servletContext.getRealPath("/") + fileName;
		FTPChannel.downloadFile(fileUrl, destPath, fileName, response);
	}
	
}