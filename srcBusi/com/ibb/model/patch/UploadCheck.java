package com.ibb.model.patch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * UploadCheck entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="upload_check")

public class UploadCheck  implements java.io.Serializable {


    // Fields    

     private Integer uploadId;//上传文档审批id
     private Integer state;//审批状态
     private Integer patId;//上传文档的id
     private Integer isOk;//审批是否通过
     private Integer oper;//上传者
     private String time;//上传时间
     private Integer agreeOper;//审批者
     private String remark;//备注
     private String agreeTime;//审批时间
     private Integer type;//类型：1表示研发，2表示实施

    // Constructors

    /** default constructor */
    public UploadCheck() {
    }

	/** minimal constructor */
    public UploadCheck(Integer state, Integer patId, Integer isOk, Integer oper, String time) {
        this.state = state;
        this.patId = patId;
        this.isOk = isOk;
        this.oper = oper;
        this.time = time;
    }
    
    /** full constructor */
    public UploadCheck(Integer state, Integer patId, Integer isOk, Integer oper, String time, Integer agreeOper,String remark,String agreeTime) {
        this.state = state;
        this.patId = patId;
        this.isOk = isOk;
        this.oper = oper;
        this.time = time;
        this.agreeOper = agreeOper;
        this.remark=remark;
        this.agreeTime=agreeTime;
    }

   
    // Property accessors
    @Id @GeneratedValue
    
    @Column(name="uploadId", unique=true, nullable=false)

    public Integer getUploadId() {
        return this.uploadId;
    }
    
    public void setUploadId(Integer uploadId) {
        this.uploadId = uploadId;
    }
    
    @Column(name="state", nullable=false)

    public Integer getState() {
        return this.state;
    }
    
    public void setState(Integer state) {
        this.state = state;
    }
    
    @Column(name="patId", nullable=false)

    public Integer getPatId() {
        return this.patId;
    }
    
    public void setPatId(Integer patId) {
        this.patId = patId;
    }
    
    @Column(name="isOk", nullable=false)

    public Integer getIsOk() {
        return this.isOk;
    }
    
    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }
    
    @Column(name="oper", nullable=false, length=50)

    public Integer getOper() {
        return this.oper;
    }
    
    public void setOper(Integer oper) {
        this.oper = oper;
    }
    
    @Column(name="time", nullable=false, length=50)

    public String getTime() {
        return this.time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    @Column(name="agreeOper", length=50)

    public Integer getAgreeOper() {
        return this.agreeOper;
    }
    
    public void setAgreeOper(Integer agreeOper) {
        this.agreeOper = agreeOper;
    }
    
    @Column(name="remark",length=100)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
   
    
	@Column(name="agreeTime")
	public String getAgreeTime() {
		return agreeTime;
	}

	public void setAgreeTime(String agreeTime) {
		this.agreeTime = agreeTime;
	}

	@Column(name="type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	







}