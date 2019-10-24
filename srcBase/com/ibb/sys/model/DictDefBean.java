package com.ibb.sys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibb.common.model.AbstractModel;

/**
 * 数据字典定义
 * @author kin wong
 */
@Entity
@Table(name="tbl_sys_dict_def")
public class DictDefBean extends AbstractModel{
	private static final long serialVersionUID = 8111908826353993562L;
	//@Id表示主键
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "dictId", nullable = false)
	private int dictId;
	
	private String dictCode;
	
	private String dictName;
	
	private String dictDesc;

	public int getDictId() {
		return dictId;
	}

	public void setDictId(int dictId) {
		this.dictId = dictId;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictDesc() {
		return dictDesc;
	}

	public void setDictDesc(String dictDesc) {
		this.dictDesc = dictDesc;
	}
}
