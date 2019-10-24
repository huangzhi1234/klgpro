package com.ibb.common.web.model;


/**
 * 通用JSON返回格式
 * @author kin wong
 */
public class ReturnApiJsonModel {
	private String status;	//状态
	private String error;		//错误
	private String error_description;	//错误描述
	private Object data;	//附加数据
	
	/**
	 * 默认构造函数(默认返回)
	 */
	public ReturnApiJsonModel(){
		status = "success";
	}
	
	public ReturnApiJsonModel(String error, String error_description){
		this.error = error;
		this.error_description = error_description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
	
}
