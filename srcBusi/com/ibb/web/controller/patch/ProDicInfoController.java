package com.ibb.web.controller.patch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.StringUtils;
import com.ibb.common.web.controller.BaseController;
import com.ibb.model.patch.PatchInfo;
import com.ibb.model.patch.ProDicInfo;
import com.ibb.model.stDoc.StdocInfo;
import com.ibb.service.datadic.TreeNode;
import com.ibb.service.patch.IPatchInfoService;
import com.ibb.service.patch.ITblProDicInfoService;
import com.ibb.service.patch.IUploadCheckService;

@Controller
@RequestMapping(value="/proDicInfo/*")
public class ProDicInfoController extends BaseController{
	
	@Autowired
	private ITblProDicInfoService tblProDicInfoService;
	@Autowired
	private IPatchInfoService patchInfoService;
	@Autowired
	private IUploadCheckService uploadCheckService;
	private Logger logger=Logger.getLogger(this.getClass());
	
	
	
	/**
	 * 加载研发字典树:
	 * 父节点：openid为空
	 * <li>ID为空时，初始化一级二级节点
	 * <li>否则加载该节点的子节点
	 */
	@RequestMapping(value="tree",method=RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> tree(){
			String id = this.getRequest().getParameter("id");//点击菜单节点时，EasyUI自动传入被点击节点id
			List<TreeNode> list = this.tblProDicInfoService.getTree(id);
			return list;
	}
	
	/**
	 * 加载实施字典树
	 * 父节点：openid为00
	 * <li>ID为空时，初始化一级二级节点
	 * <li>否则加载该节点的子节点
	 */
	@RequestMapping(value="stree",method=RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> stree(){
			String id = this.getRequest().getParameter("id");//点击菜单节点时，EasyUI自动传入被点击节点id
			List<TreeNode> list = this.tblProDicInfoService.getSTree(id);
			return list;
	}
	
	/**
	 * 添加、修改字典节点
	 * @throws Exception
	 */
	@RequestMapping(value="addUpdate",method=RequestMethod.POST)
	@ResponseBody
	public Map addUpdate(ProDicInfo dict){
		    Map<String, Object> map = new HashMap<String, Object>();
			if(StringUtils.isNull(dict.getDicNum())){  //添加
				String result = this.tblProDicInfoService.addDict(dict);
				map.put("method","add");
				map.put("success",true);
				map.put("msg", result);
			}else{
				String result = this.tblProDicInfoService.updateDict(dict);
				map.put("msg", result);
				map.put("method","update");
				map.put("success",true);
			}
		return map;
	}
	
	/**
	 * 添加、修改字典节点
	 * @throws Exception
	 */
	@RequestMapping(value="addUpdate2",method=RequestMethod.POST)
	@ResponseBody
	public Map addUpdate2(ProDicInfo dict){
		    Map<String, Object> map = new HashMap<String, Object>();
			if(StringUtils.isNull(dict.getDicNum())){  //添加
				String result = this.tblProDicInfoService.addDict2(dict);
				map.put("method","add");
				map.put("success",true);
				map.put("msg", result);
			}else{
				String result = this.tblProDicInfoService.updateDict(dict);
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
			ConditionQuery conditionQuery = new ConditionQuery();
			Integer i=0;
			Integer j=0;
			if (StringUtils.isNotBlank(id)) {
				conditionQuery.add(Restrictions.eq("dicNum", id));
				List<PatchInfo> lists = patchInfoService.queryListByCondition(conditionQuery);//查询编号所对应的项目文档信息数据
				if (lists.size() > 0) {//如果存在项目文档信息，删除失败
					for (PatchInfo patchInfo : lists) {
						if(patchInfo.getState()==1){//说明此节点下有文件
							//1.拿到项目的id，查询改项目是否处于审批状态
							Boolean result=uploadCheckService.isShenHe(patchInfo.getPatId());
							if(result){
								i++;
								break;
							}
							j++;
						}
					}
					
				}
			} 
			
			if(j>0){//节点下有文件
				return null;
			}else if(i>0){//节点下有正在审批的文件
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("isshenhe", true);
				map2.put("msg", "该节点有文件正在审批中，删除失败！");
				return map2;
			}else{
				Map<String, Object> map = this.tblProDicInfoService.delDict(id); 
				return map;
			}
			
			
			
			
		
	}
	
	
	/**
	 * 根据ID加载节点信息
	 */
	@RequestMapping(value="load",method=RequestMethod.POST)
	@ResponseBody
	public ProDicInfo load(){
		ProDicInfo dic=null;
		try{
			String id = getRequest().getParameter("id");
			dic = this.tblProDicInfoService.load(id);
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
				List<ProDicInfo> dtoList = this.tblProDicInfoService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				for(ProDicInfo dto : dtoList ){
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
				
				List<ProDicInfo> dtoList = this.tblProDicInfoService.findDictByIds(idstr); 
				for(ProDicInfo dto : dtoList ){
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
				List<ProDicInfo> dtoList = this.tblProDicInfoService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				if(!StringUtils.isNullStr(choose)){
					Map chooseMap=StringUtils.chooseMap(choose);
					if(chooseMap!=null)
						comboList.add(chooseMap);
				}
				for(ProDicInfo dto : dtoList ){
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
				return this.tblProDicInfoService.getRecursionDict(id);
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
	@RequestMapping(value="toProDicInfo")
	public String toDictPage(){
		return "patch/patchInfo";
	}

}
