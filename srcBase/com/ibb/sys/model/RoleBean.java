package com.ibb.sys.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibb.common.model.AbstractModel;

/**
 * 系统角色
 * @author kin wong
 */
@Entity
@Table(name="tbl_sys_role")
public class RoleBean extends AbstractModel{
	private static final long serialVersionUID = 1204130625951407523L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int roleId;
	
	private String roleCode;
	
	private String roleName;
	
	private String roleDesc;
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
}
