package com.ibb.model.depart;

import com.ibb.dto.BaseDto;


public class DicInfo extends BaseDto{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer dicId;//文档字典id
	private String dicNum;//字典编号
	private String dicName;//字典名称
	private String level;//级别
	private String operId;//父字典
	private String dicType;//字典类型
	private String propertiyValue;//属性
	
	private String q;

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

	public String getPropertiyValue() {
		return propertiyValue;
	}

	public void setPropertiyValue(String propertiyValue) {
		this.propertiyValue = propertiyValue;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public DicInfo() {
		super();
	}

	public DicInfo(Integer dicId, String dicNum, String dicName, String level,
			String operId, String dicType, String propertiyValue, String q) {
		super();
		this.dicId = dicId;
		this.dicNum = dicNum;
		this.dicName = dicName;
		this.level = level;
		this.operId = operId;
		this.dicType = dicType;
		this.propertiyValue = propertiyValue;
		this.q = q;
	}
	
	
	/**
	 * 将页面可以修改的属性赋给实体类
	 * 
	 * @param t
	 */
	public void copyEditProperties(DepartDicInfo t) {
		t.setDicName(this.getDicName());
		t.setPropertiyValue(this.getPropertiyValue());
	}

}
