package com.ibb.service.patch.impl;

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
import com.ibb.dao.patch.ITblProDicInfoDao;
import com.ibb.model.datadic.TblDicInfo;
import com.ibb.model.patch.ProDicInfo;
import com.ibb.model.patch.TblProDicInfo;
import com.ibb.service.datadic.PageUtils;
import com.ibb.service.datadic.TreeNode;
import com.ibb.service.patch.ITblProDicInfoService;

import exception.IbbMgrException;

@Service("tblProDicInfoService")
public class TblProDicInfoService extends BaseService<TblProDicInfo, Integer> implements ITblProDicInfoService{

	private static final Logger logger=Logger.getLogger(TblProDicInfoService.class);
	@Autowired
	private ITblProDicInfoDao tblProDicInfoDao;

	@Override
	@Autowired
	@Qualifier("tblProDicInfoDao")
	public void setBaseDao(IBaseDao<TblProDicInfo, Integer> baseDao) {
		// TODO Auto-generated method stub
		this.baseDao=baseDao;
		
	}
	
	
	    @Override
	    public Page<ProDicInfo> getListByCondition(final ConditionQuery query, final int pn, final int pageSize){
	        Integer count = this.tblProDicInfoDao.queryCountByCondition(query);
	        List<TblProDicInfo> dicInfos = this.tblProDicInfoDao.queryListByCondition(query, pn, pageSize);
	        List<ProDicInfo> resultOrderDtos=new ArrayList<ProDicInfo>();
	        ProDicInfo orderDto=null;
	        for (TblProDicInfo dicInfo : dicInfos) {
	        	 orderDto=new ProDicInfo();
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
		@Cacheable(value="DICT_CACHE",key="#id + 'getTree'") 
		public List<TreeNode> getTree(String id){
			List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
			List<TblProDicInfo> dicList = null;
			if(StringUtils.isNull(id)){ //初始化
				  //顶级父节点（classNo也为0）
				TblProDicInfo tDict=this.tblProDicInfoDao.get("0");
				if(tDict==null){
					tDict = new TblProDicInfo();
					tDict.setDicNum("0");
					tDict.setDicName("字典总类别");
					tDict.setOperId("");
					tDict.setLevel("0");
					this.tblProDicInfoDao.save(tDict);
				}
			}
			dicList = this.tblProDicInfoDao.findNodes(id);
			//过滤：只返回研发成果节点
			List<TblProDicInfo> dicList2 = new ArrayList<TblProDicInfo>();
			if(dicList!=null && dicList.size()>0){
				for (TblProDicInfo tb : dicList) {
					if(tb.getDicNum().equals("0")||tb.getDicNum().startsWith("0")){
						dicList2.add(tb);
					}
				}
			}
			
			list = this.transformTreeDict(dicList2);
			return list;
		}
		
		/**
		 * 异步加载Tree（初始化加载一、二级），后面加载该节点的子节点
		 * @param id
		 * @return
		 */
		@Cacheable(value="DICT_CACHE",key="#id + 'getTree'") 
		public List<TreeNode> getSTree(String id){
			List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
			List<TblProDicInfo> dicList = null;
			
			if(StringUtils.isNull(id)){ //初始化
				  //顶级父节点（classNo也为0）
				TblProDicInfo tDict=this.tblProDicInfoDao.get("1");
				if(tDict==null){
					tDict = new TblProDicInfo();
					tDict.setDicNum("1");
					tDict.setDicName("实施成果");
					tDict.setOperId("");
					tDict.setLevel("0");
					this.tblProDicInfoDao.save(tDict);
				}
			}
			dicList = this.tblProDicInfoDao.findNodes(id);
			//过滤：只返回实施成果节点
			List<TblProDicInfo> dicList2 = new ArrayList<TblProDicInfo>();
			if(dicList!=null && dicList.size()>0){
				for (TblProDicInfo tb : dicList) {
					if(tb.getDicNum().equals("1")||tb.getDicNum().startsWith("1")){
						dicList2.add(tb);
					}
				}
			}
			list = this.transformTreeDict(dicList2);
			return list;
		}
		
		
		
		
		/**
		 * DataDictonary的List转换为Tree的List
		 * @param dicList
		 * @return
		 */
		private List<TreeNode> transformTreeDict(List<TblProDicInfo> dicList){
			List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的List
			
			for(TblProDicInfo dic : dicList){
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
					TblProDicInfo dd = this.tblProDicInfoDao.get(dic.getOperId());
					
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
				num=this.tblProDicInfoDao.queryCountByCondition(query);
			}
			
			return num;
		}
		
		
		
		/**
		 * 添加数据字典节点
		 * @param dic
		 * @return
		 */
		public String addDict(ProDicInfo dictDto){
			TblProDicInfo tDict = new TblProDicInfo();
			BeanUtils.copyProperties(dictDto, tDict);
			/**
			 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
			 */
			String upClassNo = dictDto.getLevel();  //这个是父节点所在的层次
			if("zz".equals(upClassNo.trim()))
				return "字典项添加失败，当前节点已处于最大节点层。";
			String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
			tDict.setLevel(classNo);
			
			String findMaxSystemId = this.tblProDicInfoDao.findMaxSystemId(dictDto.getOperId());
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
			
			this.tblProDicInfoDao.save(tDict);
			return "字典项添加成功！";
		}
		
		
		/**
		 * 添加数据字典节点
		 * @param dic
		 * @return
		 */
		public String addDict2(ProDicInfo dictDto){
			TblProDicInfo tDict = new TblProDicInfo();
			BeanUtils.copyProperties(dictDto, tDict);
			/**
			 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
			 */
			String upClassNo = dictDto.getLevel();  //这个是父节点所在的层次
			if("zz".equals(upClassNo.trim()))
				return "字典项添加失败，当前节点已处于最大节点层。";
			String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
			tDict.setLevel(classNo);
			
			String findMaxSystemId = this.tblProDicInfoDao.findMaxSystemId(dictDto.getOperId());
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
			
			this.tblProDicInfoDao.save(tDict);
			return "字典项添加成功！";
		}
		
		
		
		/**
		 * 修改字典节点
		 * @param dic
		 * @return
		 */
		public String updateDict(ProDicInfo dic){
			TblProDicInfo t = this.tblProDicInfoDao.get(dic.getDicNum());
			dic.copyEditProperties(t);
			this.tblProDicInfoDao.update(t);
			return "字典项修改成功！";
		}
	
		
		
		/**
		 * 删除节点
		 * @param systemId
		 * @return
		 */
		public Map<String, Object> delDict(String systemId){
			Map<String, Object> map = new HashMap<String, Object>();
			int count = this.haveNode(systemId); //判断被删除节点是否存在子节点
			if(count==0){
				TblProDicInfo t = this.tblProDicInfoDao.get(systemId);
				int pCount = this.haveNode(t.getOperId());//删除前，被删除节点的父节点是否只有一个子节点
				//this.dictionaryDao.delete(t);
				this.tblProDicInfoDao.deleteObject(t);
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
				map.put("isshenhe", true);
				map.put("msg", "无法删除非叶子字典项。");
			}
			return map;
		}
		
		
		
		public ProDicInfo load(String systemId){
			TblProDicInfo t = this.tblProDicInfoDao.get(systemId);
			ProDicInfo dto = new ProDicInfo();
			if(t!=null){
				BeanUtils.copyProperties(t, dto);
			}
			return dto;
		}
		
		
		public List<ProDicInfo> findDictByPid(String pid){
			List<ProDicInfo> dtoList = new ArrayList<ProDicInfo>();
			List<TblProDicInfo> tList = this.tblProDicInfoDao.findNodes(pid);
			for(TblProDicInfo t : tList){
				ProDicInfo dto = new ProDicInfo();
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
		public List<ProDicInfo> findDictByIds(String ids){
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
			List<TblProDicInfo> tList = this.tblProDicInfoDao.queryListByCondition(query, orderBy);
			
			
			List<ProDicInfo> dtoList = new ArrayList<ProDicInfo>();
			for(TblProDicInfo t : tList){
				ProDicInfo dto = new ProDicInfo();
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
		public List<TreeNode> getRecursionDict(String id){
			if(StringUtils.isNull(id))
				return null;
			List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的children
			
			TreeNode tree = new TreeNode();
			
			TblProDicInfo tdict=this.tblProDicInfoDao.get(id);
			
			tree.setId(tdict.getDicNum());
			tree.setText(tdict.getDicName());
			tree.setChildren(this.getTreeNode(id));
			
			list.add(tree);
			return list;
		}
		

		private List<TreeNode> getTreeNode(String id){
			List<TblProDicInfo> chilList = this.tblProDicInfoDao.findNodes(id);  
			
			List<TreeNode> chilTreeList = new ArrayList<TreeNode>();
			for(TblProDicInfo dic : chilList){
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
		
		
		public String getChineseName(String systemId){
			TblProDicInfo t = this.tblProDicInfoDao.get(systemId);
			if (t != null) {
				return t.getDicName();
			}
		return "";
	  }
		
		/**
		 * 得到中文名称
		 *@param upsystemId 父节点
		 *@param propertiyValue 属性值
		 *@return
		 *@author：zhousiliang
		 *@Create Date：2014-10-30
		 */
		public String getChineseName(String upsystemId,String propertiyValue){
			 List<ProDicInfo> dicInfos = this.findDictByPid(upsystemId);
			 String[] isStrings=null;
			for (ProDicInfo dicDto : dicInfos) {
				 isStrings=dicDto.getPropertiyValue().split(",");
				 for (String string : isStrings) {
					 if (string.equals(propertiyValue)) {
						 return dicDto.getDicName();
					 } 
				 }
			}
		    return "";
	  }	


		@Override
		public void clear(String id) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public PageUtils findAllChilListDictByPid(ProDicInfo dictionaryDto) {
			// TODO Auto-generated method stub
			List<ProDicInfo> dtoList = new ArrayList<ProDicInfo>();
			PageUtils pageUtils = this.findChildListByPidParam(dictionaryDto);
			List<TblDicInfo> tList  = (List<TblDicInfo>) pageUtils.getRows();
			for(TblDicInfo t : tList){
				ProDicInfo dto = new ProDicInfo();
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
		private PageUtils findChildListByPidParam(ProDicInfo dictionaryDto){
			ConditionQuery conditionQuery=new ConditionQuery();
			if (StringUtils.isNotNull(dictionaryDto.getQ())) {
				conditionQuery.add(Restrictions.like("dicName", dictionaryDto.getQ()));
			}
			conditionQuery.add(Restrictions.eq("operId",dictionaryDto.getDicNum()+"%"));
			List<TblProDicInfo> resultlist = this.tblProDicInfoDao.queryListByCondition(conditionQuery,dictionaryDto.getPage(),dictionaryDto.getRows());
			
			int total = this.tblProDicInfoDao.queryCountByCondition(conditionQuery);
			PageUtils pageUtils = new PageUtils(total,resultlist);
			return pageUtils;
		}
		
		
		/**
		 * 通过中文名及上级中文名获取字典信息
		 */
		public TblProDicInfo findByNameAndParentName(String name, String pname) {
			return tblProDicInfoDao.findByNameAndParentName( name,  pname) ;
		}


		@Override
		public TblDicInfo findByNameAndParentNameold(String name, String pname) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<ProDicInfo> findChildNodes(String pid) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<TblDicInfo> findListByCName(String name) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<TblDicInfo> findListByCNameold(String name) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<ProDicInfo> findtreeLeveldept(int levellength) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public ProDicInfo getByChineseName(String chinsesName) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public String getChineseNameAndPerent(String systemId, int len) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<TreeNode> getComBoBoxTreeByFlag(String id, String flag) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public Map<String, TblDicInfo> getDictMap(String id) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public Map<String, TblDicInfo> getDictMapByPropertiy(String id) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public Map<String, TblDicInfo> getDictMapByPropertiyNoPid(String id) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public Map<String, TblDicInfo> getDictMapNoPid(String id) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public String getSystemidByCName(String chineseName) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public String getSystemidByCNamePid(String chineseName, String pid) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<String> getdicId(String id) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public HashMap<String, TblDicInfo> searchDicMap() {
			// TODO Auto-generated method stub
			return null;
		}
}
