package com.ibb.service.patch.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.NumberUtils;
import com.ibb.common.util.StringBufferUtils;
import com.ibb.common.util.StringUtils;
import com.ibb.dao.patch.impl.ProDicDao;
import com.ibb.model.patch.ProDic;
import com.ibb.model.patch.TblProDic;
import com.ibb.service.datadic.PageUtils;
import com.ibb.service.datadic.TreeNode;
import com.ibb.service.patch.ITblProDicService;

import exception.IbbMgrException;

@Service("tblProDicService")
public class TblProDicService implements ITblProDicService {
	private static final Logger logger=Logger.getLogger(TblProDicService.class);
	
	@Autowired
	private ProDicDao proDicDao;

	@Autowired
	public void setproDicDao(ProDicDao proDicDao) {
		this.proDicDao = proDicDao;
	}
	///////////////////////////////////////////////////////////////
	/*
	 * # 由于数据字典一旦定型就很少修改，所以增删改时可以清除所有缓存@CacheEvict(value="DICT_CACHE",allEntries=true) 
	 * # Cacheable：value对应ehcache.xml中配置的缓存名称，key是缓存的唯一标示，命名规则：查询参数+方法名
	 * # 在同一个类中调用有缓存注解的方法，并不会获取到缓存，而是直接执行该方法
	 */
	
	/**
	 * 添加数据字典节点
	 * @param dic
	 * @return
	 */
	@CacheEvict(value="DICT_CACHE",allEntries=true) 
	public String addDict(TblProDic dictDto){
		ProDic tDict = new ProDic();
		BeanUtils.copyProperties(dictDto, tDict);
		/**
		 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
		 */
		String upClassNo = dictDto.getClassNo();  //这个是父节点所在的层次
		if("zz".equals(upClassNo.trim()))
			return "字典项添加失败，当前节点已处于最大节点层。";
		String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
		tDict.setClassNo(classNo);
		
		String findMaxSystemId = this.findMaxSystemId(dictDto.getUpSystemId());
		if(!StringUtils.isNull(findMaxSystemId)){
			if(!findMaxSystemId.equals("zz")){
				String sid = StringUtils.add36System(findMaxSystemId);
				tDict.setSystemId(sid);
			}else{
				return "字典项添加失败，该节点的子级节点编号已经到达最大值。";
			}
		}else{
			if("1".equals(classNo))
				tDict.setSystemId("01");
			else
				tDict.setSystemId(dictDto.getUpSystemId()+"01");
		}
		int n = tDict.getSystemId().length();
		tDict.setSortNo(StringUtils.to10System(tDict.getSystemId().substring(n-2,n),36));
		
		int idlen = tDict.getSystemId().length();
		int no = NumberUtils.toInt(tDict.getClassNo());
		if(idlen/2!=no){
			logger.error("=====================数据字典"+tDict.getSystemId()+"的classNo设值有误。");
			tDict.setClassNo(idlen/2+"");
		}
		
		this.proDicDao.save(tDict);
		return "字典项添加成功！";
	}
	
