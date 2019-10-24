package com.ibb.web.controller.datadic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.common.util.StringUtils;
import com.ibb.common.web.controller.BaseController;
import com.ibb.dto.datadic.DictionaryDto;
import com.ibb.service.datadic.IDictionaryService;
import com.ibb.service.datadic.PageUtils;
import com.ibb.service.datadic.TreeNode;
import com.sun.org.apache.bcel.internal.generic.NEW;


/**
 * 
 *    
 * 描述：   数据字典
 * 创建人：zhousiliang   
 * 创建时间：2014-10-8 上午10:17:19   
 * @version
 */
@Controller
@RequestMapping(value="/dic/*")
public class DictionaryController extends BaseController {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DictionaryController.class);
	
	private IDictionaryService dictionaryService;
	@Autowired
	public void setDictionaryService(IDictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	/////////////////////////////////////

	/**
	 * 返回到数据字典管理页面
	 */
	@RequestMapping(value="toDictPage")
	public String toDictPage(){
		return "datadic/dict";
	}
	/**
	 * 加载字典树
	 * <li>ID为空时，初始化一级二级节点
	 * <li>否则加载该节点的子节点
	 */
	@RequestMapping(value="tree",method=RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> tree(){
			String id = this.getRequest().getParameter("id");//点击菜单节点时，EasyUI自动传入被点击节点id
			List<TreeNode> list = this.dictionaryService.getTree(id);
			return list;
	}
	/**
	 * 添加、修改字典节点
	 * @throws Exception
	 */
	@RequestMapping(value="addUpdate",method=RequestMethod.POST)
	@ResponseBody
	public Map addUpdate(DictionaryDto dict){
		    Map<String, Object> map = new HashMap<String, Object>();
			if(StringUtils.isNull(dict.getSystemId())){  //添加
				String result = this.dictionaryService.addDict(dict);
				map.put("method","add");
				map.put("success",true);
				map.put("msg", result);
			}else{
				String result = this.dictionaryService.updateDict(dict);
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
			Map<String, Object> map = this.dictionaryService.delDict(id); 
			return map;
		
	}
	
	/**
	 * 根据ID加载节点信息
	 */
	@RequestMapping(value="load",method=RequestMethod.POST)
	@ResponseBody
	public DictionaryDto load(){
		DictionaryDto dic=null;
		try{
			String id = getRequest().getParameter("id");
			dic = this.dictionaryService.load(id);
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
				List<DictionaryDto> dtoList = this.dictionaryService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				for(DictionaryDto dto : dtoList ){
					Map<String,String> kv = new HashMap<String,String>();
					kv.put("id", dto.getSystemId());
					kv.put("text", dto.getChineseName());
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
				
				List<DictionaryDto> dtoList = this.dictionaryService.findDictByIds(idstr); 
				for(DictionaryDto dto : dtoList ){
					Map<String,String> kv = new HashMap<String,String>();
					kv.put("id", dto.getSystemId());
					kv.put("text", dto.getChineseName());
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
				List<DictionaryDto> dtoList = this.dictionaryService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				if(!StringUtils.isNullStr(choose)){
					Map chooseMap=StringUtils.chooseMap(choose);
					if(chooseMap!=null)
						comboList.add(chooseMap);
				}
				for(DictionaryDto dto : dtoList ){
					Map<String,String> kv = new HashMap<String,String>();
					kv.put("id", dto.getSystemId());
					kv.put("text", dto.getChineseName());
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
	 * 页面combogric下拉框数据加载，返回id：字典id，text：字典中文名<br>
	 * 带参数模糊查询
	 */
	@RequestMapping(value="combovByParam",method=RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Object combovByParam(DictionaryDto dict){
		try {
			if(StringUtils.isNull(dict.getSystemId())){
				//this.writeJson("{msg:'ID不能为空。'}");
				Map resultMap=new HashMap();
				resultMap.put("msg", "ID不能为空。");
				return resultMap;
			}else{
				if (StringUtils.isNotNull(dict.getQ())) {
					this.dictionaryService.clear("09");
				}
				PageUtils pageUtils  = this.dictionaryService.findAllChilListDictByPid(dict);
				//this.writeJson(pageUtils);
				return pageUtils;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		
		return new Object();
	}
	
	
	/**
	 * 页面combobox下拉框数据加载，返回id：字典id，text：字典中文名<br>
	 * 添加一个带"==请选择=="项
	 * @author woohaha
	 */
	@RequestMapping(value="combosub",method=RequestMethod.POST)
	@ResponseBody
	public Object combosub(){
		String id = getRequest().getParameter("id");
		String choose=getRequest().getParameter("choose");
		String subid=getRequest().getParameter("subid");
		String oper=getRequest().getParameter("oper");
		try {
			if(StringUtils.isNull(id)){
				//this.writeJson("{msg:'ID不能为空。'}");
				Map resultMap=new HashMap();
				resultMap.put("msg", "ID不能为空。");
				return resultMap;
			}else{
				List<DictionaryDto> dtoList = this.dictionaryService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				if(!StringUtils.isNullStr(choose)){
					Map chooseMap=StringUtils.chooseMap(choose);
					if(chooseMap!=null)
						comboList.add(chooseMap);
				}
				String[] sub=null;
				if(StringUtils.isNotNull(subid) && StringUtils.isNotNull(oper) && "reqadd".equals(oper)){
					StringBuffer sb=new StringBuffer(subid);
					sb.delete(0, 1).delete(sb.length()-1, sb.length());
					sub=StringUtils.strToArrTo(sb.toString(), ",");
				}
				for(DictionaryDto dto : dtoList ){
					Map<String,String> kv = new HashMap<String,String>();
					if(sub!=null){
						for(String ids:sub){
							if(ids.equals(dto.getSystemId())){
								kv.put("id", dto.getSystemId());
								kv.put("text", dto.getChineseName());
								comboList.add(kv);
								break;
							}
						}
					}else{
						kv.put("id", dto.getSystemId());
						kv.put("text", dto.getChineseName());
						comboList.add(kv);
					}
				}
				//this.writeJson(comboList);
				return comboList;
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
				return this.dictionaryService.getRecursionDict(id);
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
	@RequestMapping(value="comboTreeByFlag",method=RequestMethod.POST)
	@ResponseBody
	public Object comboTreeByFlag(){
		String id = getRequest().getParameter("id");
		String flag =getRequest().getParameter("flag");
		try {
			if(StringUtils.isNull(id)){
				//this.writeJson("{msg:'ID不能为空。'}");
				Map resultMap=new HashMap();
				resultMap.put("msg", "ID不能为空。");
				return resultMap;
			}else{
					//this.writeJson(this.dictionaryService.getComBoBoxTreeByFlag(id,flag));
				return this.dictionaryService.getComBoBoxTreeByFlag(id,flag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		
		return new Object();
	}
	
	
}
