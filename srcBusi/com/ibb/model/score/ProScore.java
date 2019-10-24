package com.ibb.model.score;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户积分实体类
 * ProScore entity. @author XieXiPeng
 * @date 2017年7月19日 下午16:31:13
 */
@Entity
@Table(name = "pro_score")

public class ProScore implements java.io.Serializable {

	// Fields

	private Integer scoreId;//用户编号
	private Integer userId;//用户名字
	private Integer score;//用户积分

	// Constructors

	/** default constructor */
	public ProScore() {
	}

	/** full constructor */
	public ProScore(Integer userId, Integer score) {
		this.userId = userId;
		this.score = score;
	}

	// Property accessors
	@Id
	@GeneratedValue

	@Column(name = "scoreId", unique = true, nullable = false)

	public Integer getScoreId() {
		return this.scoreId;
	}

	public void setScoreId(Integer scoreId) {
		this.scoreId = scoreId;
	}

	@Column(name = "userId", length = 50)

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "score")

	public Integer getScore() {
		return this.score;
	}

	

	public void setScore(Integer score) {
		this.score = score;
	}

}