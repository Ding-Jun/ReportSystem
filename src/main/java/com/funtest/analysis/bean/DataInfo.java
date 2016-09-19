package com.funtest.analysis.bean;

import java.util.List;

public class DataInfo {
	private String id;//uuid
	private String reportName;//报告名
	private String chipName;//芯片名
	private Integer mode;//处理模式 FT   or  FT&RT
	private List<ColumnInfo> columns;
	
	private List<FileInfo> files;
	private String testItemStr;
	private String limitMinStr;
	private String limitMaxStr;
	private String limitUnitStr;
	
	public class FileInfo{
		private String fileName;
		private String  postFileName;
		private String message;
		private Integer status;
		private Integer rawDataLine;
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getPostFileName() {
			return postFileName;
		}
		public void setPostFileName(String postFileName) {
			this.postFileName = postFileName;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public Integer getRawDataLine() {
			return rawDataLine;
		}
		public void setRawDataLine(Integer rawDataLine) {
			this.rawDataLine = rawDataLine;
		}
	}
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
