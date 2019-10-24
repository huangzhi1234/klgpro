package com.ibb.poi;

public class WarnMessage {
	public String rowname;
	public String cellName;
	public String errorMsg;
	public String comment;
	public WarnMessage(String rowname, String cellName, String errorMsg,
			String comment) {
		super();
		this.rowname = rowname;
		this.cellName = cellName;
		this.errorMsg = errorMsg;
		this.comment = comment;
	}
	public String getRowname() {
		return rowname;
	}
	public void setRowname(String rowname) {
		this.rowname = rowname;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	

}
