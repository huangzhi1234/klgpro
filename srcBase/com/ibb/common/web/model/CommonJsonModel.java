package com.ibb.common.web.model;


/**
 * 通用JSON返回格式
 * @author kin wong
 */
public class CommonJsonModel {
	private boolean result;	//操作结果
	private Object msg;		//返回消息
	private Object data;	//附加数据
	
	/**
	 * 默认构造函数(默认返回)
	 */
	public CommonJsonModel(){
		result = true;
		msg = "成功";
	}
	
	public CommonJsonModel(boolean result, Object msg){
		this.result = result;
		this.msg = msg;
	}
	
	public CommonJsonModel(boolean result, Object msg, Object data){
		this.result = result;
		this.msg = msg;
		this.data = data;
	}
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public Object getMsg() {
		return msg;
	}
	public void setMsg(Object msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
