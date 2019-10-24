package com.ibb.sys.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.sys.model.DictDefBean;
import com.ibb.sys.service.IDictDefService;


/**
 * 数据字典定义管理控制
 * 
 * @author kin wong
 */
@Controller
public class DictDefController {
	@Autowired
    private IDictDefService dictDefService;
	
	/**
	 * 初始化页面(异步取数时需要)
	 * @return 查询列表初始化页面
	 */
	@RequestMapping(value = "/dictDef")
	public String initPage(){
		return "sys/dictDef";
	}
	
	/**
	 * 查询数据列表
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dictDef/list.json",method=RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<DictDefBean> queryList(DictDefBean dictDef,HttpServletRequest request){
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1);//第几页
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		if(StringUtils.isNotEmpty(dictDef.getDictCode())){
			query.add(Restrictions.eq("dictCode", dictDef.getDictCode().trim()));
		}
		if(StringUtils.isNotEmpty(dictDef.getDictName())){
			query.add(Restrictions.like("dictName", "%" + dictDef.getDictName().trim() + "%"));
		}
		
		Page<DictDefBean> page = dictDefService.queryListByCondition(query, pn, pageSize);
		
		return new EasyUIGridJsonModel(page);
	}
	
	/**
	 * 增加一条记录
	 * @param dictDef 数据字典对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictDef/add.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel add(DictDefBean dictDef){
		if(dictDef != null){
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("dictCode", dictDef.getDictCode().trim()));
			List<DictDefBean> dictDefList = dictDefService.queryListByCondition(query);
			if(dictDefList != null && dictDefList.size() > 0 ){
				return	new CommonJsonModel(false,"已存在该数据字典！");
			}else{
				dictDefService.save(dictDef);
			}
		}else{
			return new CommonJsonModel(false,"数据字典对象为空！");
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 更新一条记录
	 * @param dictDef 数据字典对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictDef/update.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(DictDefBean dictDef){
		if(dictDef != null){
			dictDefService.update(dictDef);
		}else{
			return new CommonJsonModel(false,"数据字典对象为空！");
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 删除多条(一条)记录
	 * @param dictDefIdArr 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictDef/delete.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String dictIdArr,HttpServletRequest request){
		String[] dictIds = dictIdArr.split("#");
		for(String dictId : dictIds){
			if(StringUtils.isNotEmpty(dictId)){
				dictDefService.deleteCasecade(Integer.parseInt(dictId));
			}
		}
		return new CommonJsonModel();
	}
}
