package com.teng.d_processVariables;

import java.io.Serializable;

public class Person implements Serializable  {
	/**
	 * 版本信息
	 */
	private static final long serialVersionUID = -1426484919423478323L;
	
	private Integer id;//编号
	private String name;//姓名
	private String education;//教育
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
}
