package com.funtest.analysis.bean;

import java.util.List;

public class DataInfo {
	private String id;//uuid
	private String reportName;//报告名
	private String chipName;//芯片名
	private Integer mode;//处理模式 FT   or  FT&RT
	private List<ColumnInfo> columns;	//测试项字段信息
	private List<FileInfo> files;		//数据文件信息
	
	//辅助信息
	private String testItemStr;	//测试项所在行
	private String limitMinStr;//判限最小值所在行 字符串形式
	private String limitMaxStr;//判限最大值所在行 字符串形式
	private String limitUnitStr;//判限单位所在行 字符串形式
	private Integer dutPassCol;//dut_pass所在列
	private Integer indexCol;//dut_No所在列
	private Integer siteCol;//site_No所在列
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getChipName() {
		return chipName;
	}
	public void setChipName(String chipName) {
		this.chipName = chipName;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getTestItemStr() {
		return testItemStr;
	}
	public void setTestItemStr(String testItemStr) {
		this.testItemStr = testItemStr;
	}
	public String getLimitMinStr() {
		return limitMinStr;
	}
	public void setLimitMinStr(String limitMinStr) {
		this.limitMinStr = limitMinStr;
	}
	public String getLimitMaxStr() {
		return limitMaxStr;
	}
	public void setLimitMaxStr(String limitMaxStr) {
		this.limitMaxStr = limitMaxStr;
	}
	public String getLimitUnitStr() {
		return limitUnitStr;
	}
	public void setLimitUnitStr(String limitUnitStr) {
		this.limitUnitStr = limitUnitStr;
	}

	public Integer getDutPassCol() {
		return dutPassCol;
	}
	public void setDutPassCol(Integer dutPassCol) {
		this.dutPassCol = dutPassCol;
	}
	public Integer getIndexCol() {
		return indexCol;
	}
	public void setIndexCol(Integer indexCol) {
		this.indexCol = indexCol;
	}
	public Integer getSiteCol() {
		return siteCol;
	}
	public void setSiteCol(Integer siteCol) {
		this.siteCol = siteCol;
	}
	public List<ColumnInfo>  getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
	}
	public List<FileInfo>  getFiles() {
		return files;
	}
	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}
}