	/**
	 * 修改字典节点
	 * @param dic
	 * @return
	 */
	@CacheEvict(value="DICT_CACHE",allEntries=true)  
	public String updateDict(TblProDic dic){
		ProDic t = this.get(dic.getSystemId());
		dic.copyEditProperties(t);
		this.proDicDao.update(t);
		return "字典项修改成功！";
	}
	
	
	/**
	 * 根据id得到直接下级子节点id集合
	 */
	public List<String> getdicId(String id){
		 List<String> resultlist=new ArrayList<String>();
		 try {
			List<TblProDic> list = this.findDictByPid(id);
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				TblProDic dictionaryDto = (TblProDic) iterator.next();
				resultlist.add(dictionaryDto.getSystemId());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			this.logger.error(e);
		}
		return resultlist;
	}
	
	
	/**
	 * 删除节点
	 * @param systemId
	 * @return
	 */
	@CacheEvict(value="DICT_CACHE",allEntries=true)  
	public Map<String, Object> delDict(String systemId){
		Map<String, Object> map = new HashMap<String, Object>();
		int count = this.haveNode(systemId); //判断被删除节点是否存在子节点
		if(count==0){
			ProDic t = this.get(systemId);
			int pCount = this.haveNode(t.getUpSystemId());//删除前，被删除节点的父节点是否只有一个子节点
			this.proDicDao.deleteObject(t);
			map.put("msg", "字典项添加删除成功！");
			if(!t.getUpSystemId().equals("0")){
				if(pCount==1)
					map.put("isleaf", true);
				else
					map.put("isleaf", false);
			}else{
				map.put("isleaf", false); //如果是顶级菜单，照样刷新顶级菜单
			}
		}else{
			map.put("isleaf", true);
			map.put("msg", "无法删除非叶子字典项。");
		}
		return map;
	}
	/**
	 * 异步加载Tree（初始化加载一、二级），后面加载该节点的子节点
	 * @param id
	 * @return
	 */
	@Cacheable(value="DICT_CACHE",key="#id + 'getTree'") 
	public List<TreeNode> getTree(String id){
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
		List<ProDic> dicList = null;
		if(StringUtils.isNull(id)){ //初始化
			id = "0";  //顶级父节点（classNo也为0）
			ProDic tDict=this.get(id);
			if(tDict==null){
				tDict = new ProDic();
				tDict.setSystemId(id);
				tDict.setChineseName("字典总类别");
				tDict.setUpSystemId(" ");
				tDict.setClassNo("0");
				this.proDicDao.save(tDict);
			}
			
			TreeNode tree = new TreeNode();
			tree.setId(tDict.getSystemId());
			tree.setText(tDict.getChineseName()+"("+tDict.getSystemId()+")");
			
			Map<String,String> attributesMap = new HashMap<String,String>();
			attributesMap.put("pid", tDict.getUpSystemId());
			attributesMap.put("ptext", tDict.getChineseName());
			attributesMap.put("classNo", tDict.getClassNo());
			tree.setAttributes(attributesMap);
	
			List<ProDic> chilList = this.findNodes(tDict.getSystemId());  //获取顶级节点下所有子节点
			
			List<TreeNode> chilTreeList = this.transformTreeDict(chilList);
			tree.setChildren(chilTreeList);
			list.add(tree);
		}else{
			dicList = this.findNodes(id);
			list = this.transformTreeDict(dicList);
		}
		return list;
	}
	/**
	 * 方法说明：根据字典id，递归所有子节点
	 * 参数说明：@param id
	 * 参数说明：@return
	 * @author Ou
	 * 2013-8-26
	 */
	@Cacheable(value="DICT_CACHE",key="#id + 'getRecursionDict'") 
	public List<TreeNode> getRecursionDict(String id){
		if(StringUtils.isNull(id))
			return null;
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
		
		TreeNode tree = new TreeNode();
		
		ProDic tdict=this.get(id);
		
		tree.setId(tdict.getSystemId());
		tree.setText(tdict.getChineseName());
		tree.setChildren(this.getTreeNode(id));
		
		list.add(tree);
		return list;
	}
	

		/**
		 * 方法说明：根据字典id，递归所有子节点
		 * 参数说明：@param id
		 * 参数说明：@return
		 * @author Ou
		 * 2013-8-26
		 */
	  @Cacheable(value="DICT_CACHE",key="#id + 'getComBoBoxTreeByFlag'") 
		public List<TreeNode> getComBoBoxTreeByFlag(String id,String flag){
			if(StringUtils.isNull(id))
				return null;
			List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
			TreeNode tree = new TreeNode();
			
			ProDic tdict=this.get(id);
			
			tree.setId(tdict.getSystemId());
			tree.setText(tdict.getChineseName());
			List<TreeNode> chiList = this.getTreeNodeComBoTree(id,flag);
			tree.setChildren(chiList);
			
			list.add(tree);
			return list;
		}
		

	private List<TreeNode> getTreeNode(String id){
		List<ProDic> chilList = this.findNodes(id);  
		
		List<TreeNode> chilTreeList = new ArrayList<TreeNode>();
		for(ProDic dic : chilList){
			TreeNode tree = new TreeNode();
			tree.setId(dic.getSystemId());
			tree.setText(dic.getChineseName());	
			
			int chilCount = this.haveNode(dic.getSystemId());  //判断是否存在子节点
			if(chilCount>0){
				tree.setState("closed");
				
				tree.setChildren(this.getTreeNode(dic.getSystemId()));
			}else{
				tree.setState("open");
			}
			
			chilTreeList.add(tree);
		}
		
		return chilTreeList;
	}
	
