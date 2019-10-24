package com.ibb.sys.model;

import javax.persistence.Embeddable;

@Embeddable
public class UserRolePk implements java.io.Serializable{
	private static final long serialVersionUID = 568194644282677554L;

	private int userId;
	
	private int roleId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + roleId;
		result = prime * result + userId;
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
		UserRolePk other = (UserRolePk) obj;
		if (roleId != other.roleId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
}