package com.ibb.dto;

import java.io.Serializable;

public class BaseDto implements Serializable{
	private int page;//当前页
	private int rows;//每页记录数
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
}
