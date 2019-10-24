package com.ibb.service.depart.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.service.impl.BaseService;
import com.ibb.common.util.NumberUtils;
import com.ibb.common.util.StringUtils;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.util.pagination.PageUtil;
import com.ibb.dao.depart.IDepartDicDao;
import com.ibb.model.depart.DepartDicInfo;
import com.ibb.model.depart.DicInfo;
import com.ibb.service.datadic.PageUtils;
import com.ibb.service.datadic.TreeNode;
import com.ibb.service.depart.IDepartDicService;
import com.ibb.service.stDoc.impl.DocDicService;

import exception.IbbMgrException;
@Service("departDicService")
public class DepartDicService extends BaseService<DepartDicInfo, Integer> implements IDepartDicService{
	private static final Logger logger=Logger.getLogger(DocDicService.class);
	@Autowired
	private IDepartDicDao departDicDao;
	
	@Override
	@Autowired
	@Qualifier("departDicDao")
	public void setBaseDao(IBaseDao<DepartDicInfo, Integer> baseDao) {
		this.baseDao=baseDao;
	}
	
	
	@Override
	public Page<DicInfo> getListByCondition(ConditionQuery query, int pn,int pageSize) {
		Integer count = this.departDicDao.queryCountByCondition(query);
        List<DepartDicInfo> dicInfos = this.departDicDao.queryListByCondition(query, pn, pageSize);
        List<DicInfo> resultOrderDtos=new ArrayList<DicInfo>();
        DicInfo orderDto=null;
        for (DepartDicInfo dicInfo : dicInfos) {
        	 orderDto=new DicInfo();
			 BeanUtils.copyProperties(dicInfo, orderDto);
			 resultOrderDtos.add(orderDto);
		}
        return PageUtil.getPage(count, pn, resultOrderDtos, pageSize);
	}
	/**
	 * 异步加载Tree（初始化加载一、二级），后面加载该节点的子节点
	 * @param id
	 * @return
	 */
	@Override
	@Cacheable(value="DICT_CACHE",key="#id + 'getTree'") 
	public List<TreeNode> getTree(String id) {
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
		List<DepartDicInfo> dicList = null;
		if(StringUtils.isNull(id)){ //初始化
			  //顶级父节点（classNo也为0）
			DepartDicInfo tDict=this.departDicDao.get("0");
			if(tDict==null){
				tDict = new DepartDicInfo();
				tDict.setDicNum("0");
				tDict.setDicName("字典总类别");
				tDict.setOperId("");
				tDict.setLevel("0");
				this.departDicDao.save(tDict);
			}
		}
		dicList = this.departDicDao.findNodes(id);
		list = this.transformTreeDict(dicList);
		return list;
	}

