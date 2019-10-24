package com.ibb.sys.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.common.web.model.EasyUITreeJsonModel;
import com.ibb.sys.model.ResourceBean;
import com.ibb.sys.model.RoleBean;
import com.ibb.sys.model.RoleResourceBean;
import com.ibb.sys.model.RoleResourcePk;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.model.UserRoleBean;
import com.ibb.sys.model.UserRolePk;
import com.ibb.sys.service.IResourceService;
import com.ibb.sys.service.IRoleResourceService;
import com.ibb.sys.service.IRoleService;
import com.ibb.sys.service.IUserRoleService;
import com.ibb.sys.service.IUserService;


/**
 * 角色管理控制
 * 
 * @author kin wong
 */
@Controller
public class RoleController {
	@Autowired
    private IRoleService roleService;
	
	@Autowired
    private IRoleResourceService roleResourceService;
	
	@Autowired
    private IResourceService resourceService;
	
	@Autowired
	private IUserRoleService userRoleService;
	
	@Autowired
	private IUserService userService;
	
	/**
	 * 初始化页面(异步取数时需要)
	 * @return 查询列表初始化页面
	 */
	@RequestMapping(value = "/role")
	public String initPage(){
		return "sys/role";
	}
	
	/**
	 * 查询数据列表
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/role/list.json",method=RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<RoleBean> queryList(RoleBean role,HttpServletRequest request){
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1);//第几页
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		//角色编码模糊查询
		if(StringUtils.isNotEmpty(role.getRoleCode())){
			query.add(Restrictions.like("roleCode", "%"+role.getRoleCode().trim()+"%"));
		}
		
		//角色名称模糊查询
		if(StringUtils.isNotEmpty(role.getRoleName())){
			query.add(Restrictions.like("roleName", "%" + role.getRoleName().trim() + "%"));
		}
		
		Page<RoleBean> page = roleService.queryListByCondition(query,pn, pageSize);
		
		return new EasyUIGridJsonModel(page);
	}
	
	/**
	 * 增加一条记录
	 * @param role 角色对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/role/add.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel add(RoleBean role){
		if(role != null){
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.or(Restrictions.eq("roleName", role.getRoleName().trim()), Restrictions.eq("roleCode", role.getRoleCode().trim())));
			List<RoleBean> roleList = roleService.queryListByCondition(query);
			for(RoleBean temp : roleList){
				if(role.getRoleCode().trim().equals(temp.getRoleCode()))
					return	new CommonJsonModel(false,"已存在该角色编号！");
				else if(role.getRoleName().trim().equals(temp.getRoleName()))
					return	new CommonJsonModel(false,"已存在该角色名称！");
			}
			
			roleService.save(role);
		}else{
			return new CommonJsonModel(false,"角色对象为空！");
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 更新一条记录
	 * @param role 角色对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/role/update.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(RoleBean role){
		if(role != null){
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("roleName", role.getRoleName().trim()));
			List<RoleBean> roleList = roleService.queryListByCondition(query);
			if(roleList != null && roleList.size() > 0 ){
				for(RoleBean ro : roleList){
					if(role.getRoleId() == ro.getRoleId())
						continue;
					else if(role.getRoleName().trim().equals(ro.getRoleName().trim()))
						return	new CommonJsonModel(false,"已存在该角色名称！");
				}
			}

			roleService.update(role);
		}else{
			return new CommonJsonModel(false,"角色对象为空！");
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 删除多条(一条)记录
	 * @param roleIdArr 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/role/delete.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String roleIdArr,HttpServletRequest request){
		String[] roleIds = roleIdArr.split("#");
		for(String roleId : roleIds){
			if(StringUtils.isNotEmpty(roleId)){
				roleService.deleteCasecade(Integer.parseInt(roleId));
			}
		}
		return new CommonJsonModel();
	}
	
	@RequestMapping(value = "/role/{roleId}/listResource.json",method=RequestMethod.POST)
	@ResponseBody
	public List<EasyUITreeJsonModel> listRoleResource(@PathVariable int roleId,HttpServletRequest request){
		//查找所有的资源与当前角色拥有的资源
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("pk.roleId", roleId));
		List<RoleResourceBean> roleResourceList = roleResourceService.queryListByCondition(query);
		List<ResourceBean> resourceList = resourceService.queryResourceByPid(0);
		
		//该角色已存在的权限列表
		List<Integer> resourceIdList = new ArrayList<Integer>();
		if(roleResourceList != null){
			for(RoleResourceBean roleResource : roleResourceList)
				resourceIdList.add(roleResource.getPk().getResourceId());
		}
		
		if(resourceList.size() > 0){
			return generEasyUITree(resourceList, resourceIdList);
		}else{
			return null;
		}
	}
	
	@RequestMapping(value = "/role/updateResource.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel updateRoleResource(HttpServletRequest request){
		int roleId = Integer.parseInt(request.getParameter("roleId").trim());
		
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("pk.roleId", roleId));
		List<RoleResourceBean> roleResourceList = roleResourceService.queryListByCondition(query);
		if(roleResourceList != null && roleResourceList.size() > 0){
			for(RoleResourceBean roleResource : roleResourceList)
				roleResourceService.deleteObject(roleResource);
		}
		
		String resourceIdStr = request.getParameter("checkIdArr").trim();
		if(StringUtils.isNotEmpty(resourceIdStr)){
			String[] resourceIdArr = resourceIdStr.split("#");
			if(resourceIdArr != null && resourceIdArr.length > 0){
				Set<String> resourceIdSet = new HashSet<String>(Arrays.asList(resourceIdArr));//转成集合
				for(String resourceId : resourceIdSet){
					RoleResourceBean roleResource = new RoleResourceBean();
					RoleResourcePk pk = new RoleResourcePk();
					pk.setResourceId(Integer.parseInt(resourceId));
					pk.setRoleId(roleId);
					roleResource.setPk(pk);
					
					roleResourceService.save(roleResource);
				}
			}
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 生成easyui格式的树
	 * 
	 * @param resourceList 树列表
	 * @param resourceIdList 待选择的树节点列表
	 * 
	 * @param returnModel easyui树数据模型
	 */
	protected List<EasyUITreeJsonModel> generEasyUITree(List<ResourceBean> resourceList, List<Integer> resourceIdList) {
		List<EasyUITreeJsonModel> returnModelList = new ArrayList<EasyUITreeJsonModel>();
		
		for(ResourceBean resource : resourceList){
			EasyUITreeJsonModel returnModel = new EasyUITreeJsonModel();
			returnModel.setId(String.valueOf(resource.getResourceId()));
			returnModel.setText(resource.getResourceName());
			if("3".equals(resource.getResourceType()))
				returnModel.setChecked(resourceIdList.contains(resource.getResourceId()));
			else
				returnModel.setChecked(false);
			
			//属性类型
			Map<String,String> map = new HashMap<String,String>();
			map.put("resourceType", resource.getResourceType());
			returnModel.setAttributes(map);
			
			//递归检查是否还有子结点
			if(resource.hasChild()){
				returnModel.setChildren(generEasyUITree(resource.getChildren(), resourceIdList));
			}
			
			//是否构造虚拟结点
			if("2".equals(resource.getResourceType())){
				//初始化结点
				EasyUITreeJsonModel enterNode = new EasyUITreeJsonModel();
				enterNode.setId(String.valueOf(resource.getResourceId()) + "_");
				enterNode.setText("(访问)");
				enterNode.setChecked(resourceIdList.contains(resource.getResourceId()));
				Map<String,String> nodeMap = new HashMap<String,String>();
				nodeMap.put("resourceType", "4");
				enterNode.setAttributes(nodeMap);
				
				if(resource.hasChild()){
					returnModel.getChildren().add(0,enterNode);
				}else{
					List<EasyUITreeJsonModel> enterNodeList = new ArrayList<EasyUITreeJsonModel>();
					enterNodeList.add(enterNode);
					returnModel.setChildren(enterNodeList);
				}
			}
			
			returnModelList.add(returnModel);
		}
		
		return returnModelList;
	}
	
	
	
