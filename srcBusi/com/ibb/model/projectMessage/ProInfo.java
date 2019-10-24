package com.ibb.model.projectMessage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author WangGangWei
 * @date 2017年4月20日 下午2:08:29
 *
 */
@Entity
@Table(name = "pro_info")
public class ProInfo implements java.io.Serializable {

	// Fields

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Integer proId;
	private String proNum;
	private String proPactNum;
	private String proName;
	private String proIndu;
	private String proProductLine;
	private String proDis;
	private String startTime;
	private String finishTime;
	private String finishTime2;
	private String currentStage;
	private String custName;
	private String custConnect;
	private String phone;
	private String deliMode;
	private String createTime;
	private String createOper;
	private String comment;
	private Integer state;

	// Constructors

	/** default constructor */
	public ProInfo() {
	}

	/** minimal constructor */
	public ProInfo(String proNum, String proPactNum, String proName) {
		this.proNum = proNum;
		this.proPactNum = proPactNum;
		this.proName = proName;
	}

	/** full constructor */
	public ProInfo(Integer proId, String proNum, String proPactNum, String proName, String proIndu,
			String proProductLine, String proDis, String startTime, String finishTime, String finishTime2,
			String currentStage, String custName, String custConnect, String phone, String deliMode, String createTime,
			String createOper, String comment,Integer state) {
		super();
		this.proId = proId;
		this.proNum = proNum;
		this.proPactNum = proPactNum;
		this.proName = proName;
		this.proIndu = proIndu;
		this.proProductLine = proProductLine;
		this.proDis = proDis;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.finishTime2 = finishTime2;
		this.currentStage = currentStage;
		this.custName = custName;
		this.custConnect = custConnect;
		this.phone = phone;
		this.deliMode = deliMode;
		this.createTime = createTime;
		this.createOper = createOper;
		this.comment = comment;
		this.state=state;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "proId", unique = true, nullable = false)
	public Integer getProId() {
		return this.proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	@Column(name = "proNum", nullable = false, length = 100)
	public String getProNum() {
		return this.proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	@Column(name = "proPactNum", nullable = false, length = 100)
	public String getProPactNum() {
		return this.proPactNum;
	}

	public void setProPactNum(String proPactNum) {
		this.proPactNum = proPactNum;
	}

	@Column(name = "proName", nullable = false, length = 50)
	public String getProName() {
		return this.proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	@Column(name = "proIndu", length = 20)
	public String getProIndu() {
		return this.proIndu;
	}

	public void setProIndu(String proIndu) {
		this.proIndu = proIndu;
	}

	@Column(name = "proProductLine", length = 50)
	public String getProProductLine() {
		return this.proProductLine;
	}

	public void setProProductLine(String proProductLine) {
		this.proProductLine = proProductLine;
	}

	@Column(name = "proDis", length = 200)
	public String getProDis() {
		return this.proDis;
	}

	public void setProDis(String proDis) {
		this.proDis = proDis;
	}

	@Column(name = "startTime", length = 20)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "finishTime", length = 20)
	public String getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "custName", length = 40)
	public String getCustName() {
		return this.custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	@Column(name = "custConnect", length = 100)
	public String getCustConnect() {
		return this.custConnect;
	}

	public void setCustConnect(String custConnect) {
		this.custConnect = custConnect;
	}

	@Column(name = "phone", length = 50)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "deliMode", length = 20)
	public String getDeliMode() {
		return this.deliMode;
	}

	public void setDeliMode(String deliMode) {
		this.deliMode = deliMode;
	}

	@Column(name = "createTime", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "createOper", length = 20)
	public String getCreateOper() {
		return this.createOper;
	}

	public void setCreateOper(String createOper) {
		this.createOper = createOper;
	}

	@Column(name = "comment", length = 200)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "finishTime2", length = 20)
	public String getFinishTime2() {
		return finishTime2;
	}

	public void setFinishTime2(String finishTime2) {
		this.finishTime2 = finishTime2;
	}

	@Column(name = "currentStage", length = 30)
	public String getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}
	@Column(name = "state", length = 2)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	

}