package com.ibb.model.patch;

import com.ibb.common.model.AbstractModel;

public class ProDic extends AbstractModel {

	private static final long serialVersionUID = 1L;
	private String systemId; // 字典ID
	private String upSystemId; // 父节点ID
	private String classNo; // 描述当前代码集所在的层次
	private String standardNo; // 国标编码
	private String standardChName; // 国标中文名称
	private String standardEnName; // 国标英文名称
	private String chineseName; // 字典中文名
	private String englishName; // 字典英文名
	private Integer sortNo; // 排序
	private String propertiyValue; // 字典属性值
	private Integer statisticFlag; // 是否界面下拉可见: 1 是，0 否（内部代码使用）
	private Integer controlFlag; // (备用)
	private Integer typeFlag; // (备用)
	private String propertiy1; // 扩展属性一
	private String propertiy1Explain;// 扩展属性一描述
	private String propertiy2;
	private String propertiy2Explain;
	private String propertiy3;
	private String propertiy3Explain;
	
	private String q;//combogrid模糊查询的时候传的参数名称

	public ProDic() {
		super();
	}

	public ProDic(String systemId, String upSystemId, String classNo, String standardNo, String standardChName,
			String standardEnName, String chineseName, String englishName, Integer sortNo, String propertiyValue,
			Integer statisticFlag, Integer controlFlag, Integer typeFlag, String propertiy1, String propertiy1Explain,
			String propertiy2, String propertiy2Explain, String propertiy3, String propertiy3Explain) {
		super();
		this.systemId = systemId;
		this.upSystemId = upSystemId;
		this.classNo = classNo;
		this.standardNo = standardNo;
		this.standardChName = standardChName;
		this.standardEnName = standardEnName;
		this.chineseName = chineseName;
		this.englishName = englishName;
		this.sortNo = sortNo;
		this.propertiyValue = propertiyValue;
		this.statisticFlag = statisticFlag;
		this.controlFlag = controlFlag;
		this.typeFlag = typeFlag;
		this.propertiy1 = propertiy1;
		this.propertiy1Explain = propertiy1Explain;
		this.propertiy2 = propertiy2;
		this.propertiy2Explain = propertiy2Explain;
		this.propertiy3 = propertiy3;
		this.propertiy3Explain = propertiy3Explain;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUpSystemId() {
		return upSystemId;
	}

	public void setUpSystemId(String upSystemId) {
		this.upSystemId = upSystemId;
	}

	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	public String getStandardNo() {
		return standardNo;
	}

	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}

	public String getStandardChName() {
		return standardChName;
	}

	public void setStandardChName(String standardChName) {
		this.standardChName = standardChName;
	}

	public String getStandardEnName() {
		return standardEnName;
	}

	public void setStandardEnName(String standardEnName) {
		this.standardEnName = standardEnName;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getPropertiyValue() {
		return propertiyValue;
	}

	public void setPropertiyValue(String propertiyValue) {
		this.propertiyValue = propertiyValue;
	}

	public Integer getStatisticFlag() {
		return statisticFlag;
	}

	public void setStatisticFlag(Integer statisticFlag) {
		this.statisticFlag = statisticFlag;
	}

	public Integer getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(Integer controlFlag) {
		this.controlFlag = controlFlag;
	}

	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	public String getPropertiy1() {
		return propertiy1;
	}

	public void setPropertiy1(String propertiy1) {
		this.propertiy1 = propertiy1;
	}

	public String getPropertiy1Explain() {
		return propertiy1Explain;
	}

	public void setPropertiy1Explain(String propertiy1Explain) {
		this.propertiy1Explain = propertiy1Explain;
	}

	public String getPropertiy2() {
		return propertiy2;
	}

	public void setPropertiy2(String propertiy2) {
		this.propertiy2 = propertiy2;
	}

	public String getPropertiy2Explain() {
		return propertiy2Explain;
	}

	public void setPropertiy2Explain(String propertiy2Explain) {
		this.propertiy2Explain = propertiy2Explain;
	}

	public String getPropertiy3() {
		return propertiy3;
	}

	public void setPropertiy3(String propertiy3) {
		this.propertiy3 = propertiy3;
	}

	public String getPropertiy3Explain() {
		return propertiy3Explain;
	}

	public void setPropertiy3Explain(String propertiy3Explain) {
		this.propertiy3Explain = propertiy3Explain;
	}

}
