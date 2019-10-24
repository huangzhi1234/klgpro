package com.ibb.service.datadic;

import java.util.HashMap;
import java.util.Map;

/**
 * EasyUI分页查询工具实体
 * @author Ou
 */
public class PageUtils {
	/**
	 * EasyUI使用的变量：
	 * 	提交：
	 * 		page:当前页
	 * 		rows:每页记录数
	 * 	响应：
	 * 		total:总记录数
	 * 		rows：记录集合
	 */
	private int page;   	//当前页
	private int limit;  	//每页记录数      但EasyUI每页记录数使用的变量是rows
	private int total;  	//总记录数
	private Object rows;	//数据集合
	private String sort;	// 指定排序字段
	private String dir;		// 指定排序方向
	private String hql;	
	private Map params = new HashMap();
	
	public PageUtils() {
		super();
	}

	public PageUtils(int total, Object rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	
	public PageUtils(int page, int limit, int total, Object rows, String sort,
			String dir) {
		super();
		this.page = page;
		this.limit = limit;
		this.total = total;
		this.rows = rows;
		this.sort = sort;
		this.dir = dir;
	}
	/**
	 * @param page 当前页	
	 * @param limit	每页记录数
	 */
	public PageUtils(int page, int limit) {
		super();
		this.page = page;
		this.limit = limit;
	}
	/**
	 * @param page 当前页	
	 * @param limit	每页记录数
	 * @param sort 排序字段名
	 * @param dir  排序方向
	 */
	public PageUtils(int page, int limit, String sort, String dir) {
		super();
		this.page = page;
		this.limit = limit;
		this.sort = sort;
		this.dir = dir;
	}

	/**
	 * @param page 当前页	
	 * @param limit	每页记录数
	 * @param sort 排序字段名
	 * @param dir  排序方向
	 * @param params 条件
	 */
	public PageUtils(int page, int limit, String sort, String dir,Map params) {
		super();
		this.page = page;
		this.limit = limit;
		this.sort = sort;
		this.dir = dir;
		this.params = params;
	}

	/**
	 * @param page 当前页	
	 * @param limit	每页记录数
	 * @param params 条件
	 */
	public PageUtils(int page, int limit,Map params) {
		super();
		this.page = page;
		this.limit = limit;
		this.params = params;
	}
	/**
	 * @param page 当前页	
	 * @param limit	每页记录数
	 * @param hql 
	 * @param params 条件
	 */
	public PageUtils(int page, int limit,String hql,Map params) {
		super();
		this.page = page;
		this.limit = limit;
		this.hql = hql;
		this.params = params;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}



	public String getHql() {
		return hql;
	}



	public void setHql(String hql) {
		this.hql = hql;
	}
	
	
}
