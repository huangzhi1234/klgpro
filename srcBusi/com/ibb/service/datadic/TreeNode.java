package com.ibb.service.datadic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EasyUI Tree的数据传输实体
 */
public class TreeNode implements java.io.Serializable {
	
	private String id;			//节点Id
	private String text;			//节点名称
	private String state;		//tree的打开关闭（open/closed）
	private String iconCls;
	private String checked;
	private List children;  //Tree控件的children
	private Map attributes;   //Tree控件的attributes属性集
	
	public TreeNode() {
		super();
	}
	public TreeNode(String id) {
		super();
		this.id = id;
	}
	
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List getChildren() {
		return children;
	}
	public void setChildren(List children) {
		this.children = children;
	}
	public Map getAttributes() {
		return attributes;
	}
	public void setAttributes(Map attributes) {
		this.attributes = attributes;
	}
	
}
