package com.ibb.model.svn;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SvnDicInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "svn_dic_info")
public class SvnDicInfo implements java.io.Serializable {

	// Fields

	private Integer svnId;
	private String address;//�ļ�·��
	private String fileName;//�ֵ����
	private Integer parentId;//���ڵ�
	private String submitTime;//�ϴ�ʱ��
	private String submitOper;//�ϴ���

	// Constructors

	/** default constructor */
	public SvnDicInfo() {
	}

	/** minimal constructor */
	public SvnDicInfo(String fileName, Integer parentId) {
		this.fileName = fileName;
		this.parentId = parentId;
	}

	/** full constructor */
	public SvnDicInfo(String address, String fileName, Integer parentId,
			String submitTime, String submitOper) {
		this.address = address;
		this.fileName = fileName;
		this.parentId = parentId;
		this.submitTime = submitTime;
		this.submitOper = submitOper;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "svnId", unique = true, nullable = false)
	public Integer getSvnId() {
		return this.svnId;
	}

	public void setSvnId(Integer svnId) {
		this.svnId = svnId;
	}

	@Column(name = "address", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "fileName", nullable = false, length = 200)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "parentId", nullable = false)
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "submitTime", length = 50)
	public String getSubmitTime() {
		return this.submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	@Column(name = "submitOper", length = 50)
	public String getSubmitOper() {
		return this.submitOper;
	}

	public void setSubmitOper(String submitOper) {
		this.submitOper = submitOper;
	}

}