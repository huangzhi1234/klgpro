package com.ibb.model.patch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CheckLoad entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "check_load")
public class CheckLoad implements java.io.Serializable {

	// Fields

	private Integer checkId;//审核下载id
	private Integer state;//是否处于待审核状态，1为待审核,默认为0
	private Integer patId;//成果管理信息表的id
	private Integer isOk;//是否通过审核，1为通过，0为否，默认为0
	private Integer oper;//申请人
	private String time;//申请时间
	private Integer agreeOper;//批准者
	private String agreeTime;//审批时间
	private String remark;//审批不通过的原因
	private Integer type;//类型：1表示研发，2表示实施
	private String cause;//下载申请的理由

	// Constructors

	/** default constructor */
	public CheckLoad() {
	}

	/** full constructor */
	

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "checkId", unique = true, nullable = false)
	public Integer getCheckId() {
		return this.checkId;
	}

	public CheckLoad(Integer checkId, Integer state, Integer patId, Integer isOk, Integer oper, String time,
			Integer agreeOper, String agreeTime, String remark, Integer type, String cause) {
		super();
		this.checkId = checkId;
		this.state = state;
		this.patId = patId;
		this.isOk = isOk;
		this.oper = oper;
		this.time = time;
		this.agreeOper = agreeOper;
		this.agreeTime = agreeTime;
		this.remark = remark;
		this.type = type;
		this.cause = cause;
	}

	@Override
	public String toString() {
		return "CheckLoad [checkId=" + checkId + ", state=" + state + ", patId=" + patId + ", isOk=" + isOk + ", oper="
				+ oper + ", time=" + time + ", agreeOper=" + agreeOper + ", agreeTime=" + agreeTime + ", remark="
				+ remark + ", type=" + type + ", cause=" + cause + "]";
	}

	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}

	@Column(name = "state", nullable = false)
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "patId", nullable = false)
	public Integer getPatId() {
		return this.patId;
	}

	public void setPatId(Integer patId) {
		this.patId = patId;
	}

	@Column(name = "isOk", nullable = false)
	public Integer getIsOk() {
		return this.isOk;
	}

	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}
	
	@Column(name = "oper", nullable = false)
	public Integer getOper() {
		return oper;
	}

	public void setOper(Integer oper) {
		this.oper = oper;
	}
	
	@Column(name = "time", nullable = false)
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column(name="agreeOper")
	public Integer getAgreeOper() {
		return agreeOper;
	}

	public void setAgreeOper(Integer agreeOper) {
		this.agreeOper = agreeOper;
	}

	@Column(name="agreeTime")
	public String getAgreeTime() {
		return agreeTime;
	}

	public void setAgreeTime(String agreeTime) {
		this.agreeTime = agreeTime;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name="cause")
	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
	
	
	
	

}