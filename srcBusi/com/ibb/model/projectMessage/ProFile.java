package com.ibb.model.projectMessage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ProFile entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "pro_file")
public class ProFile implements java.io.Serializable {

	// Fields

	private Integer fileId;//文件id
	private String address;//文件路径
	private String fileName;//当parentId为0时，表示为项目阶段，其他情况，表示为文件名
	private String submitTime;//提交时间
	private String submitOper;//提交人
	private String proNum;//项目编号
	private Integer parentId;//父节点id，根目录的值为0
	private Integer state;

	// Constructors

	/** default constructor */
	public ProFile() {
	}

	/** minimal constructor */
	public ProFile(Integer parentId) {
		this.parentId = parentId;
	}

	/** full constructor */
	public ProFile(String address, String fileName, String submitTime,
			String submitOper, String proNum, Integer parentId,Integer state) {
		this.address = address;
		this.fileName = fileName;
		this.submitTime = submitTime;
		this.submitOper = submitOper;
		this.proNum = proNum;
		this.parentId = parentId;
		this.state=state;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "fileId", unique = true, nullable = false)
	public Integer getFileId() {
		return this.fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	@Column(name = "address", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "fileName", length = 50)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "submitTime", length = 20)
	public String getSubmitTime() {
		return this.submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	@Column(name = "submitOper", length = 20)
	public String getSubmitOper() {
		return this.submitOper;
	}

	public void setSubmitOper(String submitOper) {
		this.submitOper = submitOper;
	}

	@Column(name = "proNum", length = 50)
	public String getProNum() {
		return this.proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	@Column(name = "parentId", nullable = false)
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	
}