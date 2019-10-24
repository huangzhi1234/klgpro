package com.ibb.model.stDoc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DocDicInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "doc_dic_info")
public class DocDicInfo implements java.io.Serializable {

	// Fields

	private Integer dicId;//文档字典id
	private String dicNum;//字典编号
	private String dicName;//字典名称
	private String level;//级别
	private String operId;//父字典
	private String dicType;//字典类型
	private String propertiyValue;//属性

	// Constructors

	/** default constructor */
	public DocDicInfo() {
	}

	/** minimal constructor */
	public DocDicInfo(String dicNum, String dicName, String level, String operId) {
		this.dicNum = dicNum;
		this.dicName = dicName;
		this.level = level;
		this.operId = operId;
	}

	/** full constructor */
	public DocDicInfo(String dicNum, String dicName, String level,
			String operId, String dicType, String propertiyValue) {
		this.dicNum = dicNum;
		this.dicName = dicName;
		this.level = level;
		this.operId = operId;
		this.dicType = dicType;
		this.propertiyValue = propertiyValue;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "dicId", unique = true, nullable = false)
	public Integer getDicId() {
		return this.dicId;
	}

	public void setDicId(Integer dicId) {
		this.dicId = dicId;
	}

	@Column(name = "dicNum", nullable = false, length = 10)
	public String getDicNum() {
		return this.dicNum;
	}

	public void setDicNum(String dicNum) {
		this.dicNum = dicNum;
	}

	@Column(name = "dicName", nullable = false, length = 200)
	public String getDicName() {
		return this.dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	@Column(name = "level", nullable = false, length = 2)
	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(name = "operId", nullable = false, length = 10)
	public String getOperId() {
		return this.operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	@Column(name = "dicType", length = 10)
	public String getDicType() {
		return this.dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	@Column(name = "propertiyValue", length = 500)
	public String getPropertiyValue() {
		return this.propertiyValue;
	}

	public void setPropertiyValue(String propertiyValue) {
		this.propertiyValue = propertiyValue;
	}

}