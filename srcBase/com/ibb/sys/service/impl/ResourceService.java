package com.ibb.sys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.service.impl.BaseService;
import com.ibb.common.util.StringUtils;
import com.ibb.service.datadic.TreeNode;
import com.ibb.sys.dao.IResourceDao;
import com.ibb.sys.dao.IRoleResourceDao;
import com.ibb.sys.dao.impl.ResourceDao;
import com.ibb.sys.model.ResourceBean;
import com.ibb.sys.service.IResourceService;

import exception.IbbMgrException;

/**
 * 资源管理services
 * 
 * @author kin wong
 */
@Component
public class ResourceService extends BaseService<ResourceBean, Integer> implements IResourceService {
	private IResourceDao resourceDao;
	@Autowired
	private IRoleResourceDao roleResourceDao;
	
	
	@Autowired
	@Qualifier("resourceDao")
	@Override
	public void setBaseDao(IBaseDao<ResourceBean, Integer> baseDao) {
		this.baseDao = baseDao;
        this.resourceDao = (ResourceDao) baseDao;
	}

	/**
	 * 方法说明：获取当前登录用户的权限菜单
	 * 参数说明：@param uid
	 * 参数说明：@return
	 * @author Ou
	 * 2013-8-22
	 */
	public List<TreeNode> getUserMenu(String uid,String pmid) {
		String ridSql="SELECT t.resourceid FROM tbl_sys_role_resource t where t.roleid in (SELECT ur.roleid FROM tbl_sys_user_role ur where ur.userid=:userid)";
		Map<String, Object> ridSqlMap=new HashMap<String, Object>();
		ridSqlMap.put("userid", uid);
		List<Integer> userMidList=this.resourceDao.executeSQL(ridSql, ridSqlMap);//获取uid拥有的菜单ID
		
		if(userMidList == null || userMidList.size()==0)
			throw new IbbMgrException("当前用户无权限资源。");
		
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuffer hql=new StringBuffer("from ResourceBean t where 1=1 and t.resourceId in (:userMidList) and resourceType not in ('3') ");
		params.put("userMidList", userMidList);
		if (StringUtils.isNotNull(pmid)) {
			hql.append("and t.resourcePid= :resourcePid ");
			params.put("resourcePid", pmid);
		}
		hql.append("order by t.resourceIndex");
		
		List<ResourceBean> l = this.resourceDao.find(hql.toString(), params);
		List<TreeNode> tree = new ArrayList<TreeNode>();
		for (ResourceBean t : l) {
			tree.add(userTree(t, true,userMidList));
		}
		return tree;
	}
	
