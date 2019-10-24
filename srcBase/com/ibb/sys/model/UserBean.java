package com.ibb.sys.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibb.common.model.AbstractModel;

/**
 * 系统用户
 * 
 * @author kin wong
 */
@Entity
@Table(name = "tbl_sys_user")
public class UserBean extends AbstractModel {
	private static final long serialVersionUID = -2354747878130409197L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userId", nullable = false)
	private int userId;

	private String userAct;
	private String userPwd;
	private String userName;
	private String userPhone;
	private String userMail;
	private Date actTime;
	private String userAge;
	private String userSex;
	private String imgSrc;
	private String descript;
	private Integer departId;
	private String position;

	public Integer getDepartId() {
		return departId;
	}

	public void setDepartId(Integer departId) {
		this.departId = departId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserAct() {
		return userAct;
	}

	public void setUserAct(String userAct) {
		this.userAct = userAct;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public Date getActTime() {
		return actTime;
	}

	public void setActTime(Date actTime) {
		this.actTime = actTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actTime == null) ? 0 : actTime.hashCode());
		result = prime * result + ((departId == null) ? 0 : departId.hashCode());
		result = prime * result + ((descript == null) ? 0 : descript.hashCode());
		result = prime * result + ((imgSrc == null) ? 0 : imgSrc.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((userAct == null) ? 0 : userAct.hashCode());
		result = prime * result + ((userAge == null) ? 0 : userAge.hashCode());
		result = prime * result + userId;
		result = prime * result + ((userMail == null) ? 0 : userMail.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userPhone == null) ? 0 : userPhone.hashCode());
		result = prime * result + ((userPwd == null) ? 0 : userPwd.hashCode());
		result = prime * result + ((userSex == null) ? 0 : userSex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBean other = (UserBean) obj;
		if (actTime == null) {
			if (other.actTime != null)
				return false;
		} else if (!actTime.equals(other.actTime))
			return false;
		if (departId == null) {
			if (other.departId != null)
				return false;
		} else if (!departId.equals(other.departId))
			return false;
		if (descript == null) {
			if (other.descript != null)
				return false;
		} else if (!descript.equals(other.descript))
			return false;
		if (imgSrc == null) {
			if (other.imgSrc != null)
				return false;
		} else if (!imgSrc.equals(other.imgSrc))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (userAct == null) {
			if (other.userAct != null)
				return false;
		} else if (!userAct.equals(other.userAct))
			return false;
		if (userAge == null) {
			if (other.userAge != null)
				return false;
		} else if (!userAge.equals(other.userAge))
			return false;
		if (userId != other.userId)
			return false;
		if (userMail == null) {
			if (other.userMail != null)
				return false;
		} else if (!userMail.equals(other.userMail))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userPhone == null) {
			if (other.userPhone != null)
				return false;
		} else if (!userPhone.equals(other.userPhone))
			return false;
		if (userPwd == null) {
			if (other.userPwd != null)
				return false;
		} else if (!userPwd.equals(other.userPwd))
			return false;
		if (userSex == null) {
			if (other.userSex != null)
				return false;
		} else if (!userSex.equals(other.userSex))
			return false;
		return true;
	}
	

}