package com.ibb.sys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LoginInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "login_info")
public class LoginInfo implements java.io.Serializable {

	// Fields

	private Integer loginId;//登录表主键
	private Integer userId;//用户id
	private String loginTime;//登录时间

	// Constructors

	/** default constructor */
	public LoginInfo() {
	}

	/** full constructor */
	public LoginInfo(Integer userId, String loginTime) {
		this.userId = userId;
		this.loginTime = loginTime;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "loginId", unique = true, nullable = false)
	public Integer getLoginId() {
		return this.loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	@Column(name = "userId")
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "loginTime", length = 50)
	public String getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

}