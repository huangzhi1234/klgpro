package com.ibb.web.controller.projectMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.DateUtil;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.ftp.FTPChannel;
import com.ibb.ftp.LoadPrefix;
import com.ibb.model.datadic.TblDicInfo;
import com.ibb.model.projectMessage.ProCustomerMember;
import com.ibb.model.projectMessage.ProFile;
import com.ibb.model.projectMessage.ProInfo;
import com.ibb.model.projectMessage.ProMember;
import com.ibb.model.projectMessage.ProSourceCode;
import com.ibb.model.projectMessage.ProSubInfo;
import com.ibb.model.projectMessage.UserProBean;
import com.ibb.service.datadic.impl.TblDicInfoService;
import com.ibb.service.projectMessage.IProCustomerMemberService;
import com.ibb.service.projectMessage.IProFileService;
import com.ibb.service.projectMessage.IProInfoService;
import com.ibb.service.projectMessage.IProMemberService;
import com.ibb.service.projectMessage.IProSourceCodeService;
import com.ibb.service.projectMessage.IProSubInfoService;
import com.ibb.service.projectMessage.IUserProService;
import com.ibb.sys.model.UserBean;
import com.jcraft.jsch.ChannelSftp;

/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午3:08:31
 *
 */
@Controller
public class ProInfoController implements ServletContextAware {
	@Autowired
	private IProInfoService proInfoService;
	@Autowired
	private IProMemberService proMemberService;
	@Autowired
	private IProFileService proFileService;
	@Autowired
	private IProSourceCodeService proSourceCodeService;
	@Autowired
	private TblDicInfoService tblDicInfoService;
	@Autowired
	private IProSubInfoService proSubInfoService;
	@Autowired
	private IUserProService userProService;
	@Autowired
	private IProCustomerMemberService proCustomerMemberService;
	@SuppressWarnings("unused")
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
	
	@RequestMapping(value = "/proInfo")
	public String toProInfoMessagePage() {
		return "proInfoMessage/proInfo";
	}
	
	@RequestMapping(value = "/proInfo/add.jsp")
	public String toProInfoAdd() {
		return "proInfoMessage/proInfoAdd";
	}
	
	@RequestMapping(value = "/proInfo/getList.json", method = RequestMethod.POST)
	@ResponseBody
	public List<ProInfo> getList(ProInfo proInfo, HttpServletRequest request) {
		List<ProInfo> list = proInfoService.queryListAll();
		return list;
	}
	
	/**
	 * 查询项目信息列表	分页查询+模糊查询
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proInfo/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProInfo> queryList(ProInfo proInfo, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("createTime"));
		
		// 项目名称
		if (StringUtils.isNotEmpty(proInfo.getProName())) {
			query.add(Restrictions.like("proName", "%" + proInfo.getProName().trim() + "%"));
		}
		// 项目所属行业
		if (StringUtils.isNotEmpty(proInfo.getProIndu())) {
			query.add(Restrictions.like("proIndu", "%" + proInfo.getProIndu().trim() + "%"));
		}
		// 产品线
		if (StringUtils.isNotEmpty(proInfo.getProProductLine())) {
			query.add(Restrictions.like("proProductLine", "%" + proInfo.getProProductLine().trim() + "%"));
		}
		/*// 启动时间
		if (StringUtils.isNotEmpty(proInfo.getStartTime())) {
			query.add(Restrictions.like("startTime", "%" + proInfo.getStartTime().trim() + "%"));
		}
		