	private TreeNode userTree(ResourceBean t, boolean recursive,List<Integer> userMidList) {
		TreeNode node = new TreeNode();
		node.setId(Integer.toString(t.getResourceId()));
		node.setText(t.getResourceName());
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("url", t.getResourceUrl());
		node.setAttributes(attributes);
		/*
		if (t.getIconCls() != null) {
			node.setIconCls(t.getIconCls());
		} else {
			node.setIconCls("");
		}*/
		
		Map<String,Object> countParams = new HashMap<String,Object>();
		countParams.put("resourceId", t.getResourceId());
		countParams.put("userMidList", userMidList);
		ConditionQuery query=new ConditionQuery();
		query.add(Restrictions.eq("resourcePid", t.getResourceId()));
		query.add(Restrictions.in("resourceId", userMidList));
		resourceDao.queryCountByCondition(query);
		int count = resourceDao.queryCountByCondition(query);
		if (count > 0) {
			node.setState("open");
			if (recursive) {// 递归查询子节点
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("resourceId", t.getResourceId());
				params.put("userMidList", userMidList);
				List<ResourceBean> l = resourceDao.find("from ResourceBean t where t.resourcePid = :resourceId and t.resourceId in (:userMidList)  and resourceType not in ('3')  order by t.resourceIndex", params);
				List<TreeNode> children = new ArrayList<TreeNode>();
				for (ResourceBean r : l) {
					TreeNode tn = userTree(r, true,userMidList);
					children.add(tn);
				}
				node.setChildren(children);
			}
		}
		return node;
	}

	
	@Override
	public List<ResourceBean> queryResourceByPid(int pid) {
		List<ResourceBean> returnResources = new ArrayList<ResourceBean>();
		
		//一次全取数据
		OrderBy order = new OrderBy();
		order.add(Order.asc("resourceIndex"));
		List<ResourceBean> resources = resourceDao.queryListByCondition(new ConditionQuery(), order);
		
		//将关系以映射形式保存
		Map<Integer,List<Integer>> pKeyMap = new HashMap<Integer, List<Integer>>();
		Map<Integer,ResourceBean> resourceMap = new HashMap<Integer,ResourceBean>();	//主键记录映射
		for(ResourceBean resource : resources){
			if(pKeyMap.containsKey(resource.getResourcePid()))
				pKeyMap.get(resource.getResourcePid()).add(resource.getResourceId());
			else{
				List<Integer> idArr = new ArrayList<Integer>();
				idArr.add(resource.getResourceId());
				pKeyMap.put(resource.getResourcePid(), idArr);
			}
				
			resourceMap.put(resource.getResourceId(), resource);
		}
		
		//关系建立
		for(ResourceBean resource : resources){
			if(resourceMap.containsKey(resource.getResourcePid())){
				resourceMap.get(resource.getResourcePid()).addChild(resource);
			}
		}
		
		//选择相应的子树
		if(resourceMap.containsKey(pid))
			returnResources.add(resourceMap.get(pid));
		else if(pKeyMap.containsKey(pid)){
			List<Integer> idArr = pKeyMap.get(pid);
			for(int id : idArr)
				returnResources.add(resourceMap.get(id));
		}
		
		return returnResources;
	}
	
	
	public Object[] queryResourceByUserId(int userId){
		Map<String,Boolean> urlMap = new HashMap<String,Boolean>();//地址映射
		Map<String,List<String>> bntMap = new HashMap<String,List<String>>();//地址有按钮映射
		Map<String,List<String>> noBntMap = new HashMap<String,List<String>>();//地址无按钮映射
		Map<String,List<String>> navMap = new HashMap<String,List<String>>();//地址导航映射
		List<ResourceBean> returnResources = new ArrayList<ResourceBean>();//菜单资源
		
		//一次全取数据
		OrderBy order = new OrderBy();
		order.add(Order.asc("resourceIndex"));
		List<ResourceBean> resources = resourceDao.queryListByCondition(new ConditionQuery(), order);
		
		//将关系以映射形式保存
		Map<Integer,Integer> keyMap = new HashMap<Integer, Integer>();//<子,父>
		Map<Integer,List<Integer>> pKeyMap = new HashMap<Integer, List<Integer>>();//<父,子>
		Map<Integer,ResourceBean> resourceMap = new HashMap<Integer,ResourceBean>();	//主键记录映射
		for(ResourceBean resource : resources){
			if(pKeyMap.containsKey(resource.getResourcePid()))
				pKeyMap.get(resource.getResourcePid()).add(resource.getResourceId());
			else{
				List<Integer> idArr = new ArrayList<Integer>();
				idArr.add(resource.getResourceId());
				pKeyMap.put(resource.getResourcePid(), idArr);
			}
			keyMap.put(resource.getResourceId(), resource.getResourcePid());
			resourceMap.put(resource.getResourceId(), resource);
		}
		
		//找出保留的项目
		Map<Integer,String> optMap= new HashMap<Integer,String>();//用户拥有的操作菜单地址映射
		Set<Integer> userResourceIdSet = new HashSet<Integer>();
		List<ResourceBean> userResources = resourceDao.queryListByUserId(userId);
		for(ResourceBean resource : userResources){
			List<String> navList = new ArrayList<String>();
			getUserResourceIdSet(keyMap, resourceMap, resource.getResourceId(), userResourceIdSet, navList);
			Collections.reverse(navList);
			navMap.put(resource.getResourceUrl(), navList);
			
			if("2".equals(resource.getResourceType()) && StringUtils.isNotEmpty(resource.getResourceUrl())){
				optMap.put(resource.getResourceId(), resource.getResourceUrl());
			}
		}
		
		//将保留项目
		List<ResourceBean> userNewResources = new ArrayList<ResourceBean>();
		for(ResourceBean resource : resources){
			if(userResourceIdSet.contains(resource.getResourceId())){
				userNewResources.add(resource);
				if(StringUtils.isNotEmpty(resource.getResourceUrl()))
					urlMap.put(resource.getResourceUrl(), true);
			}else{
				if(StringUtils.isNotEmpty(resource.getResourceUrl()))
					urlMap.put(resource.getResourceUrl(), false);
			}
		}
		
		//关系建立并找出根结点
		for(ResourceBean resource : userNewResources){
			if(resourceMap.containsKey(resource.getResourcePid())){
				resourceMap.get(resource.getResourcePid()).addChild(resource);
			}
			
			if(resource.getResourcePid() == 0)
				returnResources.add(resource);
		}
		
		//计算用户拥有哪些操作按钮
		for(Integer resourcePid : optMap.keySet()){//用户拥有的菜单
			if(pKeyMap.containsKey(resourcePid)){
				for(Integer resourceId : pKeyMap.get(resourcePid)){//菜单拥有的按钮
					if(StringUtils.isNotEmpty(resourceMap.get(resourceId).getResourceUrl()) && StringUtils.isNotEmpty(resourceMap.get(resourceId).getResourceInd())){
						if(userResourceIdSet.contains(resourceId)){
							if(bntMap.containsKey(optMap.get(resourcePid)))
								bntMap.get(optMap.get(resourcePid)).add(resourceMap.get(resourceId).getResourceInd());
							else{
								List<String> indArr = new ArrayList<String>();
								indArr.add(resourceMap.get(resourceId).getResourceInd());
								bntMap.put(optMap.get(resourcePid), indArr);
							}
						}else{
							if(noBntMap.containsKey(optMap.get(resourcePid)))
								noBntMap.get(optMap.get(resourcePid)).add(resourceMap.get(resourceId).getResourceInd());
							else{
								List<String> indArr = new ArrayList<String>();
								indArr.add(resourceMap.get(resourceId).getResourceInd());
								noBntMap.put(optMap.get(resourcePid), indArr);
							}
						}
					}
				}
			}
		}
		
		//返回数据整理
		Object[] obj = new Object[5];
		obj[0] = returnResources;
		obj[1] = urlMap;
		obj[2] = navMap;
		obj[3] = bntMap;
		obj[4] = noBntMap;
		
		return obj;
	}
	
