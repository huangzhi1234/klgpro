package com.ibb.model.patch;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pat_dl_rc")
public class PatchDlRc implements java.io.Serializable {

	private Integer dlId;//主键
	private Integer patId;//成果ID
	private Integer dlMan;//用户id
	private String dlTime;//下载时间
	private Integer dlNum;//下载次数

	public PatchDlRc() {
		super();
	}


	public PatchDlRc(Integer dlId, Integer patId, Integer dlMan, String dlTime,
			Integer dlNum) {
		super();
		this.dlId = dlId;
		this.patId = patId;
		this.dlMan = dlMan;
		this.dlTime = dlTime;
		this.dlNum = dlNum;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "dlId", unique = true, nullable = false)
	public Integer getDlId() {
		return dlId;
	}

	public void setDlId(Integer dlId) {
		this.dlId = dlId;
	}

	@Column(name = "patId",  length = 20)
	public Integer getPatId() {
		return patId;
	}

	public void setPatId(Integer patId) {
		this.patId = patId;
	}

	@Column(name = "dlMan",  length = 20)
	public Integer getDlMan() {
		return dlMan;
	}

	public void setDlMan(Integer dlMan) {
		this.dlMan = dlMan;
	}

	@Column(name = "dlTime")
	public String getDlTime() {
		return dlTime;
	}

	public void setDlTime(String dlTime) {
		this.dlTime = dlTime;
	}

	@Column(name="dlNum")
	public Integer getDlNum() {
		return dlNum;
	}


	public void setDlNum(Integer dlNum) {
		this.dlNum = dlNum;
	}

	
}
