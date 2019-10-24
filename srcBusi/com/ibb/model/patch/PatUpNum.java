package com.ibb.model.patch;

public class PatUpNum {

	private String upMan;
	private String patNum;

	public PatUpNum(String upMan, String patNum) {
		super();
		this.upMan = upMan;
		this.patNum = patNum;
	}

	public PatUpNum() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUpMan() {
		return upMan;
	}

	public void setUpMan(String upMan) {
		this.upMan = upMan;
	}

	public String getPatNum() {
		return patNum;
	}

	public void setPatNum(String patNum) {
		this.patNum = patNum;
	}

}
