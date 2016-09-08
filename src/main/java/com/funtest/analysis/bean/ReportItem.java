package com.funtest.analysis.bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_reportItem")
public class ReportItem {
	private int id;
	private int testNo;
	private String columnname;	
	private int failCount=3344;
	private String failRate="-1%";
	private double limitMin=-1;
	private double limitMax=-1;
	private String limitUnit="?";
	private Chart passChart;
	private Chart failChart;
	//private int reportId;
	private Boolean visible;
	private Boolean isDeleted;
	
	public ReportItem(){
		
	}
	public ReportItem(String columnname){
		this.columnname=columnname;
	}
	
	public ReportItem(String columnname,String unit){
		this.columnname=columnname;
		this.limitUnit=unit;
	}
	
	public ReportItem(int testNo,String columnname,String unit){
		this.columnname=columnname;
		this.limitUnit=unit;
		this.testNo=testNo;
	}
	
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
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	/*
	@Column
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}*/
	
	@Column
	public int getTestNo() {
		return testNo;
	}
	public void setTestNo(int testNo) {
		this.testNo = testNo;
	}
	@JoinColumn(name="passChartId")
	@OneToOne(optional = true, cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	public Chart getPassChart() {
		return passChart;
	}

	public void setPassChart(Chart passChart) {
		this.passChart = passChart;
	}
	@JoinColumn(name="failChartId")
	@OneToOne(optional = true, cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	public Chart getFailChart() {
		return failChart;
	}
	public void setFailChart(Chart failChart) {
		this.failChart = failChart;
	}
	
	@Column
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	
	@Column
	public String getFailRate() {
		return failRate;
	}
	public void setFailRate(String failRate) {
		this.failRate = failRate;
	}
	
	@Column
	public double getLimitMin() {
		return limitMin;
	}
	public void setLimitMin(double limitMin) {
		this.limitMin = limitMin;
	}
	@Column
	public double getLimitMax() {
		return limitMax;
	}
	public void setLimitMax(double limitMax) {
		this.limitMax = limitMax;
	}
	@Column
	public String getLimitUnit() {
		return limitUnit;
	}
	public void setLimitUnit(String limitUnit) {
		this.limitUnit = limitUnit;
	}
	
	@Column
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
	@Column
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	
}
