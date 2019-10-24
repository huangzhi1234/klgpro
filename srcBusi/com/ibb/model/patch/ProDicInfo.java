package com.ibb.model.patch;

import com.ibb.dto.BaseDto;
import com.ibb.model.datadic.TblDicInfo;

public class ProDicInfo extends BaseDto {

	private Integer dicId;
	private String dicNum;
	private String dicName;
	private String level;
	private String operId;
	private String dicType;
	private String propertiyValue;

	private String q;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public ProDicInfo() {
		super();
	}

	public ProDicInfo(Integer dicId, String dicNum, String dicName, String level, String operId, String dicType,
			String propertiyValue) {
		super();
		this.dicId = dicId;
		this.dicNum = dicNum;
		this.dicName = dicName;
		this.level = level;
		this.operId = operId;
		this.dicType = dicType;
		this.propertiyValue = propertiyValue;
	}

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

	/**
	 * 将页面可以修改的属性赋给实体类
	 * 
	 * @param t
	 */
	public void copyEditProperties(TblProDicInfo t) {
		t.setDicName(this.getDicName());
		t.setPropertiyValue(this.getPropertiyValue());
	}

}
