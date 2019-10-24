package com.ibb.model.projectMessage;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibb.common.model.AbstractModel;

@Entity
@Table(name = "tbl_user_pro")
public class UserProBean implements java.io.Serializable {

	private int id;
	private int userId;
	private int proId;
	private int state;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "userId", length = 100)
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Column(name = "proId", length = 100)
	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	@Column(name = "state", length = 2)
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	
}