		// 验收时间
		if (StringUtils.isNotEmpty(proInfo.getFinishTime())) {
			query.add(Restrictions.like("finishTime", "%" + proInfo.getFinishTime().trim() + "%"));
		}*/
		List<TblDicInfo> lst  = tblDicInfoService.findChildListByDicNum("0106");
		if(lst!=null&&lst.size()>0){
			for(TblDicInfo tdi : lst){
				System.out.println(tdi.getDicName());
			}
		}
		/*------进行过滤--------*/
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");
		int userId=userBean.getUserId();
		ConditionQuery query1=new ConditionQuery();
		query1.add(Restrictions.eq("userId", userId));
		List<UserProBean> list=userProService.queryListByCondition(query1);
		List<Integer> str=new ArrayList<Integer>();
		if(list!=null&&list.size()!=0){
			for (UserProBean userProBean : list) {
				str.add(userProBean.getProId());
			}
		}
		query.add(Restrictions.in("proId",str));
		/*-----------------------*/
		query.add(Restrictions.eq("state", 1));
		Page<ProInfo> page = proInfoService.queryListByCondition(query, order, pn, pageSize);
		return new EasyUIGridJsonModel<ProInfo>(page);
	}
	
	/**
	 * 增加一条项目基本信息
	 * 
	 * @param ProInfo
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proInfo/add.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel add(ProInfo proInfo, HttpServletRequest request) {
		
		if(proInfo!=null){
			UserBean userBean = (UserBean) request.getSession().getAttribute("user");
			proInfo.setCreateTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));
			proInfo.setCreateOper(userBean.getUserName());
			int userId = userBean.getUserId();
			if(proInfo.getProName()!=null){
				
				String proNum = "pn";
				String randomProNum;
				
				while(true){
					randomProNum = ""+System.currentTimeMillis()+new Random().nextInt(100);
					proNum = proNum + randomProNum;
					
					ConditionQuery query = new ConditionQuery();
					query.add(Restrictions.eq("proNum", proNum));
					
					if(proInfoService.queryListByCondition(query)==null||proInfoService.queryListByCondition(query).size()<=0){
						break;
					}
				}
				proInfo.setProNum(proNum);
				proInfo.setState(1);
				try{
					proInfoService.save(proInfo);
					/*
					 * ==========================对创建人和管理员的查看项目进行授权==============
					 * =======
					 */
					UserProBean userPro = new UserProBean();
					userPro.setProId(proInfo.getProId());
					userPro.setUserId(userId);
					userProService.save(userPro);
					
					/*============================生成项目文档文件夹========================================*/					
					/*SFTPChannel sftpChannel=new SFTPChannel();
					ChannelSftp channel=sftpChannel.getChannel();
					String serverDir=LoadPrefix.prefix+"proInfo/";
					
					try {
						channel.cd(serverDir);
					} catch (SftpException sException) {
						if(ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id){						    
						    channel.mkdir(serverDir);
						}
					}*/
					String serverDir=LoadPrefix.prefix+"proInfo/";
					
					serverDir=serverDir+proNum;
					/*channel.mkdir(serverDir);//创建项目编号文件夹
					channel.cd(serverDir);//进入项目编号文件夹
*/					
					
					List<TblDicInfo> lst  = tblDicInfoService.findChildListByDicNum("0106");
					if(lst!=null&&lst.size()>0){
						for(TblDicInfo tdi : lst){
							String stageDir=serverDir+"/"+tdi.getDicNum();
							/*channel.mkdir(stageDir);//创建阶段文件夹
							channel.cd(stageDir);//进入阶段文件夹
*/							
							FTPChannel.makeDirectory(stageDir);
							
							//于此同时，在每个阶段，生成一个项目文档信息对象，保存到数据库
							/*ProFileInfo proFileInfo=new ProFileInfo();
							proFileInfo.setProStage(tdi.getDicNum());//项目阶段
							proFileInfo.setProNum(proInfo.getProNum());//项目编号
							proFileInfo.setCreateOper(userBean.getUserName());//创建人
							proFileInfo.setCreateTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));//创建时间
							proFileInfoService.save(proFileInfo);//保存
*/							
							ProFile proFile=new ProFile();
							proFile.setParentId(0);//父节点为0
							proFile.setProNum(proInfo.getProNum());//项目编号
							proFile.setFileName(tdi.getDicNum());//此时表示项目阶段
							proFile.setSubmitOper(userBean.getUserName());//此时表示创建人
							proFile.setSubmitTime(DateUtil.getToday(DateUtil.DEFAULT_STRINGFORMAT_PATTERN));//此时表示创建时间
							proFile.setState(1);
							proFileService.save(proFile);//保存
							
						}
					}
					/*channel.quit();
					sftpChannel.closeChannel();*/
					
				
					
				} catch(Exception e){
					e.printStackTrace();
					return new CommonJsonModel(false,"增加失败！");
				}
				return new CommonJsonModel();
			}
		}
		return new CommonJsonModel(false,"增加失败！");
	}
	
	/**
	 * 更新一条项目基本信息
	 * 
	 * @param ProInfo
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proInfo/update.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(ProInfo proInfo, HttpServletRequest request) {
		if (proInfo != null) {
			if(proInfo.getProNum()!=null&&proInfo.getProPactNum()!=null&&proInfo.getProName()!=null){
				try{
					proInfo.setState(1);
					proInfoService.update(proInfo);
				} catch(Exception e){
					e.printStackTrace();
					return new CommonJsonModel(false,"更新失败！");
				}
				return new CommonJsonModel();
			}
		}
		return new CommonJsonModel(false,"更新失败！");
	}
	
	/**
	 * 删除记录 联表删除
	 * 
	 * @param proIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proInfo/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String proIdArr, HttpServletRequest request) {
		if(proIdArr!=null){
			String[] proIds = proIdArr.split("#");
			try {
				for (String proId : proIds) {
					if (StringUtils.isNotEmpty(proId)) {
						ProInfo proInfo = proInfoService.get(Integer.valueOf(proId));
						ConditionQuery query = new ConditionQuery();
						query.add(Restrictions.eq("proNum", proInfo.getProNum()));
						if(proInfo!=null&&proInfo.getProNum()!=null){
							/**
							 * 删除成员信息表
							 */
							List<ProMember> proMembers = proMemberService.queryListByCondition(query);
							for (ProMember proMember : proMembers) {
								proMember.setState(0);
								proMemberService.update(proMember);
								//proMemberService.delete(proMember.getMemberId());
							}
							/**
							 * 删除项目客户信息表
							 */
							List<ProCustomerMember> proCustomerMembers = proCustomerMemberService.queryListByCondition(query);
							for (ProCustomerMember proCustomerMember : proCustomerMembers) {
								proCustomerMember.setState(0);
								proCustomerMemberService.update(proCustomerMember);
								//proCustomerMemberService.delete(proCustomerMember.getCustomerId());
							}
							/**
							 * 删除项目文档信息表
							 */
							List<ProFile> proFileInfos = proFileService.queryListByCondition(query);
							for (ProFile proFileInfo : proFileInfos) {
								proFileInfo.setState(0);
								proFileService.update(proFileInfo);
								//proFileService.delete(proFileInfo.getFileId());
							}
							FTPChannel.deleteDirectory(LoadPrefix.prefix + "proInfo/"+proInfo.getProNum());
							/**
							 * 删除项目子合同信息表
							 */
							List<ProSubInfo> proSubInfos = proSubInfoService.queryListByCondition(query);
							for (ProSubInfo proSubInfo : proSubInfos) {
								proSubInfo.setState(0);
								proSubInfoService.update(proSubInfo);
								//String fileStr = proSubInfo.getFilePath();
								//deleteFile(fileStr);
								//proSubInfoService.delete(proSubInfo.getSubId());
							}
							/**
							 * 删除项目源码信息表
							 */
							List<ProSourceCode> proSourceCodes = proSourceCodeService.queryListByCondition(query);
							for (ProSourceCode proSourceCode : proSourceCodes) {
								proSourceCode.setState(0);
								proSourceCodeService.update(proSourceCode);
								//String fileStr = proSourceCode.getCodeDiscFile();
								//deleteFile(fileStr);
								//proSourceCodeService.delete(proSourceCode.getFileId());
							}
							/**
							 * 删除项目授权信息表
							 */
							ConditionQuery query1 = new ConditionQuery();
							query1.add(Restrictions.eq("proId", Integer.valueOf(proId)));
							List<UserProBean> userProList = userProService.queryListByCondition(query1);
							if (userProList != null && userProList.size() > 0) {
								for (UserProBean userProBean : userProList) {
									userProBean.setState(0);
									userProService.update(userProBean);
									//userProService.deleteObject(userProBean);
								}
							}
							/**
							 * 删除项目基本信息表
							 */
							ProInfo proInfo2=proInfoService.get(Integer.parseInt(proId));
							proInfo2.setState(0);
							proInfoService.update(proInfo2);
							//proInfoService.delete(Integer.valueOf(proId));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new CommonJsonModel(false,"删除失败！");
			}
			return new CommonJsonModel();
		}
		return new CommonJsonModel(false,"删除失败！");
	}
	
	/**
	 * 删除一个文件
	 * 
	 * @param 文件路径
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proInfo/deleteFile", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel deleteFile(@RequestParam("fileStr") String fileStr) {
		// 把数据中file字段清空
		//ProFileInfo proFileInfo = proFileInfoService.get(Integer.parseInt(sourId));
		//String fileStr = proFileInfo.getAttachAddress();
		//proFileInfo.setAttachAddress(null);
		//proFileInfoService.update(proFileInfo);

		/*try {
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
			return new CommonJsonModel(false, "删除文件失败！");
		}*/
		
		String filePath = fileStr.substring(0, fileStr.lastIndexOf("/")+1);
		String filename = fileStr.substring(fileStr.lastIndexOf("/") + 1);
		FTPChannel.deleteFile(filePath, filename);
		
		return new CommonJsonModel();

	}
	
	
	
	
	/**
	 * 查询项目名称	根据项目阶段
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proInfo/queryNameByProNum.json", method = RequestMethod.POST)
	@ResponseBody
	public ProInfo queryNameByProNum(@RequestParam(value = "proNum", required = false)String proNum, HttpServletRequest request) {
		ConditionQuery query = new ConditionQuery();
		// 项目阶段
		query.add(Restrictions.eq("proNum", proNum));
		query.add(Restrictions.eq("state", 1));
		ProInfo pro=null;
		List<ProInfo>list=proInfoService.queryListByCondition(query);
		for (ProInfo proInfo : list) {
			pro=proInfo;
		}
		return pro;
	}
	
	
}