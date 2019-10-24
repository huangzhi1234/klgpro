package com.ibb.service.datadic.impl;


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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.service.impl.BaseService;
import com.ibb.common.util.NumberUtils;
import com.ibb.common.util.StringUtils;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.util.pagination.PageUtil;
import com.ibb.dao.datadic.ITblDicInfoDao;
import com.ibb.dto.datadic.DicDto;
import com.ibb.dto.datadic.DictionaryDto;
import com.ibb.model.datadic.TBaseDataDictionary;
import com.ibb.model.datadic.TblDicInfo;
import com.ibb.service.datadic.ITblDicInfoService;
import com.ibb.service.datadic.PageUtils;
import com.ibb.service.datadic.TreeNode;

import exception.IbbMgrException;

@Component
public class TblDicInfoService extends BaseService<TblDicInfo, Integer> implements ITblDicInfoService {
	private static final Logger logger=Logger.getLogger(TblDicInfoService.class);
	@Autowired
	private ITblDicInfoDao tblDicInfoDao;

	@Override
	@Autowired
	@Qualifier("tblDicInfoDao")
	public void setBaseDao(IBaseDao<TblDicInfo, Integer> baseDao) {
		// TODO Auto-generated method stub
		this.baseDao=baseDao;
		
	}
	
	
	    @Override
	    public Page<DicDto> getListByCondition(final ConditionQuery query, final int pn, final int pageSize){
	        Integer count = this.tblDicInfoDao.queryCountByCondition(query);
	        List<TblDicInfo> dicInfos = this.tblDicInfoDao.queryListByCondition(query, pn, pageSize);
	        List<DicDto> resultOrderDtos=new ArrayList<DicDto>();
	        DicDto orderDto=null;
	        for (TblDicInfo dicInfo : dicInfos) {
	        	 orderDto=new DicDto();
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
			List<TblDicInfo> dicList = null;
			if(StringUtils.isNull(id)){ //初始化
				  //顶级父节点（classNo也为0）
				TblDicInfo tDict=this.tblDicInfoDao.get("0");
				if(tDict==null){
					tDict = new TblDicInfo();
					tDict.setDicNum("0");
					tDict.setDicName("字典总类别");
					tDict.setOperId("");
					tDict.setLevel("0");
					this.tblDicInfoDao.save(tDict);
				}
			}
			dicList = this.tblDicInfoDao.findNodes(id);
			list = this.transformTreeDict(dicList);
			return list;
		}
		
		
		
		
		/**
		 * DataDictonary的List转换为Tree的List
		 * @param dicList
		 * @return
		 */
		private List<TreeNode> transformTreeDict(List<TblDicInfo> dicList){
			List<TreeNode> list = new ArrayList<TreeNode>(); //Tree的List
			
			for(TblDicInfo dic : dicList){
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
					TblDicInfo dd = this.tblDicInfoDao.get(dic.getOperId());
					
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
				num=this.tblDicInfoDao.queryCountByCondition(query);
			}
			
			return num;
		}
		
		
		
		/**
		 * 添加数据字典节点
		 * @param dic
		 * @return
		 */
		public String addDict(DicDto dictDto){
			TblDicInfo tDict = new TblDicInfo();
			BeanUtils.copyProperties(dictDto, tDict);
			/**
			 * 字典进制规则【第一层(仅此一个)：0、 第二层：01、 第三--N层：upSystemId+01】
			 */
			String upClassNo = dictDto.getLevel();  //这个是父节点所在的层次
			if("zz".equals(upClassNo.trim()))
				return "字典项添加失败，当前节点已处于最大节点层。";
			String classNo = StringUtils.add36System(upClassNo);  //父节点加1得到当前节点层次
			tDict.setLevel(classNo);
			
			String findMaxSystemId = this.tblDicInfoDao.findMaxSystemId(dictDto.getOperId());
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
			
			this.tblDicInfoDao.save(tDict);
			return "字典项添加成功！";
		}
		
		
		
		/**
		 * 修改字典节点
		 * @param dic
		 * @return
		 */
		public String updateDict(DicDto dic){
			TblDicInfo t = this.tblDicInfoDao.get(dic.getDicNum());
			dic.copyEditProperties(t);
			this.tblDicInfoDao.update(t);
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
				TblDicInfo t = this.tblDicInfoDao.get(systemId);
				int pCount = this.haveNode(t.getOperId());//删除前，被删除节点的父节点是否只有一个子节点
				//this.dictionaryDao.delete(t);
				this.tblDicInfoDao.deleteObject(t);
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
		
		
		
		public DicDto load(String systemId){
			TblDicInfo t = this.tblDicInfoDao.get(systemId);
			DicDto dto = new DicDto();
			if(t!=null){
				BeanUtils.copyProperties(t, dto);
			}
			return dto;
		}
		
		
		public List<DicDto> findDictByPid(String pid){
			List<DicDto> dtoList = new ArrayList<DicDto>();
			List<TblDicInfo> tList = this.tblDicInfoDao.findNodes(pid);
			for(TblDicInfo t : tList){
				DicDto dto = new DicDto();
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
		public List<DicDto> findDictByIds(String ids){
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
			List<TblDicInfo> tList = this.tblDicInfoDao.queryListByCondition(query, orderBy);
			
			
			List<DicDto> dtoList = new ArrayList<DicDto>();
			for(TblDicInfo t : tList){
				DicDto dto = new DicDto();
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
			
			TblDicInfo tdict=this.tblDicInfoDao.get(id);
			
			tree.setId(tdict.getDicNum());
			tree.setText(tdict.getDicName());
			tree.setChildren(this.getTreeNode(id));
			
			list.add(tree);
			return list;
		}
		

		private List<TreeNode> getTreeNode(String id){
			List<TblDicInfo> chilList = this.tblDicInfoDao.findNodes(id);  
			
			List<TreeNode> chilTreeList = new ArrayList<TreeNode>();
			for(TblDicInfo dic : chilList){
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
			TblDicInfo t = this.tblDicInfoDao.get(systemId);
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
			 List<DicDto> dicInfos = this.findDictByPid(upsystemId);
			 String[] isStrings=null;
			for (DicDto dicDto : dicInfos) {
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
		public PageUtils findAllChilListDictByPid(DicDto dictionaryDto) {
			// TODO Auto-generated method stub
			List<DicDto> dtoList = new ArrayList<DicDto>();
			PageUtils pageUtils = this.findChildListByPidParam(dictionaryDto);
			List<TblDicInfo> tList  = (List<TblDicInfo>) pageUtils.getRows();
			for(TblDicInfo t : tList){
				DicDto dto = new DicDto();
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
		private PageUtils findChildListByPidParam(DicDto dictionaryDto){
			ConditionQuery conditionQuery=new ConditionQuery();
			if (StringUtils.isNotNull(dictionaryDto.getQ())) {
				conditionQuery.add(Restrictions.like("dicName", dictionaryDto.getQ()));
			}
			conditionQuery.add(Restrictions.eq("operId",dictionaryDto.getDicNum()+"%"));
			List<TblDicInfo> resultlist = this.tblDicInfoDao.queryListByCondition(conditionQuery,dictionaryDto.getPage(),dictionaryDto.getRows());
			
			int total = this.tblDicInfoDao.queryCountByCondition(conditionQuery);
			PageUtils pageUtils = new PageUtils(total,resultlist);
			return pageUtils;
		}
		
		
		/**
		 * 通过中文名及上级中文名获取字典信息
		 */
		public TblDicInfo findByNameAndParentName(String name, String pname) {
			return tblDicInfoDao.findByNameAndParentName( name,  pname) ;
		}


		@Override
		public TblDicInfo findByNameAndParentNameold(String name, String pname) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<DicDto> findChildNodes(String pid) {
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
		public List<DicDto> findtreeLeveldept(int levellength) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public DicDto getByChineseName(String chinsesName) {
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
		
		
		/**
		 * 根据dicNum模糊查询所有的子节点信息
		 * @return
		 */
		public List<TblDicInfo> findChildListByDicNum(String dicNum){
			ConditionQuery conditionQuery=new ConditionQuery();
			conditionQuery.add(Restrictions.like("dicNum", dicNum+"__"));
			List<TblDicInfo> resultlist = this.tblDicInfoDao.queryListByCondition(conditionQuery);
			return resultlist;
		}
		public TblDicInfo queryByNum(String dicNum){
			TblDicInfo tbl=null;
			ConditionQuery conditionQuery=new ConditionQuery();
			conditionQuery.add(Restrictions.eq("dicNum", dicNum));
			List<TblDicInfo> resultlist = this.tblDicInfoDao.queryListByCondition(conditionQuery);
			for (TblDicInfo tblDicInfo : resultlist) {
				tbl=tblDicInfo;
			}
			return tbl;
		}


		
}