package com.ibb.model.projectMessage;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author WangGangWei
 * @date 2017年4月20日 下午2:09:13
 *
 */
@Entity
@Table(name = "pro_member")

public class ProMember implements java.io.Serializable {

	// Fields

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Integer memberId;
	private String proNum;
	private String memberName;
	private String roleaName;
	private String phone;
	private String email;
	private String delyModule;
	private String comment;
	private String responsibleArea;
	private Integer state;

	// Constructors

	/** default constructor */
	public ProMember() {
	}

	/** minimal constructor */
	public ProMember(Integer memberId, String proNum, String memberName) {
		this.memberId = memberId;
		this.proNum = proNum;
		this.memberName = memberName;
	}

	/** full constructor */
	public ProMember(Integer memberId, String proNum, String memberName, String roleaName, String phone, String email,
			String delyModule, String comment,String responsibleArea,Integer state) {
		this.memberId = memberId;
		this.proNum = proNum;
		this.memberName = memberName;
		this.roleaName = roleaName;
		this.phone = phone;
		this.email = email;
		this.delyModule = delyModule;
		this.comment = comment;
		this.responsibleArea = responsibleArea;
		this.state=state;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "memberId", unique = true, nullable = false)

	public Integer getMemberId() {
		return this.memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	@Column(name = "proNum", nullable = false, length = 100)

	public String getProNum() {
		return this.proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	@Column(name = "memberName", nullable = false, length = 50)

	public String getMemberName() {
		return this.memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Column(name = "roleaName", length = 50)

	public String getRoleaName() {
		return this.roleaName;
	}

	public void setRoleaName(String roleaName) {
		this.roleaName = roleaName;
	}

	@Column(name = "phone", length = 50)

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "email", length = 20)

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "delyModule", length = 20)

	public String getDelyModule() {
		return this.delyModule;
	}

	public void setDelyModule(String delyModule) {
		this.delyModule = delyModule;
	}

	@Column(name = "comment", length = 200)

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "responsibleArea", length = 30)
	public String getResponsibleArea() {
		return responsibleArea;
	}

	public void setResponsibleArea(String responsibleArea) {
		this.responsibleArea = responsibleArea;
	}
	@Column(name = "state", length = 2)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	
}