package com.ibb.model.patch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibb.dto.BaseDto;
import com.ibb.model.datadic.TBaseDataDictionary;

@Entity
@Table(name = "pro_dic")
public class TblProDic extends BaseDto implements java.io.Serializable {

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

	private String q;// combogrid模糊查询的时候传的参数名称

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public TblProDic() {
		super();
	}

	public TblProDic(String systemId, String upSystemId, String classNo, String standardNo, String standardChName,
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

	@Id
	@Column(name = "systemId")
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	@Column(name = "upSystemId", length = 30)
	public String getUpSystemId() {
		return upSystemId;
	}

	public void setUpSystemId(String upSystemId) {
		this.upSystemId = upSystemId;
	}

	@Column(name = "classNo", length = 2)
	public String getClassNo() {
		return classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	@Column(name = "standardNo", length = 20)
	public String getStandardNo() {
		return standardNo;
	}

	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}

	@Column(name = "standardChName", length = 80)
	public String getStandardChName() {
		return standardChName;
	}

	public void setStandardChName(String standardChName) {
		this.standardChName = standardChName;
	}

	@Column(name = "standardEnName", length = 80)
	public String getStandardEnName() {
		return standardEnName;
	}

	public void setStandardEnName(String standardEnName) {
		this.standardEnName = standardEnName;
	}

	@Column(name = "chineseName", length = 80)
	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	@Column(name = "englishName", length = 80)
	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	@Column(name = "sortNo", length = 10)
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	@Column(name = "propertiyValue", length = 500)
	public String getPropertiyValue() {
		return propertiyValue;
	}

	public void setPropertiyValue(String propertiyValue) {
		this.propertiyValue = propertiyValue;
	}

	@Column(name = "statisticFlag")
	public Integer getStatisticFlag() {
		return statisticFlag;
	}

	public void setStatisticFlag(Integer statisticFlag) {
		this.statisticFlag = statisticFlag;
	}

	@Column(name = "controlFlag")
	public Integer getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(Integer controlFlag) {
		this.controlFlag = controlFlag;
	}

	@Column(name = "typeFlag")
	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	@Column(name = "propertiy1", length = 100)
	public String getPropertiy1() {
		return propertiy1;
	}

	public void setPropertiy1(String propertiy1) {
		this.propertiy1 = propertiy1;
	}

	@Column(name = "propertiy1Explain", length = 100)
	public String getPropertiy1Explain() {
		return propertiy1Explain;
	}

	public void setPropertiy1Explain(String propertiy1Explain) {
		this.propertiy1Explain = propertiy1Explain;
	}

	@Column(name = "propertiy2", length = 100)
	public String getPropertiy2() {
		return propertiy2;
	}

	public void setPropertiy2(String propertiy2) {
		this.propertiy2 = propertiy2;
	}

	@Column(name = "propertiy2Explain", length = 100)
	public String getPropertiy2Explain() {
		return propertiy2Explain;
	}

	public void setPropertiy2Explain(String propertiy2Explain) {
		this.propertiy2Explain = propertiy2Explain;
	}

	@Column(name = "propertiy3", length = 100)
	public String getPropertiy3() {
		return propertiy3;
	}

	public void setPropertiy3(String propertiy3) {
		this.propertiy3 = propertiy3;
	}

	@Column(name = "propertiy3Explain", length = 100)
	public String getPropertiy3Explain() {
		return propertiy3Explain;
	}

	public void setPropertiy3Explain(String propertiy3Explain) {
		this.propertiy3Explain = propertiy3Explain;
	}

	/**
	 * 将页面可以修改的属性赋给实体类
	 * 
	 * @param t
	 */
	public void copyEditProperties(ProDic t) {
		t.setChineseName(this.getChineseName());
		t.setStandardChName(this.getStandardChName());
		t.setEnglishName(this.getEnglishName());
		t.setStandardEnName(this.getStandardEnName());
		t.setStandardNo(this.getStandardNo());
		t.setPropertiyValue(this.getPropertiyValue());
		t.setSortNo(this.getSortNo());
		t.setPropertiy1(this.getPropertiy1());
		t.setPropertiy1Explain(this.getPropertiy1Explain());
		t.setPropertiy2(this.getPropertiy2());
		t.setPropertiy2Explain(this.getPropertiy2Explain());
		t.setPropertiy3(this.getPropertiy3());
		t.setPropertiy3Explain(this.getPropertiy3Explain());

		t.setStatisticFlag(this.getStatisticFlag());
	}

}
