package com.ibb.model.patch;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pro_dic_info")
public class TblProDicInfo implements java.io.Serializable {
	
	private Integer dicId;
	private String dicNum;
	private String dicName;
	private String level;
	private String operId;
	private String dicType;
	private String propertiyValue;

	public TblProDicInfo() {
		super();
	}

	public TblProDicInfo(Integer dicId, String dicNum, String dicName, String level, String operId, String dicType,
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

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "dicId", unique = true, nullable = false)
	public Integer getDicId() {
		return dicId;
	}

	public void setDicId(Integer dicId) {
		this.dicId = dicId;
	}

	@Column(name = "dicNum",  length = 10)
	public String getDicNum() {
		return dicNum;
	}

	public void setDicNum(String dicNum) {
		this.dicNum = dicNum;
	}

	@Column(name = "dicName",  length = 200)
	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	@Column(name = "level",  length = 2)
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(name = "operId",  length = 10)
	public String getOperId() {
		return operId;
	}

	public void setOperId(String operId) {
		this.operId = operId;
	}

	@Column(name = "dicType",  length = 10)
	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	@Column(name = "propertiyValue",  length = 500)
	public String getPropertiyValue() {
		return propertiyValue;
	}

	public void setPropertiyValue(String propertiyValue) {
		this.propertiyValue = propertiyValue;
	}

}
