package com.ibb.dto.datadic;

import com.ibb.dto.BaseDto;
import com.ibb.model.datadic.TblDicInfo;

public class DicDto extends BaseDto {

	private Integer dicId;
	private String dicNum;//节点编码
	private String dicName;//中文名称
	private String level;//等级
	private String operId;//父节点
	private String dicType;
	private String propertiyValue;//属性值
	private String q;//combogrid模糊查询的时候传的参数名称
	
	public Integer getDicId() {
		return dicId;
	}
	public void setDicId(Integer dicId) {
		this.dicId = dicId;
	}
	public String getDicNum() {
		return dicNum;
	}
	public void setDicNum(String dicNum) {
		this.dicNum = dicNum;
	}
	public String getDicName() {
		return dicName;
	}
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getOperId() {
		return operId;
	}
	public void setOperId(String operId) {
		this.operId = operId;
	}
	public String getDicType() {
		return dicType;
	}
	public void setDicType(String dicType) {
		this.dicType = dicType;
	}
	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}
	/**
	 * 将页面可以修改的属性赋给实体类
	 * @param t
	 */
	public void copyEditProperties(TblDicInfo t){
		t.setDicName(this.getDicName());
		t.setPropertiyValue(this.getPropertiyValue());
	}
	public String getPropertiyValue() {
		return propertiyValue;
	}
	public void setPropertiyValue(String propertiyValue) {
		this.propertiyValue = propertiyValue;
	}
	
}
