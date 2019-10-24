package com.ibb.common.web.model;

import java.util.List;

/**
 * EasyUI 树返回格式
 * @author kin wong
 */
public class EasyUITreeJsonModel {
	private String id;	//节点ID
	private String text;	//节点名称
	private String state;	//节点状态('open' 或 'closed')
	private boolean checked;	//是否选中
	private Object attributes;	//节点的其它属性
	private List<EasyUITreeJsonModel> children ;	//子结点
	
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
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Object getAttributes() {
		return attributes;
	}
	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}
	public List<EasyUITreeJsonModel> getChildren() {
		return children;
	}
	public void setChildren(List<EasyUITreeJsonModel> children) {
		this.children = children;
	}
}
