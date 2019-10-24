package com.ibb.model.datadic;

/**
 * TblDicInfo entity. @author MyEclipse Persistence Tools
 */

public class TblDicInfo  implements java.io.Serializable {

	// Fields

	private Integer dicId;
	private String dicNum;
	private String dicName;
	private String level;
	private String operId;
	private String dicType;
    private String propertiyValue;

	// Constructors

	/** default constructor */
	public TblDicInfo() {
	}

	/** minimal constructor */
	public TblDicInfo(String dicNum, String dicName, String level, String operId) {
		this.dicNum = dicNum;
		this.dicName = dicName;
		this.level = level;
		this.operId = operId;
	}

	/** full constructor */
	public TblDicInfo(String dicNum, String dicName, String level,
			String operId, String dicType) {
		this.dicNum = dicNum;
		this.dicName = dicName;
		this.level = level;
		this.operId = operId;
		this.dicType = dicType;
	}

	// Property accessors

	public Integer getDicId() {
		return this.dicId;
	}

	public void setDicId(Integer dicId) {
		this.dicId = dicId;
	}

	public String getDicNum() {
		return this.dicNum;
	}

	public void setDicNum(String dicNum) {
		this.dicNum = dicNum;
	}

	public String getDicName() {
		return this.dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getOperId() {
		return this.operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	public String getDicType() {
		return this.dicType;
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

}