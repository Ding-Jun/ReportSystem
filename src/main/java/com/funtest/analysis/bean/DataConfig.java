package com.funtest.analysis.bean;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_dataConfig")
public class DataConfig {
	private int id;
	private String dutNoColumnFlag ="Dut_No";
	private String limitMinLineFlag="^(-1|Min),";
	private String limitMaxLineFlag="^(0|Max),";
	private String dutPassTrueString="TRUE";
	private String dutPassFalseString="FALSE";
	private String dutPassColumnFlag="Dut_Pass";
	private String siteNoColumnFlag="Site_No";
	private String password="joulwatt";
	private String testItemColumnFlag=",(OS|PIN)";
	
	
	@Column(name="id")
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column
	public String getDutNoColumnFlag() {
		return dutNoColumnFlag;
	}
	public void setDutNoColumnFlag(String dutNoColumnFlag) {
		this.dutNoColumnFlag = dutNoColumnFlag;
	}
	
	@Column
	public String getLimitMinLineFlag() {
		return limitMinLineFlag;
	}
	public void setLimitMinLineFlag(String limitMinLineFlag) {
		this.limitMinLineFlag = limitMinLineFlag;
	}
	
	@Column
	public String getLimitMaxLineFlag() {
		return limitMaxLineFlag;
	}
	public void setLimitMaxLineFlag(String limitMaxLineFlag) {
		this.limitMaxLineFlag = limitMaxLineFlag;
	}
	
	@Column
	public String getDutPassTrueString() {
		return dutPassTrueString;
	}
	public void setDutPassTrueString(String dutPassTrueString) {
		this.dutPassTrueString = dutPassTrueString;
	}
	
	@Column
	public String getDutPassFalseString() {
		return dutPassFalseString;
	}
	public void setDutPassFalseString(String dutPassFalseString) {
		this.dutPassFalseString = dutPassFalseString;
	}
	
	
	@Column
	public String getDutPassColumnFlag() {
		return dutPassColumnFlag;
	}
	public void setDutPassColumnFlag(String dutPassColumnFlag) {
		this.dutPassColumnFlag = dutPassColumnFlag;
	}
	
	@Column
	public String getSiteNoColumnFlag() {
		return siteNoColumnFlag;
	}
	public void setSiteNoColumnFlag(String siteNoColumnFlag) {
		this.siteNoColumnFlag = siteNoColumnFlag;
	}
	
	@Column
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column
	public String getTestItemColumnFlag() {
		return testItemColumnFlag;
	}
	public void setTestItemColumnFlag(String testItemColumnFlag) {
		this.testItemColumnFlag = testItemColumnFlag;
	}

}
