package com.ibb.sys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibb.common.model.AbstractModel;

/**
 * 数据字典项
 * @author kin wong
 */
@Entity
@Table(name="tbl_sys_dict_item")
public class DictItemBean extends AbstractModel{
	private static final long serialVersionUID = 2855473987832193358L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "itemId", nullable = false)
	private int itemId;
	
	private String dictCode;
	
	private String itemCode;
	
	private String itemName;
	
	private int itemOrder;
	
	private String itemDesc;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
}
