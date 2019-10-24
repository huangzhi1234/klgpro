package com.ibb.service.datadic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.IBaseService;
import com.ibb.common.util.pagination.Page;
import com.ibb.dto.datadic.DicDto;
import com.ibb.dto.datadic.DictionaryDto;
import com.ibb.model.datadic.TBaseDataDictionary;
import com.ibb.model.datadic.TblDicInfo;

public interface ITblDicInfoService extends IBaseService<TblDicInfo, Integer> {
	  public Page<DicDto> getListByCondition(final ConditionQuery query, final int pn, final int pageSize);
	  /**
		 * 异步加载Tree（初始化加载一、二级），后面加载该节点的子节点
		 * @param id
		 * @return
		 */
		@Cacheable(value="DICT_CACHE",key="#id + 'getTree'") 
		public List<TreeNode> getTree(String id);

		/**
		 * 添加数据字典节点
		 * @param dic
		 * @return
		 */
		public String addDict(DicDto dictDto);

		/**
		 * 修改字典节点
		 * @param dic
		 * @return
		 */
		public String updateDict(DicDto dic);
		/**
		 * 删除节点
		 * @param systemId
		 * @return
		 */
		public Map<String, Object> delDict(String systemId);
		public DicDto load(String systemId);
		public List<DicDto> findDictByPid(String pid);
		/**
		 * 方法说明：查询指定的ids集合
		 * @param ids ids格式：01,02,03
		 * @return
		 * @author Ou
		 * 2014-4-9
		 */
		public List<DicDto> findDictByIds(String ids);
		/**
		 * 方法说明：根据字典id，递归所有子节点
		 * 参数说明：@param id
		 * 参数说明：@return
		 * @author Ou
		 * 2013-8-26
		 */
		public List<TreeNode> getRecursionDict(String id);
		public String getChineseName(String systemId);
		/**
		 * 通过父节点ID获取所有的子节点，包含直属和非直属，并且添加模糊查询
		 * @param dictionaryDto
		 * @return
		 */
		public PageUtils findAllChilListDictByPid(DicDto dictionaryDto);
		/**
		 * 根据id得到直接下级子节点id集合
		 */
		public List<String> getdicId(String id);
		/**
		 * 方法说明：通过字典ID，获取字典中文名
		 * @param systemId
		 * @param len 当systemId长度大于等于len时返回 “父名称/子名称”；否则只返回systemId的“名称”
		 * @return
		 * @author Ou
		 * 2014-5-16
		 */
		public String getChineseNameAndPerent(String systemId,int len);
		/**
		 * 方法说明：通过字典ID，获取下一子集LIST
		 * 参数说明：@param pid
		 * 参数说明：@return
		 * @author Ou
		 * 2013-8-20
		 */
		public List<DicDto> findChildNodes(String pid);
		
		/**
		 * clear(清楚部门树缓存)
		 * @param  ID
		 * @Exception 异常对象
		 * @since  CodingExample　Ver(编码范例查看) 1.1
		 */
		public void clear(String id) ;
		
		
		/**查询所有的数据字典，保存map，字典id做key，字典对象做value
		 * @Exception 异常对象
		 * @since  CodingExample　Ver(编码范例查看) 1.1
		 */
		public HashMap<String, TblDicInfo> searchDicMap();
		
		/**
		 * 方法说明：根据字典id，递归所有子节点
		 * 参数说明：@param id
		 * 参数说明：@return
		 * @author Ou
		 * 2013-8-26
		 */
		public List<TreeNode> getComBoBoxTreeByFlag(String id,String flag);
		/**
		 * getDictMapNoPid(根据ID获取基础数据字典集以Map的方式缓存,不要父ID)
		 * @param  id	基础字典集顶级ID
		 * @return Map<String,TBaseDataDictionary>    DOM对象
		 * @Exception 异常对象
		 * @since  CodingExample　Ver(编码范例查看) 1.1
		 */
		public Map<String,TblDicInfo> getDictMapNoPid(String id) ;
		/**
		 * getDictMap(根据ID获取基础数据字典集以Map的方式缓存)
		 * @param  id	基础字典集顶级ID
		 * @return Map<String,TBaseDataDictionary>    DOM对象
		 * @Exception 异常对象
		 * @since  CodingExample　Ver(编码范例查看) 1.1
		 */
		public Map<String,TblDicInfo> getDictMap(String id);
		/**
		 * getDictMap(根据ID获取基础数据字典集以Map的方式缓存)
		 * @param  id	基础字典集顶级ID
		 * @return Map<String,TBaseDataDictionary>    DOM对象
		 * @Exception 异常对象
		 * @since  CodingExample　Ver(编码范例查看) 1.1
		 */
		public Map<String,TblDicInfo> getDictMapByPropertiy(String id);
		/**
		 * getDictMapByPropertiyNoPid(根据ID获取基础数据字典集以Map的方式缓存,不要父ID)
		 * @param  id	基础字典集顶级ID
		 * @return Map<String,TBaseDataDictionary>    DOM对象
		 * @Exception 异常对象
		 * @since  CodingExample　Ver(编码范例查看) 1.1
		 */
		public Map<String,TblDicInfo> getDictMapByPropertiyNoPid(String id);
		
		/**
		 * 方法说明：通过中文名获取字典ID
		 * @param chineseName
		 * @return
		 * 小严
		 * 2013-9-9
		 */
		public String getSystemidByCName(String chineseName);
		
		/**
		 * 方法说明: 通过中文名获取字典对象
		 * @param chinsesName
		 * @return
		 * 小严
		 * 2013-9-13
		 */
		public DicDto getByChineseName(String chinsesName);
		
		/**
		 * 方法说明：通过中文名获取字典集
		 * @param name
		 * @return
		 * @author lyl
		 * 2013-9-12
		 */
		public List<TblDicInfo> findListByCName(String name);
		/**
		 * 方法说明：通过中文名获取字典集（兼容曾用名）
		 * @param name
		 * @return
		 * @author lyl
		 * 2013-9-12
		 */
		public List<TblDicInfo> findListByCNameold(String name);
		
		
		/**
		 * 方法说明：通过中文名及上级中文名获取字典信息
		 * @param name
		 * @param pname
		 * @return
		 * @author lyl
		 * 2013-9-12
		 */
		public TblDicInfo findByNameAndParentName(String name,String pname);
		
		/**
		 * 方法说明：通过中文名及上级中文名获取字典信息(兼容曾用名)
		 * @param name
		 * @param pname
		 * @return
		 * @author lyl
		 * 2013-9-12
		 */
		public TblDicInfo findByNameAndParentNameold(String name,String pname);

		/**
		 * 根据等级查询组织
		 * @return
		 */
	    public List<DicDto> findtreeLeveldept(int levellength);
	    
	    /**
	     * 根据中文名称和pid查询该pid下的数据字典
	     * @param chineseName
	     * @return
	     */
		public String getSystemidByCNamePid(String chineseName,String pid);
		/**
		 * 得到中文名称
		 *@param upsystemId 父节点
		 *@param propertiyValue 属性值
		 *@return
		 *@author：zhousiliang
		 *@Create Date：2014-10-30
		 */
		public String getChineseName(String upsystemId,String propertiyValue);
}
