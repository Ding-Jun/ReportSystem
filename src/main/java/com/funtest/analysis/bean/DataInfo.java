package com.funtest.analysis.bean;

import java.util.List;

public class DataInfo {
	String id;//uuid
	String dataFiles;//数据文件名
	String reportName;//报告名
	String chipName;//芯片名
	Integer mode;//处理模式 FT   or  FT&RT
	List<ColumnInfo> columns;
	String message;
	public class ColumnInfo{
		Integer id;//第几列
		String name;//列名称
		Double max;//判限最大值
		Double min;//判限最小值
		String unit;//单位
		Boolean isProcess;//是否要处理该列
		
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
		public Double getMax() {
			return max;
		}
		public void setMax(Double max) {
			this.max = max;
		}
		
		public void setMin(Double min) {
			this.min = min;
		}
		public Double getMin() {
			return min;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public Boolean getIsProcess() {
			return isProcess;
		}
		public void setIsProcess(Boolean isProcess) {
			this.isProcess = isProcess;
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDataFiles() {
		return dataFiles;
	}
	public void setDataFiles(String dataFiles) {
		this.dataFiles = dataFiles;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<ColumnInfo>  getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
	}
}
