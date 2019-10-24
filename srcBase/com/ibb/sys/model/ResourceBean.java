package com.ibb.sys.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ibb.common.model.AbstractModel;

/**
 * 系统资源(菜单、功能按钮)
 * @author kin wong
 */
@Entity
@Table(name="tbl_sys_resource")
public class ResourceBean extends AbstractModel{
	private static final long serialVersionUID = 694883115064050010L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int resourceId;//资源id
	
	private int resourcePid;//父资源id
	
	private String resourceInd;
	
	private String resourceName;
	
	private int resourceIndex;
	
	private String resourceUrl;
	
	private String resourceType;
	
	@Transient
	private List<ResourceBean> children = new ArrayList<ResourceBean>();//用户自定义添加：目的为实现树型列表展示

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public int getResourcePid() {
		return resourcePid;
	}

	public void setResourcePid(int resourcePid) {
		this.resourcePid = resourcePid;
	}

	public String getResourceInd() {
		return resourceInd;
	}

	public void setResourceInd(String resourceInd) {
		this.resourceInd = resourceInd;
	}
	
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getResourceIndex() {
		return resourceIndex;
	}

	public void setResourceIndex(int resourceIndex) {
		this.resourceIndex = resourceIndex;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public List<ResourceBean> getChildren() {
		return children;
	}

	public void setChildren(List<ResourceBean> children) {
		this.children = children;
	}
	
	public boolean addChild(ResourceBean resourceBean){
		return this.children.add(resourceBean);
	}
	
	public boolean hasChild(){
		return this.children.size() > 0 ;
	}
}
