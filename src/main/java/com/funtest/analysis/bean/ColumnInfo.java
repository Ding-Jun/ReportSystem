package com.funtest.analysis.bean;

public class ColumnInfo {
	private Integer id;//第几列
	private String columnName;//列名称
	private Double limitMax;//判限最大值
	private Double limitMin;//判限最小值
	private String limitUnit;//单位
	private Double realMaxInLimit;//判限内的实际最大值
	private Double realMinInLimit;//判限内的实际最小值
	private Double realAverage;//实际平均值 
	private Double realAverageInLimit;//判限内的实际平均值  暂时不用
	private Double realMaxOutOfLimit;//判限外的实际最大值
	private Double realMinOutOfLimit;//判限外的实际最小值
	private Boolean isProcess;//是否要处理该列
	private Integer passGroups;//pass的分组数量
	private Integer failGroups;//fail的分组数量
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Double getLimitMax() {
		return limitMax;
	}
	public void setLimitMax(Double limitMax) {
		this.limitMax = limitMax;
	}
	public Double getLimitMin() {
		return limitMin;
	}
	public void setLimitMin(Double limitMin) {
		this.limitMin = limitMin;
	}
	public String getLimitUnit() {
		return limitUnit;
	}
	public void setLimitUnit(String limitUnit) {
		this.limitUnit = limitUnit;
	}
	public Double getRealMaxInLimit() {
		return realMaxInLimit;
	}
	public void setRealMaxInLimit(Double realMaxInLimit) {
		this.realMaxInLimit = realMaxInLimit;
	}
	public Double getRealMinInLimit() {
		return realMinInLimit;
	}
	public void setRealMinInLimit(Double realMinInLimit) {
		this.realMinInLimit = realMinInLimit;
	}
	public Double getRealAverage() {
		return realAverage;
	}
	public void setRealAverage(Double realAverage) {
		this.realAverage = realAverage;
	}
	public Double getRealAverageInLimit() {
		return realAverageInLimit;
	}
	public void setRealAverageInLimit(Double realAverageInLimit) {
		this.realAverageInLimit = realAverageInLimit;
	}
	public Double getRealMaxOutOfLimit() {
		return realMaxOutOfLimit;
	}
	public void setRealMaxOutOfLimit(Double realMaxOutOfLimit) {
		this.realMaxOutOfLimit = realMaxOutOfLimit;
	}
	public Double getRealMinOutOfLimit() {
		return realMinOutOfLimit;
	}
	public void setRealMinOutOfLimit(Double realMinOutOfLimit) {
		this.realMinOutOfLimit = realMinOutOfLimit;
	}
	public Boolean getIsProcess() {
		return isProcess;
	}
	public void setIsProcess(Boolean isProcess) {
		this.isProcess = isProcess;
	}
	public Integer getPassGroups() {
		return passGroups;
	}
	public void setPassGroups(Integer passGroups) {
		this.passGroups = passGroups;
	}
	public Integer getFailGroups() {
		return failGroups;
	}
	public void setFailGroups(Integer failGroups) {
		this.failGroups = failGroups;
	}
}
