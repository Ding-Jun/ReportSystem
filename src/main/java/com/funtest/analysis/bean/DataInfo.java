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

	public DataInfo setId(String id) {
		this.id = id;
		return this;
	}

	public String getReportName() {
		return reportName;
	}

	public DataInfo setReportName(String reportName) {
		this.reportName = reportName;
		return this;
	}

	public String getChipName() {
		return chipName;
	}

	public DataInfo setChipName(String chipName) {
		this.chipName = chipName;
		return this;
	}

	public Integer getMode() {
		return mode;
	}

	public DataInfo setMode(Integer mode) {
		this.mode = mode;
		return this;
	}

	public List<ColumnInfo> getColumns() {
		return columns;
	}

	public DataInfo setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
		return this;
	}

	public List<FileInfo> getFiles() {
		return files;
	}

	public DataInfo setFiles(List<FileInfo> files) {
		this.files = files;
		return this;
	}

	public String getTestItemStr() {
		return testItemStr;
	}

	public DataInfo setTestItemStr(String testItemStr) {
		this.testItemStr = testItemStr;
		return this;
	}

	public String getLimitMinStr() {
		return limitMinStr;
	}

	public DataInfo setLimitMinStr(String limitMinStr) {
		this.limitMinStr = limitMinStr;
		return this;
	}

	public String getLimitMaxStr() {
		return limitMaxStr;
	}

	public DataInfo setLimitMaxStr(String limitMaxStr) {
		this.limitMaxStr = limitMaxStr;
		return this;
	}

	public String getLimitUnitStr() {
		return limitUnitStr;
	}

	public DataInfo setLimitUnitStr(String limitUnitStr) {
		this.limitUnitStr = limitUnitStr;
		return this;
	}

	public Integer getDutPassCol() {
		return dutPassCol;
	}

	public DataInfo setDutPassCol(Integer dutPassCol) {
		this.dutPassCol = dutPassCol;
		return this;
	}

	public Integer getIndexCol() {
		return indexCol;
	}

	public DataInfo setIndexCol(Integer indexCol) {
		this.indexCol = indexCol;
		return this;
	}

	public Integer getSiteCol() {
		return siteCol;
	}

	public DataInfo setSiteCol(Integer siteCol) {
		this.siteCol = siteCol;
		return this;
	}
}
