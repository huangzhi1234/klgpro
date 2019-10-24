package com.ibb.sys.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.SecureUtil;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.common.web.support.editor.DateEditor;
import com.ibb.model.datadic.TblDicInfo;
import com.ibb.model.depart.DepartDicInfo;
import com.ibb.model.score.ProScore;
import com.ibb.service.datadic.impl.TblDicInfoService;
import com.ibb.service.depart.IDepartDicService;
import com.ibb.service.score.IProScoreService;
import com.ibb.sys.model.RoleBean;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.model.UserRoleBean;
import com.ibb.sys.model.UserRolePk;
import com.ibb.sys.service.IRoleService;
import com.ibb.sys.service.IUserRoleService;
import com.ibb.sys.service.IUserService;

/**
 * 用户管理控制
 * 
 * @author kin wong
 */
@Controller
public class UserController {
	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IProScoreService proScoreService;

	@Autowired
	private IUserRoleService userRoleService;
	
	@Autowired
	private TblDicInfoService tblDicInfoService;

	@Autowired
	private IDepartDicService departDicService;
	
	/**
	 * 初始化页面(异步取数时需要)
	 * 
	 * @return 查询列表初始化页面
	 */
	@RequestMapping(value = "/user")
	public String initPage() {
		return "sys/user";
	}

	/**
	 * 查询数据列表
	 * 
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<UserBean> queryList(UserBean user, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1);// 第几页
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		String actFlag = request.getParameter("actFlag");
		String dicNum=request.getParameter("dicNum");
		//通过字典编号，查询id
		int id=0;
		ConditionQuery query1=new ConditionQuery();
		query1.add(Restrictions.eq("dicNum", dicNum));
		List<DepartDicInfo> list=departDicService.queryListByCondition(query1);
		if(list!=null&&list.size()!=0){
			id=list.get(0).getDepartId();
		}
		ConditionQuery query = new ConditionQuery();
		if (id!=0) {
			query.add(Restrictions.like("departId", id));
		}
		if (StringUtils.isNotEmpty(user.getUserAct())) {
			query.add(Restrictions.like("userAct", "%" + user.getUserAct().trim() + "%"));
		}
		if (StringUtils.isNotEmpty(user.getUserName())) {
			query.add(Restrictions.like("userName", "%" + user.getUserName().trim() + "%"));
		}
		if ("1".equals(actFlag)) {
			query.add(Restrictions.gt("actTime", new Date()));
		}
		if ("0".equals(actFlag)) {
			query.add(Restrictions.lt("actTime", new Date()));
		}

		Page<UserBean> page = userService.queryListByCondition(query, pn, pageSize);

		return new EasyUIGridJsonModel(page);
	}

	/**
	 * 查询公司名称
	 * 
	 * @return 列表json对象
	 */
	/*@RequestMapping(value = "/user/queryCompany.json", method = RequestMethod.POST)
	@ResponseBody
	public CompanyInfo queryCompany(@RequestParam(value = "companyNum", required = false) String companyNum) {

		ConditionQuery query = new ConditionQuery();
		if (StringUtils.isNotEmpty(companyNum)) {
			query.add(Restrictions.eq("companyId", Integer.valueOf(companyNum)));
		}

		CompanyInfo companyInfo = companyInfoService.queryListByCondition(query).get(0);
		return companyInfo;
	}*/