	private List<TreeNode> getTreeNodeComBoTree(String id,String flag){
		List<ProDic> chilList = this.findNodes(id);  
		List<TreeNode> chilTreeList = new ArrayList<TreeNode>();
		for(ProDic dic : chilList){
			Map<String, String> attributeMap =new HashMap<String, String>();
			TreeNode tree = new TreeNode();
			    if (StringUtils.isNotEmpty(flag)&& flag.equals("appType")) {//源从业务类型来
					tree.setId(dic.getPropertiy3());
					attributeMap.put("propertiy3", dic.getPropertiy3());
				}

				tree.setAttributes(attributeMap);
			    tree.setText(dic.getChineseName());	
			
			int chilCount = this.haveNode(dic.getSystemId());  //判断是否存在子节点
			if(chilCount>0){
				tree.setState("closed");
				tree.setChildren(this.getTreeNodeComBoTree(dic.getSystemId(),flag));
			}else{
				tree.setState("open");
			}
			chilTreeList.add(tree);
		}
		return chilTreeList;
	}
	
	/**
	 * 获取相同父节点的最大节点ID
	 * @param upSystemId
	 * @return
	 */
	@Cacheable(value="DICT_CACHE",key="#upSystemId + 'findMaxSystemId'") 
	private String findMaxSystemId(String upSystemId) {
		String hql = "Select max(dic.systemId) from ProDic dic where dic.upSystemId=:upSystemId";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("upSystemId", upSystemId);
		List list = this.proDicDao.find(hql,params);
		if(list.size()>0)
			return (String) list.get(0);
		return null;
	}
	
	/**
	 * DataDictonary的List转换为Tree的List
	 * @param dicList
	 * @return
	 */
	private List<TreeNode> transformTreeDict(List<ProDic> dicList){
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的List
		
		for(ProDic dic : dicList){
			TreeNode tree = new TreeNode();
			tree.setId(dic.getSystemId());
			//tree.setText(dic.getChineseName()+"("+dic.getSystemId()+")");
			tree.setText(dic.getChineseName());
			int chilCount = this.haveNode(dic.getSystemId());  //判断是否存在子节点
			if(chilCount>0)
				tree.setState("closed");
			else
				tree.setState("open");
			ProDic dd = this.get(dic.getUpSystemId());

			Map<String,Object> attributesMap = new HashMap<String,Object>();
			attributesMap.put("pid", dd.getUpSystemId());
			attributesMap.put("ptext", dd.getChineseName());
			attributesMap.put("classNo", dic.getClassNo());
			tree.setAttributes(attributesMap);
			
			list.add(tree);
		}
		
		return list;
	}
	
	/**
	 * 
	 * 子节点数量
	 * @param systemId
	 * @return
	 */
	@Cacheable(value="DICT_CACHE",key="#systemId + 'haveNode'") 
	private int haveNode(String systemId){
		int num=0;
		ConditionQuery query=new ConditionQuery();
		if (StringUtils.isNull(systemId)) {
			throw new IbbMgrException("数据字典systemId为空！");
		}else{
			query.add(Restrictions.eq("upSystemId", systemId));
			num=this.proDicDao.queryCountByCondition(query);
		}
		
		return num;
	}
	
	/**
	 * 根据ID获取实体
	 * @param systemId
	 * @return
	 */
	@Cacheable(value="DICT_CACHE",key="#systemId + 'get'") 
	private ProDic get(String systemId){
		if(StringUtils.isNotBlank(systemId)){
			return this.proDicDao.get(systemId);
		}
		return null;
	}
	/**
	 * 通过父节点获取所有子级节点
	 * @param upSystemId
	 * @return
	 */
	@Cacheable(value="DICT_CACHE",key="#upSystemId + 'findNodes'") 
	private List<ProDic> findNodes(String upSystemId){
		Map<String,Object> params = new HashMap<String,Object>();
		String hql = "from ProDic dic where dic.upSystemId= :upSystemId order by dic.sortNo";
		params.put("upSystemId", upSystemId);
		return this.proDicDao.find(hql,params);
	}
	
