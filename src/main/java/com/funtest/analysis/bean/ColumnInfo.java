package com.funtest.analysis.bean;

import java.math.BigDecimal;

public class ColumnInfo {
	private Integer id;//第几列
	private String columnName;//列名称
	private double limitMin;//判限最小值
	private double limitMax;//判限最大值
	private String limitUnit;//单位
	private double realMinInLimit;//判限内的实际最小值
	private double realMaxInLimit;//判限内的实际最大值
	private double realAverage;//实际平均值 
	private double realAverageInLimit;//判限内的实际平均值  暂时不用
	private double realMinOutOfLimit;//判限外的实际最小值
	private double realMaxOutOfLimit;//判限外的实际最大值
	private Boolean isProcess;//是否要处理该列
	private Integer passGroups;//pass的分组数量
	private Integer failGroups;//fail的分组数量
	private double totalValue;//总的值
	private long totalCountAll;//总的数量 =pass+fail
	private long totalCountInLimit;//Pass的数量
	private long totalCountOutOfLimit;//Fail的数量
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
	public double getLimitMax() {
		return limitMax;
	}
	public void setLimitMax(double limitMax) {
		this.limitMax = limitMax;
	}
	public double getLimitMin() {
		return limitMin;
	}
	public void setLimitMin(double limitMin) {
		this.limitMin = limitMin;
	}
	public String getLimitUnit() {
		return limitUnit;
	}
	public void setLimitUnit(String limitUnit) {
		this.limitUnit = limitUnit;
	}
	public double getRealMaxInLimit() {
		return realMaxInLimit;
	}
	public void setRealMaxInLimit(double realMaxInLimit) {
		this.realMaxInLimit = realMaxInLimit;
	}
	public double getRealMinInLimit() {
		return realMinInLimit;
	}
	public void setRealMinInLimit(double realMinInLimit) {
		this.realMinInLimit = realMinInLimit;
	}
	public double getRealAverage() {
		return realAverage;
	}
	public void setRealAverage(double realAverage) {
		this.realAverage = realAverage;
	}
	public double getRealAverageInLimit() {
		return realAverageInLimit;
	}
	public void setRealAverageInLimit(double realAverageInLimit) {
		this.realAverageInLimit = realAverageInLimit;
	}
	public double getRealMaxOutOfLimit() {
		return realMaxOutOfLimit;
	}
	public void setRealMaxOutOfLimit(double realMaxOutOfLimit) {
		this.realMaxOutOfLimit = realMaxOutOfLimit;
	}
	public double getRealMinOutOfLimit() {
		return realMinOutOfLimit;
	}
	public void setRealMinOutOfLimit(double realMinOutOfLimit) {
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
	public double getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}
	public long getTotalCountAll() {
		return totalCountAll;
	}
	public void setTotalCountAll(long totalCountAll) {
		this.totalCountAll = totalCountAll;
	}
	public long getTotalCountInLimit() {
		return totalCountInLimit;
	}
	public void setTotalCountInLimit(long totalCountInLimit) {
		this.totalCountInLimit = totalCountInLimit;
	}
	public long getTotalCountOutOfLimit() {
		return totalCountOutOfLimit;
	}
	public void setTotalCountOutOfLimit(long totalCountOutOfLimit) {
		this.totalCountOutOfLimit = totalCountOutOfLimit;
	}
}
