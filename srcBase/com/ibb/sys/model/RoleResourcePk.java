package com.ibb.sys.model;

import javax.persistence.Embeddable;

@Embeddable
public class RoleResourcePk implements java.io.Serializable{
	private static final long serialVersionUID = 1574689985991432078L;

	private int roleId;
	
	private int resourceId;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + resourceId;
		result = prime * result + roleId;
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
		RoleResourcePk other = (RoleResourcePk) obj;
		if (resourceId != other.resourceId)
			return false;
		if (roleId != other.roleId)
			return false;
		return true;
	}
}