	@Cacheable(value="DICT_CACHE",key="#pid + 'findDictByPid'") 
	public List<TblProDic> findDictByPid(String pid){
		List<TblProDic> dtoList = new ArrayList<TblProDic>();
		List<ProDic> tList = this.findNodes(pid);
		for(ProDic t : tList){
			TblProDic dto = new TblProDic();
			BeanUtils.copyProperties(t, dto);
			dtoList.add(dto);
		}
		return dtoList;
	}
	/**
	 * 方法说明：查询指定的ids集合
	 * @param ids ids格式：01,02,03
	 * @return
	 * @author Ou
	 * 2014-4-9
	 */
	@Cacheable(value="DICT_CACHE",key="#ids + 'findDictByIds'") 
	public List<TblProDic> findDictByIds(String ids){
		Map<String,Object> params = new HashMap<String,Object>();
		List<String> idsList = new ArrayList<String>();
		String[] idss = ids.split(",");
		for(String id : idss){
			idsList.add(id);
		}
		ConditionQuery query=new ConditionQuery();
		if (idsList.size()>0) {
			query.add(Restrictions.in("systemId", idsList));
		}
		OrderBy orderBy=new OrderBy();
		orderBy.add(Order.asc("sortNo"));
		List<ProDic> tList = this.proDicDao.queryListByCondition(query, orderBy);
		
		
		List<TblProDic> dtoList = new ArrayList<TblProDic>();
		for(ProDic t : tList){
			TblProDic dto = new TblProDic();
			BeanUtils.copyProperties(t, dto);
			dtoList.add(dto);
		}
		return dtoList;
	}
	

	//@Cacheable(value="DICT_CACHE",key="#pid + 'findAllChilListDictByPid'") 
	@SuppressWarnings("unchecked")
	public PageUtils findAllChilListDictByPid(TblProDic dictionaryDto) {
		List<TblProDic> dtoList = new ArrayList<TblProDic>();
		PageUtils pageUtils = this.findChildListByPidParam(dictionaryDto);
		List<ProDic> tList  = (List<ProDic>) pageUtils.getRows();
		for(ProDic t : tList){
			TblProDic dto = new TblProDic();
			BeanUtils.copyProperties(t, dto);
			dtoList.add(dto);
		}
		PageUtils pageUtils2 = new PageUtils(pageUtils.getTotal(),dtoList);
		return pageUtils2;
	}
	
	/**
	 * 根据pid和查询参数模糊查询所有的子节点信息
	 * @return
	 */
	//@Cacheable(value="DICT_CACHE",key="#pidparam + 'findChildListByPidParam'") 
	private PageUtils findChildListByPidParam(TblProDic dictionaryDto){
		Map<String,Object> params = new HashMap<String,Object>();
		String fromhql = " from ProDic dic ";
		String counthql = "select count(systemId) ";
		String wherehql =" where dic.upSystemId  like :upSystemId ";
		ConditionQuery conditionQuery=new ConditionQuery();
		if (StringUtils.isNotNull(dictionaryDto.getQ())) {
			conditionQuery.add(Restrictions.like("chineseName", dictionaryDto.getQ()));
		}
		conditionQuery.add(Restrictions.eq("upSystemId",dictionaryDto.getSystemId()+"%"));
		List<ProDic> resultlist = this.proDicDao.queryListByCondition(conditionQuery,dictionaryDto.getPage(),dictionaryDto.getRows());
		
		int total = this.proDicDao.queryCountByCondition(conditionQuery);
		PageUtils pageUtils = new PageUtils(total,resultlist);
		return pageUtils;
	}
	
	
	@Cacheable(value="DICT_CACHE",key="#systemId + 'load'") 
	public TblProDic load(String systemId){
		ProDic t = this.get(systemId);
		TblProDic dto = new TblProDic();
		if(t!=null){
			BeanUtils.copyProperties(t, dto);
		}
		return dto;
	}
	@Cacheable(value="DICT_CACHE",key="#systemId + 'getChineseName'") 
	public String getChineseName(String systemId){
		ProDic t = this.get(systemId);
			if (t != null) {
				return t.getChineseName();
			}
		return "";
	}
	@Cacheable(value="DICT_CACHE",key="#systemId + #len + 'getChineseNameAndPerent'") 
	public String getChineseNameAndPerent(String systemId,int len){
		ProDic t = this.get(systemId);
		if (t != null) {
			if(len <= systemId.length()){
				ProDic pt = this.get(t.getUpSystemId());
				if (pt != null) {
					return StringBufferUtils.getString(pt.getChineseName(),"/",t.getChineseName());
				}
			}
			return t.getChineseName();
		}
		return "";
	}
	
