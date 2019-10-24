package com.ibb.model.datadic;

import java.util.Set;

import com.ibb.common.model.AbstractModel;

/**
 * 字典实体类
 */
public class TBaseDataDictionary extends AbstractModel {

	
	private String systemId;        //字典ID
	private String upSystemId;      //父节点ID
	private String classNo;			//描述当前代码集所在的层次
	private String standardNo;      //国标编码
	private String standardChName;	//国标中文名称
	private String standardEnName;	//国标英文名称
	private String chineseName;		//字典中文名
	private String englishName;		//字典英文名
	private Integer sortNo;			//排序	
	private String propertiyValue;	//字典属性值
	private Integer statisticFlag;		//是否界面可见: 1 是，0 否（内部代码使用）
	private Integer controlFlag;		//(备用)
	private Integer typeFlag;			//(备用)
	private String propertiy1;		//扩展属性一
	private String propertiy1Explain;//扩展属性一描述
	private String propertiy2;
	private String propertiy2Explain;
	private String propertiy3;
	private String propertiy3Explain;

	// Constructors

	/** default constructor */
	public TBaseDataDictionary() {
	}

	// Property accessors
	
	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUpSystemId() {
		return this.upSystemId;
	}

	public void setUpSystemId(String upSystemId) {
		this.upSystemId = upSystemId;
	}

	/** 描述当前代码集所在的层次 */
	public String getClassNo() {
		return this.classNo;
	}
	/** 描述当前代码集所在的层次 */
	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getStandardNo() {
		return this.standardNo;
	}

	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}

	public String getStandardChName() {
		return this.standardChName;
	}

	public void setStandardChName(String standardChName) {
		this.standardChName = standardChName;
	}

	public String getStandardEnName() {
		return this.standardEnName;
	}

	public void setStandardEnName(String standardEnName) {
		this.standardEnName = standardEnName;
	}

	public String getChineseName() {
		return this.chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return this.englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getTypeFlag() {
		return this.typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	public String getPropertiyValue() {
		return this.propertiyValue;
	}

	public void setPropertiyValue(String propertiyValue) {
		this.propertiyValue = propertiyValue;
	}


	/** 是否界面可见:: 1 是，0 否 */
	public Integer getStatisticFlag() {
		return this.statisticFlag;
	}
	/** 是否界面可见:: 1 是，0 否 */
	public void setStatisticFlag(Integer statisticFlag) {
		this.statisticFlag = statisticFlag;
	}

	public Integer getControlFlag() {
		return this.controlFlag;
	}

	public void setControlFlag(Integer controlFlag) {
		this.controlFlag = controlFlag;
	}

	public String getPropertiy1() {
		return this.propertiy1;
	}

	public void setPropertiy1(String propertiy1) {
		this.propertiy1 = propertiy1;
	}

	public String getPropertiy1Explain() {
		return this.propertiy1Explain;
	}

	public void setPropertiy1Explain(String propertiy1Explain) {
		this.propertiy1Explain = propertiy1Explain;
	}

	public String getPropertiy2() {
		return this.propertiy2;
	}

	public void setPropertiy2(String propertiy2) {
		this.propertiy2 = propertiy2;
	}

	public String getPropertiy2Explain() {
		return this.propertiy2Explain;
	}

	public void setPropertiy2Explain(String propertiy2Explain) {
		this.propertiy2Explain = propertiy2Explain;
	}

	public String getPropertiy3() {
		return this.propertiy3;
	}

	public void setPropertiy3(String propertiy3) {
		this.propertiy3 = propertiy3;
	}

	public String getPropertiy3Explain() {
		return this.propertiy3Explain;
	}

	public void setPropertiy3Explain(String propertiy3Explain) {
		this.propertiy3Explain = propertiy3Explain;
	}
}