	/**
	 * 递归查找父结点
	 * @param keyMap <子编号，父编号>
	 * @param resourceMap <编号,对象>
	 * @param key 根结点
	 * @param returnSet 返回的子父结点列表
	 * @param navList 返回的导航列表
	 */
	protected void getUserResourceIdSet(Map<Integer, Integer> keyMap,
			Map<Integer, ResourceBean> resourceMap, int key,
			Set<Integer> returnSet, List<String> navList) {
		returnSet.add(key);//先把自己加进去
		
		if(resourceMap.containsKey(key))
			navList.add(resourceMap.get(key).getResourceName());//加导航
		
		if(keyMap.containsKey(key)){
			returnSet.add(keyMap.get(key));
			getUserResourceIdSet(keyMap, resourceMap, keyMap.get(key), returnSet, navList);
		}
	}
	
	@Override
	public void deleteByIdCascade(int id) {
		this.delete(id);//先删除当前结点
		
		//再判断是否存在子结点
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("resourcePid", id));
		List<ResourceBean> resourceList = queryListByCondition(query, new OrderBy());
		for(ResourceBean resource : resourceList){
			deleteByIdCascade(resource.getResourceId());
		}
	}
	
	@Override
	public void delete(Integer id) {
		ResourceBean resource = get(id);
		deleteObject(resource);
	}

	@Override
	public void deleteObject(ResourceBean resource) {
		super.deleteObject(resource);
		
		OrderBy order = new OrderBy();
		ConditionQuery query = new ConditionQuery();
		order.add(Order.asc("resourceIndex"));
		query.add(Restrictions.eq("resourcePid", resource.getResourcePid()));
		query.add(Restrictions.gt("resourceIndex", resource.getResourceIndex()));
		List<ResourceBean> rsList = resourceDao.queryListByCondition(query,order);
		updateResourceOrder(rsList);
	}
	
	protected void updateResourceOrder(List<ResourceBean> rsList) {
		for(int i = 0; i < rsList.size(); i++){
			rsList.get(i).setResourceIndex(rsList.get(i).getResourceIndex() - 1);
			super.update(rsList.get(i));
		}
	}

	@Override
	public void updateOrder(int resourceId, String orderType) {
		ResourceBean resource = resourceDao.get(resourceId);
		int itemOrder = resource.getResourceIndex();
		
		OrderBy order = new OrderBy();
		ConditionQuery query = new ConditionQuery();
		order.add(Order.asc("resourceIndex"));
		query.add(Restrictions.eq("resourcePid", resource.getResourcePid()));
		List<ResourceBean> resourceList = resourceDao.queryListByCondition(query,order);
		
		if("1".equals(orderType) && itemOrder > 1){//TOP
			for(int i = 0; i < itemOrder - 1; i++){
				resourceList.get(i).setResourceIndex(i + 2);
				super.update(resourceList.get(i));
			}
			
			resource.setResourceIndex(1);
			super.update(resource);
		}else if("2".equals(orderType) && itemOrder > 1){//UP
			resource.setResourceIndex(itemOrder - 1);
			resourceList.get(itemOrder - 2).setResourceIndex(itemOrder);
			super.update(resource);
			super.update(resourceList.get(itemOrder - 2));
		}else if("3".equals(orderType) && itemOrder < resourceList.size()){//DOWN
			resource.setResourceIndex(itemOrder + 1);
			resourceList.get(itemOrder).setResourceIndex(itemOrder);
			super.update(resource);
			super.update(resourceList.get(itemOrder));
		}else if("4".equals(orderType) && itemOrder < resourceList.size()){//LOW
			for(int i = itemOrder; i < resourceList.size(); i++){
				resourceList.get(i).setResourceIndex(i);
				super.update(resourceList.get(i));
			}
			resource.setResourceIndex(resourceList.size());
			super.update(resource);
		}
	}
}
