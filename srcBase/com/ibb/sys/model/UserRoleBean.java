package com.ibb.sys.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibb.common.model.AbstractModel;

/**
 * 系统用户
 * @author kin wong
 */
@Entity
@Table(name="tbl_sys_user_role")
public class UserRoleBean extends AbstractModel{
	private static final long serialVersionUID = 7556743434070109319L;
	
	@Id
	private UserRolePk pk;

	public UserRolePk getPk() {
		return pk;
	}

	public void setPk(UserRolePk pk) {
		this.pk = pk;
	}
}