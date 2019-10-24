package com.ibb.sys.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.sys.model.DictItemBean;
import com.ibb.sys.service.IDictItemService;


/**
 * 字典项目项目管理控制
 * 
 * @author kin wong
 */
@Controller
public class DictItemController {
	private final String TOP = "1";
	private final String UP = "2";
	private final String DOWN = "3";
	private final String LOW = "4";
	
	@Autowired
    private IDictItemService dictItemService;
	
	/**
	 * 初始化页面(异步取数时需要)
	 * @return 查询列表初始化页面
	 */
	@RequestMapping(value = "/dictItem/list/{dictCode}")
	public String initPage(@PathVariable String dictCode,HttpServletRequest request){
		request.setAttribute("dictCode", dictCode.trim());
		return "sys/dictItem";
	}
	
	/**
	 * 查询数据列表
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dictItem/list.json",method=RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<DictItemBean> queryList(DictItemBean dictItem,HttpServletRequest request){
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1);//第几页
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		OrderBy order = new OrderBy();
		ConditionQuery query = new ConditionQuery();
		order.add(Order.asc("itemOrder"));
		if(StringUtils.isNotEmpty(dictItem.getDictCode())){
			query.add(Restrictions.eq("dictCode", dictItem.getDictCode().trim()));
		}else{
			return null;
		}
		
		Page<DictItemBean> page = dictItemService.queryListByCondition(query,order, pn, pageSize);
		
		return new EasyUIGridJsonModel(page);
	}
	
	/**
	 * 增加一条记录
	 * @param dictItem 字典项目对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictItem/add.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel add(DictItemBean dictItem){
		if(dictItem != null){
			ConditionQuery query = new ConditionQuery();
			query.add(Restrictions.eq("itemCode", dictItem.getItemCode().trim()));
			List<DictItemBean> dictItemList = dictItemService.queryListByCondition(query);
			if(dictItemList != null && dictItemList.size() > 0 ){
				return	new CommonJsonModel(false,"已存在该字典项目！");
			}else{
				dictItemService.save(dictItem);
			}
		}else{
			return new CommonJsonModel(false,"字典项目对象为空！");
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 更新一条记录
	 * @param dictItem 字典项目对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictItem/update.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(DictItemBean dictItem){
		if(dictItem != null){
			dictItemService.update(dictItem);
		}else{
			return new CommonJsonModel(false,"字典项目对象为空！");
		}
		
		return new CommonJsonModel();
	}
	
	/**
	 * 删除多条(一条)记录
	 * @param itemIdArr 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictItem/delete.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String itemIdArr,HttpServletRequest request){
		String[] itemIds = itemIdArr.split("#");
		for(String itemId : itemIds){
			if(StringUtils.isNotEmpty(itemId)){
				dictItemService.delete(Integer.parseInt(itemId));
			}
		}
		return new CommonJsonModel();
	}
	
	/**
	 * 置顶记录
	 * @param itemId 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictItem/top.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel top(String itemId,HttpServletRequest request){
		dictItemService.updateOrder(Integer.parseInt(itemId), TOP);
		return new CommonJsonModel();
	}
	
	/**
	 * 向上记录
	 * @param itemId 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictItem/up.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel up(String itemId,HttpServletRequest request){
		dictItemService.updateOrder(Integer.parseInt(itemId), UP);
		return new CommonJsonModel();
	}
	
	/**
	 * 置顶记录
	 * @param itemId 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictItem/down.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel down(String itemId,HttpServletRequest request){
		dictItemService.updateOrder(Integer.parseInt(itemId), DOWN);
		return new CommonJsonModel();
	}
	
	/**
	 * 置顶记录
	 * @param itemId 主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/dictItem/low.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel low(String itemId,HttpServletRequest request){
		dictItemService.updateOrder(Integer.parseInt(itemId), LOW);
		return new CommonJsonModel();
	}
}
