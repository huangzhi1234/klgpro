package com.ibb.web.controller.datadic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.StringUtils;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.controller.BaseController;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.dto.datadic.DicDto;
import com.ibb.dto.datadic.DictionaryDto;
import com.ibb.model.datadic.TblDicInfo;
import com.ibb.service.datadic.ITblDicInfoService;
import com.ibb.service.datadic.TreeNode;

import exception.IbbMgrException;

/**
 * 
 *    
 * 描述：   
 * @author：zhousiliang   
 * @Create Date：2014-10-27 上午10:42:48   
 * @version
 */
@Controller
@RequestMapping(value="/dictionary/*")
public class DicInfoController extends BaseController{
	@Autowired
	private ITblDicInfoService tblDicInfoService;
	private Logger logger=Logger.getLogger(this.getClass());
	
	
	
	/**
	 * 加载字典树
	 * <li>ID为空时，初始化一级二级节点
	 * <li>否则加载该节点的子节点
	 */
	@RequestMapping(value="tree",method=RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> tree(){
			String id = this.getRequest().getParameter("id");//点击菜单节点时，EasyUI自动传入被点击节点id
			List<TreeNode> list = this.tblDicInfoService.getTree(id);
			return list;
	}
	
	
	
	/**
	 * 添加、修改字典节点
	 * @throws Exception
	 */
	@RequestMapping(value="addUpdate",method=RequestMethod.POST)
	@ResponseBody
	public Map addUpdate(DicDto dict){
		    Map<String, Object> map = new HashMap<String, Object>();
			if(StringUtils.isNull(dict.getDicNum())){  //添加
				String result = this.tblDicInfoService.addDict(dict);
				map.put("method","add");
				map.put("success",true);
				map.put("msg", result);
			}else{
				String result = this.tblDicInfoService.updateDict(dict);
				map.put("msg", result);
				map.put("method","update");
				map.put("success",true);
			}
		return map;
	}
	
	
	
	/**
	 * 删除节点
	 */
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public Map delete(){
			String id = getRequest().getParameter("id");
			Map<String, Object> map = this.tblDicInfoService.delDict(id); 
			return map;
		
	}
	
	
	/**
	 * 根据ID加载节点信息
	 */
	@RequestMapping(value="load",method=RequestMethod.POST)
	@ResponseBody
	public DicDto load(){
		DicDto dic=null;
		try{
			String id = getRequest().getParameter("id");
			dic = this.tblDicInfoService.load(id);
			//this.writeJson(dic);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return dic;
	}
	
	
	/**
	 * 页面combobox下拉框数据加载，返回id：字典id，text：字典中文名
	 */
	@RequestMapping(value="combo",method=RequestMethod.POST)
	@ResponseBody
	public Object combo(){
		String id = getRequest().getParameter("id");
		Map resultMap=new HashMap();
		String msg="";
		try {
			if(StringUtils.isNull(id)){
				//this.writeJson("{msg:'ID不能为空。'}");
				msg="ID不能为空";
			}else{
				List<DicDto> dtoList = this.tblDicInfoService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				for(DicDto dto : dtoList ){
					Map<String,String> kv = new HashMap<String,String>();
					kv.put("id", dto.getDicNum());
					kv.put("text", dto.getDicName());
					kv.put("propertiyValue", dto.getPropertiyValue());
					comboList.add(kv);
				}
				//this.writeJson(comboList);
				return comboList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		resultMap.put("msg", msg);
		return resultMap;
	}
	
	
	/**
	 * 方法说明：页面combobox下拉框数据加载，返回id：字典id，text：字典中文名
	 * <li>加载指定的id
	 * <li>ids格式：dictionary_comboByIds.action?ids=0e010f,0e010g,0e0106,0e010d,0e0108,0e0109,0e010i,0e010h,0e0105
	 * @author Ou
	 * 2014-4-9
	 */
	@RequestMapping(value="comboByIds",method=RequestMethod.POST)
	@ResponseBody
	public Object comboByIds(){
		String idstr = getRequest().getParameter("ids");
		try {
			if(StringUtils.isNull(idstr)){
				//this.writeJson("{msg:'ID不能为空。'}");
				Map resultMap=new HashMap();
				resultMap.put("msg", "ID不能为空。");
				return resultMap;
			}else{
				
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				
				List<DicDto> dtoList = this.tblDicInfoService.findDictByIds(idstr); 
				for(DicDto dto : dtoList ){
					Map<String,String> kv = new HashMap<String,String>();
					kv.put("id", dto.getDicNum());
					kv.put("text", dto.getDicName());
					comboList.add(kv);
				}
				return comboList;
				//this.writeJson(comboList);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return new Object();
	}
	
	
	
	/**
	 * 页面combobox下拉框数据加载，返回id：字典id，text：字典中文名<br>
	 * 添加一个带"==请选择=="项
	 * @author woohaha
	 */
	@RequestMapping(value="combov",method=RequestMethod.POST)
	@ResponseBody
	public Object combov(){
		String id = getRequest().getParameter("id");
		String choose=getRequest().getParameter("choose");
		try {
			if(StringUtils.isNull(id)){
				//this.writeJson("{msg:'ID不能为空。'}");
				Map resultMap=new HashMap();
				resultMap.put("msg", "ID不能为空。");
				return resultMap;
			}else{
				List<DicDto> dtoList = this.tblDicInfoService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				if(!StringUtils.isNullStr(choose)){
					Map chooseMap=StringUtils.chooseMap(choose);
					if(chooseMap!=null)
						comboList.add(chooseMap);
				}
				for(DicDto dto : dtoList ){
					Map<String,String> kv = new HashMap<String,String>();
					kv.put("id", dto.getDicNum());
					kv.put("text", dto.getDicName());
					comboList.add(kv);
				}
				return comboList;
				//this.writeJson(comboList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return new Object();
	}
	
	
	/**
	 * 页面comboboxTree(递归)下拉框数据加载，返回id：字典id，text：字典中文名
	 */
	@RequestMapping(value="comboTree",method=RequestMethod.POST)
	@ResponseBody
	public Object comboTree(){
		String id = getRequest().getParameter("id");
		try {
			if(StringUtils.isNull(id)){
				//this.writeJson("{msg:'ID不能为空。'}");
				Map resultMap=new HashMap();
				resultMap.put("msg", "ID不能为空。");
				return resultMap;
			}else{
					//this.writeJson(this.dictionaryService.getRecursionDict(id));
				return this.tblDicInfoService.getRecursionDict(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return new Object();
	}
	
	
	//////////------------------------------------------------------------------------------------
	
	
	/**
	 * 返回到数据字典管理页面
	 */
	@RequestMapping(value="toDictPage")
	public String toDictPage(){
		return "datadic/dict";
	}
	

	

}
