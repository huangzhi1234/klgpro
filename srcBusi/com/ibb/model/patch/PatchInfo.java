package com.ibb.model.patch;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 研发和实施成果
 * @author fzw
 *
 */
@Entity
@Table(name = "patch_info")
public class PatchInfo implements java.io.Serializable {

	private Integer patId;
	private String patName;
	private String receiveTime;
	private String patMan;
	private String email;
	private String phone;
	private String qq;
	private String proId;
	private String proName;
	private String testStatus;
	private String testMan;
	private String patDis;
	private String ncVersion;
	private String busiMod;
	private String isDep;
	private String isSql;
	private String sqlName;
	private String metadataUp;
	private String metadataName;
	private String comment;
	private Integer upMan;
	private String upTime;
	private String file;
	private String dicNum;
	private String fileName;//文件名
	private String customer;
	private Integer state;
	
	private Integer type ;//1是开发，2是实施
	

	public PatchInfo() {
		super();
	}

	public PatchInfo(Integer patId, String patName, String receiveTime, String patMan, String email, String phone,
			String qq, String proId, String proName, String testStatus, String testMan, String patDis, String ncVersion,
			String busiMod, String isDep, String isSql, String sqlName, String metadataUp, String metadataName,
			String comment, Integer upMan, String upTime, String file, String fileName,String customer,Integer state) {
		super();
		this.patId = patId;
		this.patName = patName;
		this.receiveTime = receiveTime;
		this.patMan = patMan;
		this.email = email;
		this.phone = phone;
		this.qq = qq;
		this.proId = proId;
		this.proName = proName;
		this.testStatus = testStatus;
		this.testMan = testMan;
		this.patDis = patDis;
		this.ncVersion = ncVersion;
		this.busiMod = busiMod;
		this.isDep = isDep;
		this.isSql = isSql;
		this.sqlName = sqlName;
		this.metadataUp = metadataUp;
		this.metadataName = metadataName;
		this.comment = comment;
		this.upMan = upMan;
		this.upTime = upTime;
		this.file = file;
		this.fileName = fileName;
		this.customer=customer;
		this.state=state;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "patID", unique = true, nullable = false)
	public Integer getPatId() {
		return patId;
	}

	public void setPatId(Integer patId) {
		this.patId = patId;
	}

	@Column(name = "patName", nullable = false, length = 150)
	public String getPatName() {
		return patName;
	}

	public void setPatName(String patName) {
		this.patName = patName;
	}

	@Column(name = "receiveTime",  length = 20)
	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Column(name = "patMan",  length = 30)
	public String getPatMan() {
		return patMan;
	}

	public void setPatMan(String patMan) {
		this.patMan = patMan;
	}

	@Column(name = "email",  length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "phone",  length = 20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "QQ",  length = 20)
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "proId",  length = 11)
	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	@Column(name = "proName",  length = 50)
	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	@Column(name = "testStatus", nullable = false, length = 5)
	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	@Column(name = "testMan", length = 30)
	public String getTestMan() {
		return testMan;
	}

	public void setTestMan(String testMan) {
		this.testMan = testMan;
	}

	@Column(name = "patDis", nullable = false, length = 200)
	public String getPatDis() {
		return patDis;
	}

	public void setPatDis(String patDis) {
		this.patDis = patDis;
	}

	@Column(name = "ncVersion", nullable = false, length = 20)
	public String getNcVersion() {
		return ncVersion;
	}

	public void setNcVersion(String ncVersion) {
		this.ncVersion = ncVersion;
	}

	@Column(name = "busiMod", nullable = false, length = 20)
	public String getBusiMod() {
		return busiMod;
	}

	public void setBusiMod(String busiMod) {
		this.busiMod = busiMod;
	}

	@Column(name = "isDep", nullable = false, length = 5)
	public String getIsDep() {
		return isDep;
	}

	public void setIsDep(String isDep) {
		this.isDep = isDep;
	}

	@Column(name = "isSql", nullable = false, length = 5)
	public String getIsSql() {
		return isSql;
	}

	public void setIsSql(String isSql) {
		this.isSql = isSql;
	}

	@Column(name = "sqlName",  length = 50)
	public String getSqlName() {
		return sqlName;
	}

	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	@Column(name = "metadataUp", nullable = false, length = 5)
	public String getMetadataUp() {
		return metadataUp;
	}

	public void setMetadataUp(String metadataUp) {
		this.metadataUp = metadataUp;
	}

	@Column(name = "metadataName", nullable = false, length = 50)
	public String getMetadataName() {
		return metadataName;
	}

	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}

	@Column(name = "comment", nullable = false, length = 150)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "upMan", nullable = false, length = 20)
	public Integer getUpMan() {
		return upMan;
	}

	public void setUpMan(Integer upMan) {
		this.upMan = upMan;
	}

	@Column(name = "upTime")
	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	@Column(name = "file", length = 100)
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	@Column(name = "dicNum", length = 30)
	public String getDicNum() {
		return dicNum;
	}

	public void setDicNum(String dicNum) {
		this.dicNum = dicNum;
	}
	
	@Column(name = "fileName", length = 50)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Column(name = "customer", length = 50)
	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}
	@Column(name = "state", length = 2)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "type", length = 2)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
