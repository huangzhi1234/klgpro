package com.ibb.model.projectMessage;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 附件实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="pro_attach_info")
public class ProAttach implements Serializable{
	private int attachId;//附件编号
	private String proStage;//项目阶段
	private String attachAddress;//文件地址
	private String createOper;//创建者
	private String fileName;//文件名称
	
	
	/** default constructor */
	public ProAttach() {
		super();
	}
	/** full constructor */
	public ProAttach(int attachId, String proStage, String attachAddress,
			String createOper, String fileName) {
		super();
		this.attachId = attachId;
		this.proStage = proStage;
		this.attachAddress = attachAddress;
		this.createOper = createOper;
		this.fileName = fileName;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="attachId",unique = true, nullable = false)
	public int getAttachId() {
		return attachId;
	}
	public void setAttachId(int attachId) {
		this.attachId = attachId;
	}
	@Column(name="proStage",length=50)
	public String getProStage() {
		return proStage;
	}
	public void setProStage(String proStage) {
		this.proStage = proStage;
	}
	@Column(name="attachAddress",length=50)
	public String getAttachAddress() {
		return attachAddress;
	}
	public void setAttachAddress(String attachAddress) {
		this.attachAddress = attachAddress;
	}
	@Column(name="createOper",length=50)
	public String getCreateOper() {
		return createOper;
	}
	public void setCreateOper(String createOper) {
		this.createOper = createOper;
	}
	@Column(name="fileName",length=50)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "ProAttach [attachId=" + attachId + ", proStage=" + proStage
				+ ", attachAddress=" + attachAddress + ", createOper="
				+ createOper + ", fileName=" + fileName + "]";
	}
}
