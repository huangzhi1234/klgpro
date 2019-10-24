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
@Table(name="tbl_sys_role_resource")
public class RoleResourceBean extends AbstractModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	private RoleResourcePk pk;

	public RoleResourcePk getPk() {
		return pk;
	}

	public void setPk(RoleResourcePk pk) {
		this.pk = pk;
	}
}