package com.funtest.analysis.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class SimpleReport {
	Integer id;
	String reportName;
	String chipName;
	Boolean isDeleted;
	String lotNo;
	Integer mode;
	String passPercent;
	String sealNo;
	String srcFile;
	String testCount;
	String testMan;
	Date  time;
	Integer userId;
	
	@Column(name="id")
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	@Column
	public String getChipName() {
		return chipName;
	}
	public void setChipName(String chipName) {
		this.chipName = chipName;
	}
	
	@Column
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Column
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	@Column
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	@Column
	public String getPassPercent() {
		return passPercent;
	}
	public void setPassPercent(String passPercent) {
		this.passPercent = passPercent;
	}
	@Column
	public String getSealNo() {
		return sealNo;
	}
	public void setSealNo(String sealNo) {
		this.sealNo = sealNo;
	}
	@Column
	public String getSrcFile() {
		return srcFile;
	}
	public void setSrcFile(String srcFile) {
		this.srcFile = srcFile;
	}
	@Column
	public String getTestCount() {
		return testCount;
	}
	public void setTestCount(String testCount) {
		this.testCount = testCount;
	}
	@Column
	public String getTestMan() {
		return testMan;
	}
	public void setTestMan(String testMan) {
		this.testMan = testMan;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Column
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
