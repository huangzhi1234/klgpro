package com.ibb.web.controller.depart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.util.StringUtils;
import com.ibb.common.web.controller.BaseController;
import com.ibb.model.depart.DicInfo;
import com.ibb.service.datadic.TreeNode;
import com.ibb.service.depart.IDepartDicService;
@Controller
public class DepartDicController extends BaseController implements ServletContextAware{
	@Autowired
	private IDepartDicService departDicService;
	private Logger logger=Logger.getLogger(this.getClass());
	

	
	@SuppressWarnings("unused")
	private ServletContext servletContext;
	@Autowired
    @Qualifier("hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	@RequestMapping(value = "/depart")
	public String toDepart() {
		return "depart/departInfo";
	}
	
	
	
	
	/**
	 * 加载字典树
	 * <li>ID为空时，初始化一级二级节点
	 * <li>否则加载该节点的子节点
	 */
	@RequestMapping(value="/depart/tree",method=RequestMethod.POST)
	@ResponseBody
	public List<TreeNode> tree(){
			String id = this.getRequest().getParameter("id");//点击菜单节点时，EasyUI自动传入被点击节点id
			List<TreeNode> list = this.departDicService.getTree(id);
			return list;
	}
	
	/**
	 * 添加、修改字典节点
	 * @throws Exception
	 */
	@RequestMapping(value="/depart/addUpdate",method=RequestMethod.POST)
	@ResponseBody
	public Map addUpdate(DicInfo dict){
	    Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNull(dict.getDicNum())){  //添加
			String result = this.departDicService.addDict(dict);
			map.put("method","add");
			map.put("success",true);
			map.put("msg", result);
		}else{
			String result = this.departDicService.updateDict(dict);
			map.put("msg", result);
			map.put("method","update");
			map.put("success",true);
		}
		return map;
	}
	
	/**
	 * 删除节点
	 */
	@RequestMapping(value="/depart/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map delete(){
		String id = getRequest().getParameter("id");//获取字典编号
		Map<String, Object> map = this.departDicService.delDict(id); //删除节点
		return map;
	}
	
	/**
	 * 根据ID加载节点信息
	 */
	@RequestMapping(value="/depart/load",method=RequestMethod.POST)
	@ResponseBody
	public DicInfo load(){
		DicInfo dic=null;
		try{
			String id = getRequest().getParameter("id");
			dic = this.departDicService.load(id);
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
	@RequestMapping(value="/depart/combo",method=RequestMethod.POST)
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
				List<DicInfo> dtoList = this.departDicService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				for(DicInfo dto : dtoList ){
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
	@RequestMapping(value="/depart/comboByIds",method=RequestMethod.POST)
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
				List<DicInfo> dtoList = this.departDicService.findDictByIds(idstr); 
				for(DicInfo dto : dtoList ){
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
	@RequestMapping(value="/depart/combov",method=RequestMethod.POST)
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
				List<DicInfo> dtoList = this.departDicService.findDictByPid(id); 
				List<Map<String,String>> comboList = new ArrayList<Map<String,String>>(); 
				if(!StringUtils.isNullStr(choose)){
					Map chooseMap=StringUtils.chooseMap(choose);
					if(chooseMap!=null)
						comboList.add(chooseMap);
				}
				for(DicInfo dto : dtoList ){
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
	@RequestMapping(value="/depart/comboTree",method=RequestMethod.POST)
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
				return this.departDicService.getRecursionDict(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return new Object();
	}

	
	
	
}
