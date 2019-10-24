package com.ibb.model.score;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户积分实体类
 * ProScore entity. @author XieXiPeng
 * @date 2017年7月27日 上午10:00:13
 */
@Entity
@Table(name = "pro_score_rule")

public class ProScoreRule implements java.io.Serializable {

	// Fields

	private Integer ruleId;//主键
	private String type;//类型
	private Integer score;//对应的积分

	// Constructors

	/** default constructor */
	public ProScoreRule() {
	}

	/** minimal constructor */
	public ProScoreRule(String type) {
		this.type = type;
	}

	/** full constructor */
	public ProScoreRule(String type, Integer score) {
		this.type = type;
		this.score = score;
	}

	// Property accessors
	@Id
	@GeneratedValue

	@Column(name = "ruleId", unique = true, nullable = false)

	public Integer getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name = "type", nullable = false, length = 50)

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "score")

	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}