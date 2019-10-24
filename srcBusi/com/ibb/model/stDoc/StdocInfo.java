package com.ibb.model.stDoc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 标准文档实体类
 * StdocInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "stdoc_info")
public class StdocInfo implements java.io.Serializable {

	// Fields

	private Integer stdocId;//标准文档id
	private String fileName;//文件名
	private String fileAddress;//文件路径
	private String dicNum;//字典编号
	private String createOper;//创建人/
	private String createTime;//创建时间
	private String comment;//文档说明
	private String author;//文档作者
	private Integer state;
	//add 文档类型 1--开发    2--实施
	private Integer type;//文档类型
	
	//为经典案例新增字段
	private String industry;//所属行业
	
	private String proname;//项目名称
	
	private String area;
	
	
	@Column(name = "area", length = 40)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	// Constructors
	@Column(name = "type", length = 2)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/** default constructor */
	public StdocInfo() {
	}

	/** full constructor */
	public StdocInfo(Integer stdocId, String fileName, String fileAddress, String dicNum, String createOper,
			String createTime, String comment, String author, Integer state, Integer type, String industry,
			String proname, String area) {
		super();
		this.stdocId = stdocId;
		this.fileName = fileName;
		this.fileAddress = fileAddress;
		this.dicNum = dicNum;
		this.createOper = createOper;
		this.createTime = createTime;
		this.comment = comment;
		this.author = author;
		this.state = state;
		this.type = type;
		this.industry = industry;
		this.proname = proname;
		this.area = area;
	}


	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "stdocId", unique = true, nullable = false)
	public Integer getStdocId() {
		return this.stdocId;
	}

	public void setStdocId(Integer stdocId) {
		this.stdocId = stdocId;
	}

	@Column(name = "fileName", length = 50)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "fileAddress", length = 50)
	public String getFileAddress() {
		return this.fileAddress;
	}

	public void setFileAddress(String fileAddress) {
		this.fileAddress = fileAddress;
	}

	@Column(name = "dicNum", length = 10)
	public String getDicNum() {
		return this.dicNum;
	}

	public void setDicNum(String dicNum) {
		this.dicNum = dicNum;
	}

	@Column(name = "createOper", length = 50)
	public String getCreateOper() {
		return this.createOper;
	}

	public void setCreateOper(String createOper) {
		this.createOper = createOper;
	}

	@Column(name = "createTime", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "comment", length = 500)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name = "author", length = 500)
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Column(name = "state", length = 2)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	@Column(name = "industry", length = 40)
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Column(name = "proname", length = 60)
	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}
	
}