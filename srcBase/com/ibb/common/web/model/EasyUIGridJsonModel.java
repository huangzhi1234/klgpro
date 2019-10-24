package com.ibb.common.web.model;

import java.util.List;

import com.ibb.common.util.pagination.Page;


/**
 * EasyUI 数据列表返回格式
 * @author kin wong
 */
public class EasyUIGridJsonModel<E> {
	private int total;	//总记录数
	private List<E> rows;		//该页数据
	
	
	public EasyUIGridJsonModel(Page<E> page){
		this.total = page.getContext().getTotal();
		this.rows = page.getItems();
	}
	
	public EasyUIGridJsonModel(int total, List<E> rows){
		this.total = total;
		this.rows = rows;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<E> getRows() {
		return rows;
	}
	public void setRows(List<E> rows) {
		this.rows = rows;
	}
}