	/**
	 * 查询下拉列表数据(可选用户与已选用户)  
	 * 
	 * @return 操作结果json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/role/{roleId}/selectuser.json", method = RequestMethod.GET)
	@ResponseBody
	public CommonJsonModel selectuser(@PathVariable int roleId) {
		// 所有角色和用户已有角色
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("pk.roleId", roleId));
		List<UserRoleBean> userRoleList = userRoleService.queryListByCondition(query);
//		List<RoleBean> roleList = roleService.queryListAll();
		List<UserBean> userList = userService.queryListAll();

		List<Integer> userRoles = new ArrayList<Integer>();
		if (userRoleList != null) {
			for (UserRoleBean userRole : userRoleList) {
				userRoles.add(userRole.getPk().getUserId());
			}
		}

		List<UserBean> selectList = new ArrayList<UserBean>();
		List<UserBean> unselectList = new ArrayList<UserBean>();
		if (userList != null) {
			for (UserBean user : userList) {
				if (userRoles.contains(user.getUserId())) {
					selectList.add(user);
					continue;
				}
				unselectList.add(user);
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
	@RequestMapping(value = "/role/grant.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel grant(HttpServletRequest request) {
		int roleId = Integer.parseInt(request.getParameter("roleId"));

		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("pk.roleId", roleId));
		List<UserRoleBean> userRoleList = userRoleService.queryListByCondition(query);
		if (userRoleList != null && userRoleList.size() > 0) {
			for (UserRoleBean userRole : userRoleList)
				userRoleService.deleteObject(userRole);
		}

		String selectStr = request.getParameter("select").trim();
		if (StringUtils.isNotEmpty(selectStr)) {
			String[] selectArr = selectStr.split("#");
			if (selectArr != null && selectArr.length > 0) {
				for (String userId : selectArr) {
					UserRoleBean userRole = new UserRoleBean();
					UserRolePk pk = new UserRolePk();
					pk.setRoleId(roleId);
					pk.setUserId(Integer.parseInt(userId));
					userRole.setPk(pk);

					userRoleService.save(userRole);
				}
			}
		}

		return new CommonJsonModel();
	}
	
	/**
	 * 用户授权界面查询用户
	 * 
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/role/grantSearch.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel grantSearch(HttpServletRequest request) {
		String condition = request.getParameter("condition");

		List<UserBean> list = userService.getUserListByCondition(condition);
		List<UserBean> unselectList = new ArrayList<UserBean>();
		if(list!=null && list.size()>0){
			String selectStr = request.getParameter("select").trim();
			if (StringUtils.isNotEmpty(selectStr)) {
				String[] selectArr = selectStr.split("#");
				if (selectArr != null && selectArr.length > 0) {
					for(UserBean userBean : list){
						unselectList.add(userBean);
						for(String userid : selectArr){
							Integer id = userBean.getUserId();
							if(userid.equals(id.toString())){
								unselectList.remove(userBean);
								break;
							}
						}
					}
				}
			}
		}
		
		List[] data = new ArrayList[1];
		data[0] = unselectList;

		CommonJsonModel cjm = new CommonJsonModel();
		cjm.setData(data);
		return cjm;
	}
	
}
