package com.ibb.model.projectMessage;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pro_sub_info")
public class ProSubInfo implements java.io.Serializable{

	private Integer subId;
	private String proNum;
	private String proSubName;
	private String proSubNum;
	private String signTime;
	private String proSubDis;
	private String filePath;
	private String proName;
	private String fileName;//文件名
	private Integer state;

	public ProSubInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProSubInfo(Integer subId, String proNum, String proSubName, String proSubNum, String signTime,
			String proSubDis, String filePath,String proName, String fileName,Integer state) {
		super();
		this.subId = subId;
		this.proNum = proNum;
		this.proSubName = proSubName;
		this.proSubNum = proSubNum;
		this.signTime = signTime;
		this.proSubDis = proSubDis;
		this.filePath = filePath;
		this.proName = proName;
		this.fileName = fileName;
		this.state=state;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "subId", unique = true, nullable = false)
	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	@Column(name = "proNum", length = 100)
	public String getProNum() {
		return proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	@Column(name = "proSubName", length = 100)
	public String getProSubName() {
		return proSubName;
	}

	public void setProSubName(String proSubName) {
		this.proSubName = proSubName;
	}

	@Column(name = "proSubNum", length = 50)
	public String getProSubNum() {
		return proSubNum;
	}

	public void setProSubNum(String proSubNum) {
		this.proSubNum = proSubNum;
	}

	@Column(name = "signTime", length = 20)
	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	@Column(name = "proSubDis", length = 150)
	public String getProSubDis() {
		return proSubDis;
	}

	public void setProSubDis(String proSubDis) {
		this.proSubDis = proSubDis;
	}

	@Column(name = "filePath", length = 100)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Column(name = "proName", length = 50)
	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	@Column(name = "fileName", length = 50)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "state", length = 2)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
}
