package com.ibb.model.projectMessage;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 客户信息实体类
 * @author FangZeWang
 * @date 2017年5月19日 上午10:15:13
 *
 */
@Entity
@Table(name="pro_customerMember")
public class ProCustomerMember implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer customerId;//编号
	private String proNum;//项目编号
	private String cusName;//客户名字
	private String cusRole;//角色
	private String phone;//联系电话
	private String email;//邮箱
	private String responsibleArea;//责任领域
	private String delyModule;//工作描述
	private Integer state;
	
	
	
	/** default constructor */
	public ProCustomerMember() {
		super();
	}
	/** full constructor */
	public ProCustomerMember(Integer customerId, String proNum, String cusName,
			String cusRole, String phone, String email, String responsibleArea,
			String delyModule,Integer state) {
		super();
		this.customerId = customerId;
		this.proNum = proNum;
		this.cusName = cusName;
		this.cusRole = cusRole;
		this.phone = phone;
		this.email = email;
		this.responsibleArea = responsibleArea;
		this.delyModule = delyModule;
		this.state=state;
	}
	
	@Id 
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="customerId",unique = true, nullable = false)
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	@Column(name = "proNum", nullable = false, length = 100)
	public String getProNum() {
		return proNum;
	}
	public void setProNum(String proNum) {
		this.proNum = proNum;
	}
	@Column(name = "cusName", length = 50)
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	@Column(name = "cusRole", length = 50)
	public String getCusRole() {
		return cusRole;
	}
	public void setCusRole(String cusRole) {
		this.cusRole = cusRole;
	}
	@Column(name = "phone", length = 50)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(name = "email", length = 50)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name = "responsibleArea", length = 50)
	public String getResponsibleArea() {
		return responsibleArea;
	}
	public void setResponsibleArea(String responsibleArea) {
		this.responsibleArea = responsibleArea;
	}
	@Column(name = "delyModule", length = 255)
	public String getDelyModule() {
		return delyModule;
	}
	public void setDelyModule(String delyModule) {
		this.delyModule = delyModule;
	}
	
	
	@Column(name = "state", length = 2)
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "ProCustomerMember [customerId=" + customerId + ", proNum="
				+ proNum + ", cusName=" + cusName + ", cusRole=" + cusRole
				+ ", phone=" + phone + ", email=" + email
				+ ", responsibleArea=" + responsibleArea + ", delyModule="
				+ delyModule + "]";
	}
	
	
	
	
}