	@Override
	/**
	 * 添加数据字典节点
	 * @param dic
	 * @return
	 */
	public String addDict(DicInfo dictDto) {
		DepartDicInfo tDict = new DepartDicInfo();
		BeanUtils.copyProperties(dictDto, tDict);
		/**
		 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
		 */
		String upClassNo = dictDto.getLevel();  //这个是父节点所在的层次
		if("zz".equals(upClassNo.trim()))
			return "字典项添加失败，当前节点已处于最大节点层。";
		String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
		tDict.setLevel(classNo);
		String findMaxSystemId = this.departDicDao.findMaxSystemId(dictDto.getOperId());
		if(!StringUtils.isNull(findMaxSystemId)){
			if(!findMaxSystemId.equals("zz")){
				String sid = StringUtils.add36System(findMaxSystemId);
				tDict.setDicNum(sid);
			}else{
				return "字典项添加失败，该节点的子级节点编号已经到达最大值。";
			}
		}else{
			if("1".equals(classNo))
				tDict.setDicNum("01");
			else
				tDict.setDicNum(dictDto.getOperId()+"01");
		}
		int n = tDict.getDicNum().length();
		int idlen = tDict.getDicNum().length();
		int no = NumberUtils.toInt(tDict.getLevel());
		if(idlen/2!=no){
			logger.error("=====================数据字典"+tDict.getDicNum()+"的classNo设值有误。");
			tDict.setLevel(idlen/2+"");
		}
		this.departDicDao.save(tDict);
		return "字典项添加成功！";
	}
	/**
	 * 修改字典节点
	 * @param dic
	 * @return
	 */
	@Override
	public String updateDict(DicInfo dic) {
		DepartDicInfo t = this.departDicDao.get(dic.getDicNum());
		dic.copyEditProperties(t);
		this.departDicDao.update(t);
		return "字典项修改成功！";
	}
	/**
	 * 删除节点
	 * @param systemId
	 * @return
	 */
	@Override
	public Map<String, Object> delDict(String systemId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int count = this.haveNode(systemId); //判断被删除节点是否存在子节点
		if(count==0){
			DepartDicInfo t = this.departDicDao.get(systemId);
			int pCount = this.haveNode(t.getOperId());//删除前，被删除节点的父节点是否只有一个子节点
			//this.dictionaryDao.delete(t);
			this.departDicDao.deleteObject(t);
			map.put("msg", "字典项添加删除成功！");
			if(!t.getOperId().equals("0")){
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

	@Override
	public DicInfo load(String systemId) {
		DepartDicInfo t = this.departDicDao.get(systemId);
		DicInfo dto = new DicInfo();
		if(t!=null){
			BeanUtils.copyProperties(t, dto);
		}
		return dto;
	}

	@Override
	public List<DicInfo> findDictByPid(String pid) {
		List<DicInfo> dtoList = new ArrayList<DicInfo>();
		List<DepartDicInfo> tList = this.departDicDao.findNodes(pid);
		for(DepartDicInfo t : tList){
			DicInfo dto = new DicInfo();
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
	@Override
	public List<DicInfo> findDictByIds(String ids) {
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
		List<DepartDicInfo> tList = this.departDicDao.queryListByCondition(query, orderBy);
		List<DicInfo> dtoList = new ArrayList<DicInfo>();
		for(DepartDicInfo t : tList){
			DicInfo dto = new DicInfo();
			BeanUtils.copyProperties(t, dto);
			dtoList.add(dto);
		}
		return dtoList;
	}
	/**
	 * 方法说明：根据字典id，递归所有子节点
	 * 参数说明：@param id
	 * 参数说明：@return
	 * @author Ou
	 * 2013-8-26
	 */
	@Override
	public List<TreeNode> getRecursionDict(String id) {
		if(StringUtils.isNull(id))
			return null;
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
		TreeNode tree = new TreeNode();
		DepartDicInfo tdict=this.departDicDao.get(id);
		tree.setId(tdict.getDicNum());
		tree.setText(tdict.getDicName());
		tree.setChildren(this.getTreeNode(id));
		list.add(tree);
		return list;
	}

	@Override
	public String getChineseName(String systemId) {
		DepartDicInfo t = this.departDicDao.get(systemId);
		if (t != null) {
			return t.getDicName();
		}
		return "";
	}

	@Override
	public PageUtils findAllChilListDictByPid(DicInfo dictionaryDto) {
		List<DicInfo> dtoList = new ArrayList<DicInfo>();
		PageUtils pageUtils = this.findChildListByPidParam(dictionaryDto);
		List<DepartDicInfo> tList  = (List<DepartDicInfo>) pageUtils.getRows();
		for(DepartDicInfo t : tList){
			DicInfo dto = new DicInfo();
			BeanUtils.copyProperties(t, dto);
			dtoList.add(dto);
		}
		PageUtils pageUtils2 = new PageUtils(pageUtils.getTotal(),dtoList);
		return pageUtils2;
	}

	@Override
	public List<String> getdicId(String id) {
		return null;
	}

	@Override
	public String getChineseNameAndPerent(String systemId, int len) {
		return null;
	}

	@Override
	public List<DicInfo> findChildNodes(String pid) {
		return null;
	}

	@Override
	public void clear(String id) {
	}

	@Override
	public HashMap<String, DepartDicInfo> searchDicMap() {
		return null;
	}

	@Override
	public List<TreeNode> getComBoBoxTreeByFlag(String id, String flag) {
		return null;
	}

	@Override
	public Map<String, DepartDicInfo> getDictMapNoPid(String id) {
		return null;
	}

	@Override
	public Map<String, DepartDicInfo> getDictMap(String id) {
		return null;
	}

	@Override
	public Map<String, DepartDicInfo> getDictMapByPropertiy(String id) {
		return null;
	}

	@Override
	public Map<String, DepartDicInfo> getDictMapByPropertiyNoPid(String id) {
		return null;
	}

	@Override
	public String getSystemidByCName(String chineseName) {
		return null;
	}

	@Override
	public DicInfo getByChineseName(String chinsesName) {
		return null;
	}

	@Override
	public List<DepartDicInfo> findListByCName(String name) {
		return null;
	}

	@Override
	public List<DepartDicInfo> findListByCNameold(String name) {
		return null;
	}
	/**
	 * 通过中文名及上级中文名获取字典信息
	 */
	@Override
	public DepartDicInfo findByNameAndParentName(String name, String pname) {
		return departDicDao.findByNameAndParentName( name,  pname) ;
	}

	@Override
	public DepartDicInfo findByNameAndParentNameold(String name, String pname) {
		return null;
	}

	@Override
	public List<DicInfo> findtreeLeveldept(int levellength) {
		return null;
	}

	@Override
	public String getSystemidByCNamePid(String chineseName, String pid) {
		return null;
	}

	@Override
	public String getChineseName(String systemId, String propertiyValue) {
		DepartDicInfo t = this.departDicDao.get(systemId);
		if (t != null) {
			return t.getDicName();
		}
		return "";
	}
	/**
	 * DataDictonary的List转换为Tree的List
	 * @param dicList
	 * @return
	 */
	private List<TreeNode> transformTreeDict(List<DepartDicInfo> dicList){
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的List
		for(DepartDicInfo dic : dicList){
			TreeNode tree = new TreeNode();
			tree.setId(dic.getDicNum());
			tree.setText(dic.getDicName()+"("+dic.getDicNum()+")");			
			int chilCount = this.haveNode(dic.getDicNum());  //判断是否存在子节点
			if(chilCount>0)
				tree.setState("closed");
			else
				tree.setState("open");
			
			Map<String,Object> attributesMap = new HashMap<String,Object>();
			if(StringUtils.isNotNull(dic.getOperId())){
				DepartDicInfo dd = this.departDicDao.get(dic.getOperId());
				attributesMap.put("pid", dd.getDicNum());
				attributesMap.put("ptext", dd.getDicName());
			}
			attributesMap.put("classNo", dic.getLevel());
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
	private int haveNode(String systemId){
		int num=0;
		ConditionQuery query=new ConditionQuery();
		if (StringUtils.isNull(systemId)) {
			throw new IbbMgrException("数据字典systemId为空！");
		}else{
			query.add(Restrictions.eq("operId", systemId));
			num=this.departDicDao.queryCountByCondition(query);
		}
		return num;
	}
	private List<TreeNode> getTreeNode(String id){
		List<DepartDicInfo> chilList = this.departDicDao.findNodes(id);  
		List<TreeNode> chilTreeList = new ArrayList<TreeNode>();
		for(DepartDicInfo dic : chilList){
			TreeNode tree = new TreeNode();
			tree.setId(dic.getDicNum());
			tree.setText(dic.getDicName());	
			int chilCount = this.haveNode(dic.getDicNum());  //判断是否存在子节点
			if(chilCount>0){
				tree.setState("closed");
				tree.setChildren(this.getTreeNode(dic.getDicNum()));
			}else{
				tree.setState("open");
			}
			
			chilTreeList.add(tree);
		}
		return chilTreeList;
	}
	/**
	 * 根据pid和查询参数模糊查询所有的子节点信息
	 * @return
	 */
	//@Cacheable(value="DICT_CACHE",key="#pidparam + 'findChildListByPidParam'") 
	private PageUtils findChildListByPidParam(DicInfo dictionaryDto){
		ConditionQuery conditionQuery=new ConditionQuery();
		if (StringUtils.isNotNull(dictionaryDto.getQ())) {
			conditionQuery.add(Restrictions.like("dicName", dictionaryDto.getQ()));
		}
		conditionQuery.add(Restrictions.eq("operId",dictionaryDto.getDicNum()+"%"));
		List<DepartDicInfo> resultlist = this.departDicDao.queryListByCondition(conditionQuery,dictionaryDto.getPage(),dictionaryDto.getRows());
		int total = this.departDicDao.queryCountByCondition(conditionQuery);
		PageUtils pageUtils = new PageUtils(total,resultlist);
		return pageUtils;
	}
	/**
	 * 查询部门集合
	 * @return
	 */
	@Override
	public List<DepartDicInfo> getAllDeptList() {
		ConditionQuery query=new ConditionQuery();
		query.add(Restrictions.eq("level","1"));
		List<DepartDicInfo> list =this.departDicDao.queryListByCondition(query);
		return list;
	}
}
