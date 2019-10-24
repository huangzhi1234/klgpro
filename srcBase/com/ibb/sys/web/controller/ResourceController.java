package com.ibb.sys.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.sys.model.ResourceBean;
import com.ibb.sys.service.IResourceService;


/**
 * 资源管理控制
 * 
 * @author kin wong
 */
@Controller
public class ResourceController {
	private final String TOP = "1";
	private final String UP = "2";
	private final String DOWN = "3";
	private final String LOW = "4";	
	
	@Autowired
    private IResourceService resourceService;
	
	/**
	 * 初始化页面(异步取数时需要)
	 * @return 查询列表初始化页面
	 */
	@RequestMapping(value = "/resource")
	public String initPage(){
		return "sys/resource";
	}
	
	/**
	 * 查询下拉列表数据
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/resource/{resourcePid}/select.json",method=RequestMethod.GET)
	@ResponseBody
	public CommonJsonModel querySelect(@PathVariable int resourcePid){
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("resourcePid", resourcePid));
		List<ResourceBean> resourceList = resourceService.queryListByCondition(query);
		
		CommonJsonModel cjm = new CommonJsonModel();
		cjm.setData(resourceList);
		return cjm;
	}
	
	/**
	 * 查询数据列表
	 * @return 列表json对象
	 */
	@RequestMapping(value = "/resource/list.json",method=RequestMethod.POST)
	@ResponseBody
	public List<ResourceBean> queryList(HttpServletRequest request){
		int resourcePid = ServletRequestUtils.getIntParameter(request, "resourcePid", 0);
		List<ResourceBean> resourceList = resourceService.queryResourceByPid(resourcePid);
		
		return resourceList;
	}
	
	
	/**
	 * 增加一条记录
	 * @param resource 资源对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/resource/add.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel add(ResourceBean resource){
		if(resource != null){
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("resourcePid", resource.getResourcePid()));
			List<ResourceBean> resourceList = resourceService.queryListByCondition(query);
			if(resourceList != null && resourceList.size() > 0 ){
				for(ResourceBean rs : resourceList){
					if(resource.getResourceName().equals(rs.getResourceName()))
						return	new CommonJsonModel(false,"同父级结点中已存在该资源名称！");
					else if(StringUtils.isNotEmpty(resource.getResourceInd()) && resource.getResourceInd().equals(rs.getResourceInd()))
						return	new CommonJsonModel(false,"同父级结点中已存在该资源标识！");
				}
				
				resource.setResourceIndex(resourceList.size() + 1);
			}else{
				resource.setResourceIndex(1);
			}
			
			resourceService.save(resource);
		}else{
			return new CommonJsonModel(false,"资源对象为空！");
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 更新一条记录
	 * @param resource 资源对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/resource/update.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(ResourceBean resource){
		if(resource != null){
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("resourcePid", resource.getResourcePid()));
			List<ResourceBean> resourceList = resourceService.queryListByCondition(query);
			if(resourceList != null && resourceList.size() > 0 ){
				for(ResourceBean rs : resourceList){
					if(resource.getResourceId() == rs.getResourceId())
						continue;
					else if(resource.getResourceName().equals(rs.getResourceName()))
						return	new CommonJsonModel(false,"同父级结点中已存在该资源名称！");
					else if(StringUtils.isNotEmpty(resource.getResourceInd()) && resource.getResourceInd().equals(rs.getResourceInd()))
						return	new CommonJsonModel(false,"同父级结点中已存在该资源标识！");
				}
			}
			
			resourceService.update(resource);
		}else{
			return new CommonJsonModel(false,"资源对象为空！");
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 删除多条(一条)记录
	 * @param resourceIdArr 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/resource/delete.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String resourceIdArr,HttpServletRequest request){
		String[] resourceIds = resourceIdArr.split("#");
		for(String resourceId : resourceIds){
			if(StringUtils.isNotEmpty(resourceId)){
				resourceService.deleteByIdCascade(Integer.parseInt(resourceId));
			}
		}
		return new CommonJsonModel();
	}
	
	/**
	 * 置顶记录
	 * @param resourceId 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/resource/top.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel top(String resourceId,HttpServletRequest request){
		resourceService.updateOrder(Integer.parseInt(resourceId), TOP);
		return new CommonJsonModel();
	}
	
	/**
	 * 向上记录
	 * @param resourceId 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/resource/up.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel up(String resourceId,HttpServletRequest request){
		resourceService.updateOrder(Integer.parseInt(resourceId), UP);
		return new CommonJsonModel();
	}
	
	/**
	 * 置顶记录
	 * @param resourceId 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/resource/down.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel down(String resourceId,HttpServletRequest request){
		resourceService.updateOrder(Integer.parseInt(resourceId), DOWN);
		return new CommonJsonModel();
	}
	
	/**
	 * 置顶记录
	 * @param resourceId 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/resource/low.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel low(String resourceId,HttpServletRequest request){
		resourceService.updateOrder(Integer.parseInt(resourceId), LOW);
		return new CommonJsonModel();
	}	
}
