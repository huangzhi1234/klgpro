package com.ibb.service.stDoc.impl;
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
import com.ibb.dao.stDoc.IDocDicDao;
import com.ibb.model.stDoc.DicInfo;
import com.ibb.model.stDoc.DocDicInfo;
import com.ibb.service.datadic.PageUtils;
import com.ibb.service.datadic.TreeNode;
import com.ibb.service.stDoc.IDocDicService;
import exception.IbbMgrException;
@Service("docDicService")
public class DocDicService extends BaseService<DocDicInfo, Integer> implements IDocDicService{
	private static final Logger logger=Logger.getLogger(DocDicService.class);
	@Autowired
	private IDocDicDao docDicDao;
	
	@Override
	@Autowired
	@Qualifier("docDicDao")
	public void setBaseDao(IBaseDao<DocDicInfo, Integer> baseDao) {
		this.baseDao=baseDao;
	}
	
	
	@Override
	public Page<DicInfo> getListByCondition(ConditionQuery query, int pn,int pageSize) {
		Integer count = this.docDicDao.queryCountByCondition(query);
        List<DocDicInfo> dicInfos = this.docDicDao.queryListByCondition(query, pn, pageSize);
        List<DicInfo> resultOrderDtos=new ArrayList<DicInfo>();
        DicInfo orderDto=null;
        for (DocDicInfo dicInfo : dicInfos) {
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
		List<DocDicInfo> dicList = null;
		if(StringUtils.isNull(id)){ //初始化
			  //顶级父节点（classNo也为0）
			DocDicInfo tDict=this.docDicDao.get("0");
			if(tDict==null){
				tDict = new DocDicInfo();
				tDict.setDicNum("0");
				tDict.setDicName("字典总类别");
				tDict.setOperId("");
				tDict.setLevel("0");
				this.docDicDao.save(tDict);
			}
		}
		dicList = this.docDicDao.findNodes(id);
		List<DocDicInfo> sendList = new ArrayList<DocDicInfo>();
		for(DocDicInfo temp  : dicList){
			if(temp.getDicNum().equals("0") ||  temp.getDicNum().startsWith("0")){
				sendList.add(temp);
			}
		}
		//update 
//		list = this.transformTreeDict(dicList);
		list = this.transformTreeDict(sendList);
		return list;
	}
	
	
	/**
	 * 异步加载Tree（初始化加载一、二级），后面加载该节点的子节点
	 * 父节点为1
	 * @param id
	 * @return
	 */
	@Override
	@Cacheable(value="DICT_CACHE",key="#id + 'getTree2'") 
	public List<TreeNode> getTree2(String id) {
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
		List<DocDicInfo> dicList = null;
		if(StringUtils.isNull(id)){ //初始化
			  //顶级父节点（classNo也为0）
			DocDicInfo tDict=this.docDicDao.get("1");
			if(tDict==null){
				tDict = new DocDicInfo();
				tDict.setDicNum("1");
				tDict.setDicName("字典总类别");
				tDict.setOperId("");
				tDict.setLevel("0");
				this.docDicDao.save(tDict);
			}
		}
		dicList = this.docDicDao.findNodes(id);
		List<DocDicInfo> sendList = new ArrayList<DocDicInfo>();
		for(DocDicInfo temp  : dicList){
			if(temp.getDicNum().equals("1") ||  temp.getDicNum().startsWith("1")){
				sendList.add(temp);
			}
		}
		//update 
//		list = this.transformTreeDict(dicList);
		list = this.transformTreeDict(sendList);
		return list;
	}
	
	/**
	 * 异步加载Tree（初始化加载一、二级），后面加载该节点的子节点
	 * 父节点为2
	 * @param id
	 * @return
	 */
	@Override
	@Cacheable(value="DICT_CACHE",key="#id + 'getTree3'") 
	public List<TreeNode> getTree3(String id) {
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
		List<DocDicInfo> dicList = null;
		if(StringUtils.isNull(id)){ //初始化
			  //顶级父节点（classNo也为0）
			DocDicInfo tDict=this.docDicDao.get("2");
			if(tDict==null){
				tDict = new DocDicInfo();
				tDict.setDicNum("2");
				tDict.setDicName("乐学空间");
				tDict.setOperId("");
				tDict.setLevel("0");
				this.docDicDao.save(tDict);
			}
		}
		dicList = this.docDicDao.findNodes(id);
		List<DocDicInfo> sendList = new ArrayList<DocDicInfo>();
		for(DocDicInfo temp  : dicList){
			if(temp.getDicNum().equals("2") ||  temp.getDicNum().startsWith("2")){
				sendList.add(temp);
			}
		}
		//update 
//		list = this.transformTreeDict(dicList);
		list = this.transformTreeDict(sendList);
		return list;
	}
	
	
	/**
	 * 异步加载Tree（初始化加载一、二级），后面加载该节点的子节点
	 * 父节点为2
	 * @param id
	 * @return
	 */
	@Override
	@Cacheable(value="DICT_CACHE",key="#id + 'getTree4'") 
	public List<TreeNode> getTree4(String id) {
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
		List<DocDicInfo> dicList = null;
		if(StringUtils.isNull(id)){ //初始化
			  //顶级父节点（classNo也为0）
			DocDicInfo tDict=this.docDicDao.get("3");
			if(tDict==null){
				tDict = new DocDicInfo();
				tDict.setDicNum("3");
				tDict.setDicName("经典案例");
				tDict.setOperId("");
				tDict.setLevel("0");
				this.docDicDao.save(tDict);
			}
		}
		dicList = this.docDicDao.findNodes(id);
		List<DocDicInfo> sendList = new ArrayList<DocDicInfo>();
		for(DocDicInfo temp  : dicList){
			if(temp.getDicNum().equals("3") ||  temp.getDicNum().startsWith("3")){
				sendList.add(temp);
			}
		}
		//update 
//		list = this.transformTreeDict(dicList);
		list = this.transformTreeDict(sendList);
		return list;
	}

	@Override
	/**
	 * 添加数据字典节点
	 * @param dic
	 * @return
	 */
	public String addDict(DicInfo dictDto) {
		DocDicInfo tDict = new DocDicInfo();
		BeanUtils.copyProperties(dictDto, tDict);
		/**
		 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
		 */
		String upClassNo = dictDto.getLevel();  //这个是父节点所在的层次
		if("zz".equals(upClassNo.trim()))
			return "字典项添加失败，当前节点已处于最大节点层。";
		String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
		tDict.setLevel(classNo);
		String findMaxSystemId = this.docDicDao.findMaxSystemId(dictDto.getOperId());
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
		this.docDicDao.save(tDict);
		return "字典项添加成功！";
	}
	
	@Override
	/**
	 * 添加数据字典节点   实施
	 * @param dic
	 * @return
	 */
	public String addDict2(DicInfo dictDto) {
		DocDicInfo tDict = new DocDicInfo();
		BeanUtils.copyProperties(dictDto, tDict);
		/**
		 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
		 */
		String upClassNo = dictDto.getLevel();  //这个是父节点所在的层次
		if("zz".equals(upClassNo.trim()))
			return "字典项添加失败，当前节点已处于最大节点层。";
		String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
		tDict.setLevel(classNo);
		String findMaxSystemId = this.docDicDao.findMaxSystemId(dictDto.getOperId());
		if(!StringUtils.isNull(findMaxSystemId)){
			if(!findMaxSystemId.equals("zz")){
				String sid = StringUtils.add36System(findMaxSystemId);
				tDict.setDicNum(sid);
			}else{
				return "字典项添加失败，该节点的子级节点编号已经到达最大值。";
			}
		}else{
			if("1".equals(classNo))
				tDict.setDicNum("11");
			else
				tDict.setDicNum(dictDto.getOperId()+"11");
		}
		int n = tDict.getDicNum().length();
		int idlen = tDict.getDicNum().length();
		int no = NumberUtils.toInt(tDict.getLevel());
		if(idlen/2!=no){
			logger.error("=====================数据字典"+tDict.getDicNum()+"的classNo设值有误。");
			tDict.setLevel(idlen/2+"");
		}
		this.docDicDao.save(tDict);
		return "字典项添加成功！";
	}
	
	
	@Override
	/**
	 * 添加数据字典节点  乐学空间
	 * @param dic
	 * @return
	 */
	public String addDict3(DicInfo dictDto) {
		DocDicInfo tDict = new DocDicInfo();
		BeanUtils.copyProperties(dictDto, tDict);
		/**
		 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
		 */
		String upClassNo = dictDto.getLevel();  //这个是父节点所在的层次
		if("zz".equals(upClassNo.trim()))
			return "字典项添加失败，当前节点已处于最大节点层。";
		String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
		tDict.setLevel(classNo);
		String findMaxSystemId = this.docDicDao.findMaxSystemId(dictDto.getOperId());
		if(!StringUtils.isNull(findMaxSystemId)){
			if(!findMaxSystemId.equals("zz")){
				String sid = StringUtils.add36System(findMaxSystemId);
				tDict.setDicNum(sid);
			}else{
				return "字典项添加失败，该节点的子级节点编号已经到达最大值。";
			}
		}else{
			if("1".equals(classNo))
				tDict.setDicNum("21");
			else
				tDict.setDicNum(dictDto.getOperId()+"21");
		}
		int n = tDict.getDicNum().length();
		int idlen = tDict.getDicNum().length();
		int no = NumberUtils.toInt(tDict.getLevel());
		if(idlen/2!=no){
			logger.error("=====================数据字典"+tDict.getDicNum()+"的classNo设值有误。");
			tDict.setLevel(idlen/2+"");
		}
		this.docDicDao.save(tDict);
		return "字典项添加成功！";
	}
	
	
	@Override
	/**
	 * 添加数据字典节点 经典案例
	 * @param dic
	 * @return
	 */
	public String addDict4(DicInfo dictDto) {
		DocDicInfo tDict = new DocDicInfo();
		BeanUtils.copyProperties(dictDto, tDict);
		/**
		 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
		 */
		String upClassNo = dictDto.getLevel();  //这个是父节点所在的层次
		if("zz".equals(upClassNo.trim()))
			return "字典项添加失败，当前节点已处于最大节点层。";
		String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
		tDict.setLevel(classNo);
		String findMaxSystemId = this.docDicDao.findMaxSystemId(dictDto.getOperId());
		if(!StringUtils.isNull(findMaxSystemId)){
			if(!findMaxSystemId.equals("zz")){
				String sid = StringUtils.add36System(findMaxSystemId);
				tDict.setDicNum(sid);
			}else{
				return "字典项添加失败，该节点的子级节点编号已经到达最大值。";
			}
		}else{
			if("1".equals(classNo))
				tDict.setDicNum("31");
			else
				tDict.setDicNum(dictDto.getOperId()+"31");
		}
		int n = tDict.getDicNum().length();
		int idlen = tDict.getDicNum().length();
		int no = NumberUtils.toInt(tDict.getLevel());
		if(idlen/2!=no){
			logger.error("=====================数据字典"+tDict.getDicNum()+"的classNo设值有误。");
			tDict.setLevel(idlen/2+"");
		}
		this.docDicDao.save(tDict);
		return "字典项添加成功！";
	}
	
	/**
	 * 修改字典节点
	 * @param dic
	 * @return
	 */
	@Override
	public String updateDict(DicInfo dic) {
		DocDicInfo t = this.docDicDao.get(dic.getDicNum());
		t.setDicType(dic.getDicType());
		dic.copyEditProperties(t);
		this.docDicDao.update(t);
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
			DocDicInfo t = this.docDicDao.get(systemId);
			int pCount = this.haveNode(t.getOperId());//删除前，被删除节点的父节点是否只有一个子节点
			//this.dictionaryDao.delete(t);
			this.docDicDao.deleteObject(t);
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
		DocDicInfo t = this.docDicDao.get(systemId);
		DicInfo dto = new DicInfo();
		if(t!=null){
			BeanUtils.copyProperties(t, dto);
		}
		return dto;
	}

	@Override
	public List<DicInfo> findDictByPid(String pid) {
		List<DicInfo> dtoList = new ArrayList<DicInfo>();
		List<DocDicInfo> tList = this.docDicDao.findNodes(pid);
		for(DocDicInfo t : tList){
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
		List<DocDicInfo> tList = this.docDicDao.queryListByCondition(query, orderBy);
		List<DicInfo> dtoList = new ArrayList<DicInfo>();
		for(DocDicInfo t : tList){
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
		DocDicInfo tdict=this.docDicDao.get(id);
		tree.setId(tdict.getDicNum());
		tree.setText(tdict.getDicName());
		tree.setChildren(this.getTreeNode(id));
		list.add(tree);
		return list;
	}

	@Override
	public String getChineseName(String systemId) {
		DocDicInfo t = this.docDicDao.get(systemId);
		if (t != null) {
			return t.getDicName();
		}
		return "";
	}

	@Override
	public PageUtils findAllChilListDictByPid(DicInfo dictionaryDto) {
		List<DicInfo> dtoList = new ArrayList<DicInfo>();
		PageUtils pageUtils = this.findChildListByPidParam(dictionaryDto);
		List<DocDicInfo> tList  = (List<DocDicInfo>) pageUtils.getRows();
		for(DocDicInfo t : tList){
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
	public HashMap<String, DocDicInfo> searchDicMap() {
		return null;
	}

	@Override
	public List<TreeNode> getComBoBoxTreeByFlag(String id, String flag) {
		return null;
	}

	@Override
	public Map<String, DocDicInfo> getDictMapNoPid(String id) {
		return null;
	}

	@Override
	public Map<String, DocDicInfo> getDictMap(String id) {
		return null;
	}

	@Override
	public Map<String, DocDicInfo> getDictMapByPropertiy(String id) {
		return null;
	}

	@Override
	public Map<String, DocDicInfo> getDictMapByPropertiyNoPid(String id) {
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
	public List<DocDicInfo> findListByCName(String name) {
		return null;
	}

	@Override
	public List<DocDicInfo> findListByCNameold(String name) {
		return null;
	}
	/**
	 * 通过中文名及上级中文名获取字典信息
	 */
	@Override
	public DocDicInfo findByNameAndParentName(String name, String pname) {
		return docDicDao.findByNameAndParentName( name,  pname) ;
	}

	@Override
	public DocDicInfo findByNameAndParentNameold(String name, String pname) {
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
		DocDicInfo t = this.docDicDao.get(systemId);
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
	private List<TreeNode> transformTreeDict(List<DocDicInfo> dicList){
		List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的List
		for(DocDicInfo dic : dicList){
			TreeNode tree = new TreeNode();
			tree.setId(dic.getDicNum());
			//tree.setText(dic.getDicName()+"("+dic.getDicNum()+")");		
			tree.setText(dic.getDicName());
			int chilCount = this.haveNode(dic.getDicNum());  //判断是否存在子节点
			if(chilCount>0)
				tree.setState("closed");
			else
				tree.setState("open");
			
			Map<String,Object> attributesMap = new HashMap<String,Object>();
			if(StringUtils.isNotNull(dic.getOperId())){
				DocDicInfo dd = this.docDicDao.get(dic.getOperId());
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
			num=this.docDicDao.queryCountByCondition(query);
		}
		return num;
	}
	private List<TreeNode> getTreeNode(String id){
		List<DocDicInfo> chilList = this.docDicDao.findNodes(id);  
		List<TreeNode> chilTreeList = new ArrayList<TreeNode>();
		for(DocDicInfo dic : chilList){
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
		List<DocDicInfo> resultlist = this.docDicDao.queryListByCondition(conditionQuery,dictionaryDto.getPage(),dictionaryDto.getRows());
		int total = this.docDicDao.queryCountByCondition(conditionQuery);
		PageUtils pageUtils = new PageUtils(total,resultlist);
		return pageUtils;
	}
}