	@Cacheable(value="DICT_CACHE",key="#pid + 'findChildNodes'") 
	public List<TblProDic> findChildNodes(String pid){
		List<ProDic> tList = this.findNodes(pid);
		List<TblProDic> mList = new ArrayList<TblProDic>();
		int l = tList.size();
		for(int i=0;i<l;i++){
			ProDic t = tList.get(i);
			TblProDic m = new TblProDic();
			BeanUtils.copyProperties(t, m);
			mList.add(m);
		}
		return mList;
	}
	@CacheEvict(value="DICT_CACHE",allEntries=true,key="#id + 'getRecursionDict'")
	public void clear(String id){
		
	}

	@Override
	public HashMap<String, ProDic> searchDicMap() {
		HashMap<String,ProDic> dicMap =new HashMap<String, ProDic>();
		String hql="from ProDic ";
		List<ProDic> list = this.proDicDao.queryListAll();
		for (ProDic dic : list) {
			dicMap.put(dic.getSystemId(), dic);
		}
		return dicMap;
	}
	public Map<String,ProDic> getDictMap(String id){
		if(StringUtils.isNull(id))
			return null;
		Map<String,ProDic> dictMap = new HashMap<String, ProDic>();
		ProDic tdict = this.get(id);
		if (tdict != null) {
			dictMap.put(tdict.getSystemId(), tdict);
			this.getTreeNodes(id,dictMap);
		}
		return dictMap;
	}
	@CacheEvict(value="DICT_CACHE",allEntries=true,key="#id + 'getDictMapNoPid'")
	public Map<String,ProDic> getDictMapNoPid(String id){
		if(StringUtils.isNull(id))
			return null;
		Map<String,ProDic> dictMap = new HashMap<String, ProDic>();
		ProDic tdict = this.get(id);
		if (tdict != null) {
			this.getTreeNodes(id,dictMap);
		}
		return dictMap;
	}
	
	@CacheEvict(value="DICT_CACHE",allEntries=true,key="#id + 'getDictMapByPropertiy'")
	public Map<String,ProDic> getDictMapByPropertiy(String id){
		if(StringUtils.isNull(id))
			return null;
		Map<String,ProDic> dictMap = new HashMap<String, ProDic>();
		ProDic tdict = this.get(id);
		if (tdict != null) {
			dictMap.put(tdict.getPropertiyValue(), tdict);
			this.getTreeNodeByPropertiys(id,dictMap);
		}
		return dictMap;
	}
	
	@CacheEvict(value="DICT_CACHE",allEntries=true,key="#id + 'getDictMapByPropertiyNoPid'")
	public Map<String,ProDic> getDictMapByPropertiyNoPid(String id){
		if(StringUtils.isNull(id))
			return null;
		Map<String,ProDic> dictMap = new HashMap<String, ProDic>();
		ProDic tdict = this.get(id);
		if (tdict != null) {
			this.getTreeNodeByPropertiys(id,dictMap);
		}
		return dictMap;
	}
	
	private void getTreeNodeByPropertiys(String id,Map<String,ProDic> dictMap){
		List<ProDic> chilList = this.findNodes(id);  
		for(ProDic dic : chilList){
			dictMap.put(dic.getPropertiyValue(), dic);
			
			int chilCount = this.haveNode(dic.getSystemId());  //判断是否存在子节点
			if(chilCount > 0){
				this.getTreeNodeByPropertiys(dic.getSystemId(),dictMap);
			}
		}
	}
	
	private void getTreeNodes(String id,Map<String,ProDic> dictMap){
		List<ProDic> chilList = this.findNodes(id);  
		for(ProDic dic : chilList){
			dictMap.put(dic.getSystemId(), dic);
			
			int chilCount = this.haveNode(dic.getSystemId());  //判断是否存在子节点
			if(chilCount > 0){
				this.getTreeNodes(dic.getSystemId(),dictMap);
			}
		}
	}
	
