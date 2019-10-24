package com.ibb.model.stDoc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * StdocUploadcheck entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="stdoc_uploadcheck")

public class StdocUploadcheck  implements java.io.Serializable {


    // Fields    

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer uploadId;//标准文档上传审批id
     private Integer state;//审批状态，1为申请中
     private Integer stdocId;//标准文档id
     private Integer isOk;//是否通过审批
     private Integer oper;//上传者
     private String time;//上传时间
     private String agreeTime;//审批时间
     private Integer agreeOper;//审批人
     private String remark;//备注
     //add 文档标识  1为开发    2为实施
     private Integer type;


    // Constructors

    /** default constructor */
    public StdocUploadcheck() {
    }

	/** minimal constructor */
    public StdocUploadcheck(Integer state, Integer stdocId, Integer isOk, Integer oper, String time) {
        this.state = state;
        this.stdocId = stdocId;
        this.isOk = isOk;
        this.oper = oper;
        this.time = time;
    }
    
    /** full constructor */
    public StdocUploadcheck(Integer state, Integer stdocId, Integer isOk, Integer oper, String time, String agreeTime, Integer agreeOper, String remark,Integer type) {
        this.state = state;
        this.stdocId = stdocId;
        this.isOk = isOk;
        this.oper = oper;
        this.time = time;
        this.agreeTime = agreeTime;
        this.agreeOper = agreeOper;
        this.remark = remark;
        this.type = type;
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
    
    @Column(name="stdocId", nullable=false)

    public Integer getStdocId() {
        return this.stdocId;
    }
    
    public void setStdocId(Integer stdocId) {
        this.stdocId = stdocId;
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
    
    @Column(name="agreeTime", length=50)

    public String getAgreeTime() {
        return this.agreeTime;
    }
    
    public void setAgreeTime(String agreeTime) {
        this.agreeTime = agreeTime;
    }
    
    @Column(name="agreeOper", length=50)

    public Integer getAgreeOper() {
        return this.agreeOper;
    }
    
    public void setAgreeOper(Integer agreeOper) {
        this.agreeOper = agreeOper;
    }
    
    @Column(name="remark", length=100)

    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name="type", length=2)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
   








}