	/**
	 * 增加一条记录
	 * 
	 * @param user
	 *            用户对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/user/add.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel add(UserBean user,HttpServletRequest request) {
		if (user != null) {
			String dicNum=request.getParameter("dicNum");
			int id=0;
			ConditionQuery query1=new ConditionQuery();
			query1.add(Restrictions.eq("dicNum", dicNum));
			List<DepartDicInfo> list=departDicService.queryListByCondition(query1);
			if(list!=null&&list.size()!=0){
				id=list.get(0).getDepartId();
			}
			user.setDepartId(id);
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("userAct", user.getUserAct().trim()));
			List<UserBean> userList = userService.queryListByCondition(query);
			if (userList != null && userList.size() > 0) {
				return new CommonJsonModel(false, "已存在该用户！");
			} else {
				user.setUserPwd(SecureUtil.encode("SHA", user.getUserPwd()));
				userService.save(user);
				/*-------新增用户的同时，创建一条积分表数据--------*/
				ProScore proScore =new ProScore();
				proScore.setUserId(user.getUserId());
				proScore.setScore(0);
				proScoreService.save(proScore);
				
				
			}
		} else {
			return new CommonJsonModel(false, "用户对象为空！");
		}

		return new CommonJsonModel();
	}

	/**
	 * 更新一条记录
	 * 
	 * @param user
	 *            用户对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/user/update.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(UserBean user,HttpServletRequest request) {
		if (user != null) {
			
			
			if (StringUtils.isNotEmpty(user.getUserPwd()))
				user.setUserPwd(SecureUtil.encode("SHA", user.getUserPwd()));
			userService.update(user);
		} else {
			return new CommonJsonModel(false, "用户对象为空！");
		}

		return new CommonJsonModel();
	}

	/**
	 * 删除多条(一条)记录
	 * 
	 * @param userIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/user/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String userIdArr, HttpServletRequest request) {
		String[] userIds = userIdArr.split("#");
		UserBean currentUser = (UserBean) request.getSession().getAttribute("user");
		if (Arrays.binarySearch(userIds, String.valueOf(currentUser.getUserId())) >= 0) {
			return new CommonJsonModel(false, userIds.length > 1 ? "选择的记录中不能包括当前登陆用户！" : "不能删除当前登陆用户");
		}
		for (String userId : userIds) {
			if (StringUtils.isNotEmpty(userId)) {
				/*-----删除积分表对应的数据-----*/
				UserBean user=userService.get(Integer.parseInt(userId));
				Integer id=user.getUserId();
				ConditionQuery query1=new ConditionQuery();
				query1.add(Restrictions.eq("userId", id));
				List<ProScore> list=proScoreService.queryListByCondition(query1);
				if(list!=null&&list.size()>0){
					Integer scoreId=list.get(0).getScoreId();
					proScoreService.delete(scoreId);
				}
				userService.deleteCasecade(Integer.parseInt(userId));
			}
		}
		return new CommonJsonModel();
	}

	/**
	 * 查询下拉列表数据(可选角色与已选角色)
	 * 
	 * @return 操作结果json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/{userId}/select.json", method = RequestMethod.GET)
	@ResponseBody
	public CommonJsonModel querySelect(@PathVariable int userId) {
		// 所有角色和用户已有角色
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("pk.userId", userId));
		List<UserRoleBean> userRoleList = userRoleService.queryListByCondition(query);
		List<RoleBean> roleList = roleService.queryListAll();

		List<Integer> userRoles = new ArrayList<Integer>();
		if (userRoleList != null) {
			for (UserRoleBean userRole : userRoleList) {
				userRoles.add(userRole.getPk().getRoleId());
			}
		}

		List<RoleBean> selectList = new ArrayList<RoleBean>();
		List<RoleBean> unselectList = new ArrayList<RoleBean>();
		if (roleList != null) {
			for (RoleBean role : roleList) {
				if (userRoles.contains(role.getRoleId())) {
					selectList.add(role);
					continue;
				}
				unselectList.add(role);
			}
		}

		List[] data = new ArrayList[2];
		data[0] = unselectList;
		data[1] = selectList;

		CommonJsonModel cjm = new CommonJsonModel();
		cjm.setData(data);
		return cjm;
	}

	/**
	 * 给用户授权
	 * 
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/user/grant.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel grant(HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("userId"));

		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("pk.userId", userId));
		List<UserRoleBean> userRoleList = userRoleService.queryListByCondition(query);
		if (userRoleList != null && userRoleList.size() > 0) {
			for (UserRoleBean userRole : userRoleList)
				userRoleService.deleteObject(userRole);
		}

		String selectStr = request.getParameter("select").trim();
		if (StringUtils.isNotEmpty(selectStr)) {
			String[] selectArr = selectStr.split("#");
			if (selectArr != null && selectArr.length > 0) {
				for (String roleId : selectArr) {
					UserRoleBean userRole = new UserRoleBean();
					UserRolePk pk = new UserRolePk();
					pk.setRoleId(Integer.parseInt(roleId));
					pk.setUserId(userId);
					userRole.setPk(pk);

					userRoleService.save(userRole);
				}
			}
		}

		return new CommonJsonModel();
	}

	/**
	 * 查询用户列表
	 * 
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/queryUser.json", method = RequestMethod.POST)
	@ResponseBody
	public List<UserBean> queryUserList() {
		List<UserBean> lst = userService.queryListAll();
		return lst;
	}

	/**
	 * 查询用户列表
	 * 
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/queryUserById.json", method = RequestMethod.POST)
	@ResponseBody
	public UserBean queryUserById(@RequestParam(value = "userId") String userId) {
		UserBean userBean = null;
		ConditionQuery query = new ConditionQuery();
		if(StringUtils.isNotEmpty(userId)){
			query.add(Restrictions.eq("userId", Integer.parseInt(userId)));
			List<UserBean> lst = userService.queryListByCondition(query);
			if (lst != null && lst.size() > 0)
				userBean = lst.get(0);
		}
		
		
		return userBean;
	}

	/**
	 * 日期转换器
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateEditor());
	}
	
	
	
	/**
	 * 查询数据列表
	 * 
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/listByName.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<UserBean> queryListByName(UserBean user, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1);// 第几页
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		int id=0;
		String dicNum=request.getParameter("dicNum");
		//通过字典编号，查询id
		ConditionQuery query1=new ConditionQuery();
		query1.add(Restrictions.eq("dicNum", dicNum));
		List<DepartDicInfo> list=departDicService.queryListByCondition(query1);
		if(list!=null&&list.size()!=0){
			id=list.get(0).getDepartId();
		}
		ConditionQuery query = new ConditionQuery();
		if (id!=0) {
			query.add(Restrictions.like("departId", id));
		}
		Page<UserBean> page = userService.queryListByCondition(query, pn, pageSize);

		return new EasyUIGridJsonModel(page);
	}
	
		
	@RequestMapping(value = "/user/getUserId.json", method = RequestMethod.POST)
	@ResponseBody
	public Integer getUserId(HttpServletRequest request){
		Integer userId=0;//上传总积分
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");//当前用户登录的对象
		userId=userBean.getUserId();
		return userId;
	}
	
	
	@RequestMapping(value = "/user/changePwd.json", method = RequestMethod.POST)
	@ResponseBody
	public int changePwd(HttpServletRequest request){
//		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String newpassword = request.getParameter("newpassword");
		String newpassword2 = request.getParameter("newpassword2");
		if(!newpassword.equals(newpassword2)){
			return 3;
		}
		UserBean userBean = (UserBean) request.getSession().getAttribute("user");//当前用户登录的对象
		if(SecureUtil.encode("SHA", password).equals(userBean.getUserPwd())){
			userBean.setUserPwd(SecureUtil.encode("SHA", newpassword));
			userService.saveOrUpdate(userBean);
			return 1;
		}
		return 2;
	}
	
	
}