	@Cacheable(value="DICT_CACHE",key="#chineseName + 'getSystemidByCName'") 
	public String getSystemidByCName(String chineseName)
	{
		String hql = "from ProDic t where t.chineseName='" + chineseName + "'";
		List<ProDic> list = proDicDao.find(hql);
		if(list.size() > 0)
		{
			return list.get(0).getSystemId();
		}else
		{
			return null;
		}
	}

	@Override
	public String getSystemidByCNamePid(String chineseName, String pid) {
		String hql = "from ProDic t where t.chineseName=:chineseName  and  t.systemId like :pid ";
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("chineseName", chineseName);
		params.put("pid", pid+"%");
		List<ProDic> list = proDicDao.find(hql,params);
		if(list.size() > 0)
		{
			return list.get(0).getSystemId();
		}else
		{
			return null;
		}
	}

	/**
	 * 通过中文名获取字典集
	 */
	@Override
	public List<ProDic> findListByCName(String name) {
		String hql = "from ProDic t where t.chineseName='" + name + "'";
		return proDicDao.find(hql);
	}
	
	
	@Override
	public List<ProDic> findListByCNameold(String name) {
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = "select tc.* from t_base_data_dictionary tc ";
               sql+= "where (tc.chinese_name=:name or instr (tc.propertiy_value,:name1)>0) ";
        params.put("name",  name);
        params.put("name1", name);
        List<ProDic> resList = this.proDicDao.executeSQL(sql, params, ProDic.class);
	    return resList;
	}

	/**
	 * 通过中文名及上级中文名获取字典信息
	 */
	@Cacheable(value="DICT_CACHE",key="#name + #pname + 'findByNameAndParentName'") 
	public ProDic findByNameAndParentName(String name, String pname) {
		Map<String,Object> params = new HashMap<String,Object>();
		String hql = "select t from ProDic t,ProDic tp where t.upSystemId=tp.systemId and t.chineseName= :name and tp.chineseName= :pname";
		params.put("name", name);
		params.put("pname", pname);
		return proDicDao.get(hql, params);
	}
	/**
	 * 通过中文名及上级中文名获取字典信息（兼容曾用名）
	 */
	@SuppressWarnings("unchecked")
	@Cacheable(value="DICT_CACHE",key="#name + #pname + 'findByNameAndParentNameold'") 
	public ProDic findByNameAndParentNameold(String name, String pname) {
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = "select tc.* from t_base_data_dictionary tc,t_base_data_dictionary tp ";
               sql+= "where tc.up_system_id=tp.system_id and (tc.chinese_name=:name or instr (tc.propertiy_value,:name1)>0) ";
               sql+=" and (tp.chinese_name=:pname or instr (tp.propertiy_value,:pname1)>0) ";
        params.put("name",  name);
        params.put("name1", name);
        params.put("pname",  pname);
        params.put("pname1",pname);
        List<ProDic> resList = this.proDicDao.executeSQL(sql, params, ProDic.class);
        
	    if (resList.size()>0) {
	    	return resList.get(0);
		}else{
			return null;
		}
	}
	
	
	
	@Cacheable(value="DICT_CACHE",key="#chineseName + 'getByChineseName'") 
	public TblProDic getByChineseName(String chineseName)
	{
		String hql = "from ProDic t where t.chineseName='" + chineseName + "'";
		List<ProDic> list = proDicDao.find(hql);
		if(list.size() > 0)
		{
			TblProDic m = new TblProDic();
			BeanUtils.copyProperties(list.get(0), m);
			return m;
		}else
		{
			return null;
		}
	}

	@Override
	public List<TblProDic> findtreeLeveldept(int levellength) {
		String hql = "from ProDic t where LENGTH(t.systemId)="+levellength+" and t.systemId like '09%' ";
		List<TblProDic> dtoList = new ArrayList<TblProDic>();
		List<ProDic> tList = this.proDicDao.find(hql);
		for(ProDic t : tList){
			TblProDic dto = new TblProDic();
			BeanUtils.copyProperties(t, dto);
			dtoList.add(dto);
		}
		return dtoList;
	}

}
