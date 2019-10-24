package com.ibb.web.controller.projectMessage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.model.depart.DepartDicInfo;
import com.ibb.model.projectMessage.UserProBean;
import com.ibb.service.depart.IDepartDicService;
import com.ibb.service.projectMessage.IUserProService;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.service.IUserService;

@Controller
public class UserProController implements ServletContextAware {

	@Autowired
	private IUserProService userProService;
	@Autowired
	private IDepartDicService departDicService;
	@Autowired
	private IUserService userService;

	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	/**
	 * 获取部门集合和已授权的用户集合
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userPro/{proId}/deptselect.json", method = RequestMethod.GET)
	@ResponseBody
	public CommonJsonModel getDeptList(@PathVariable String proId) {
		
		// 获取已授权查看该项目的User集合
		List<UserBean> selecUsertList = userService.getUserListWithProId(proId);
		
		List<DepartDicInfo> deptselectList = departDicService.getAllDeptList();

		List[] data = new ArrayList[2];
		data[0] = deptselectList;
		data[1] =selecUsertList;

		CommonJsonModel cjm = new CommonJsonModel();
		cjm.setData(data);
		return cjm;
	}

	/**
	 * 获取用户对项目查看权限的项目集合
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userPro/select.json", method = RequestMethod.POST)
	@ResponseBody
	public List[] listByUserId(String proId, int departId, String select) {

		// 存放未授权的用户
		List<UserBean> unselectList = new ArrayList<UserBean>();
		// 存放已授权的用户
//		List<UserBean> selectList = new ArrayList<UserBean>();

		// 获取已授权查看该项目的UserPro集合
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("proId", Integer.parseInt(proId)));
		List<UserProBean> list = userProService.queryListByCondition(query);

		// 获取页面上显示已授权的人员的id集合
		List<Integer> userProPageList = new ArrayList<Integer>();
		if (!select.equals("select=")) {
			select = select.substring(7);
			if (StringUtils.isNotEmpty(select)) {
				String[] selectArr = select.split("#");
				if (selectArr != null && selectArr.length > 0) {
					for (String userId : selectArr) {
						userProPageList.add(Integer.parseInt(userId));
					}
				}
			}
		}

		// 获取该部门的人员
		ConditionQuery query2 = new ConditionQuery();
		query2.add(Restrictions.eq("departId", departId));
		List<UserBean> userDeptList = userService.queryListByCondition(query2);
		
		if(userDeptList!=null && userDeptList.size()>0){
			for(UserBean u : userDeptList){
				if(!userProPageList.contains(u.getUserId())){
					unselectList.add(u);
				}
			}
		}
		
		List[] data = new ArrayList[1];
		data[0] = unselectList;
		return data;
	}

	/**
	 * 给用户授权
	 * 
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/userPro/grant.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel grant(HttpServletRequest request) {
		int proId = Integer.parseInt(request.getParameter("proId"));

		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("proId", proId));
		List<UserProBean> userProList = userProService.queryListByCondition(query);
		// 删掉该项目已授权的所有用户
		if (userProList != null && userProList.size() > 0) {
			for (UserProBean userRole : userProList)
				userProService.deleteObject(userRole);
		}
		// 重新对用户进行授权
		String selectStr = request.getParameter("select").trim();
		if (StringUtils.isNotEmpty(selectStr)) {
			String[] selectArr = selectStr.split("#");
			if (selectArr != null && selectArr.length > 0) {
				for (String userId : selectArr) {
					UserProBean userPro = new UserProBean();
					userPro.setUserId(Integer.parseInt(userId));
					userPro.setProId(proId);
					userPro.setState(1);
					userProService.save(userPro);
				}
			}
		}

		return new CommonJsonModel();
	}

}
