package com.funtest.analysis.bean;

public class FileInfo {
	private Integer id;				//唯一标识符
	private String fileName;		//文件名
	private String  localFileName;	//预处理后存储在本地的文件名
	private String message;			//若未成功  这里包含具体的错误信息
	private Integer status;			//文件状态  初始化/没找到测试项行/没找到判限行/成功
	private Long DataStartLine;	//数据起始行
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getLocalFileName() {
		return localFileName;
	}
	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
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
	public Long getDataStartLine() {
		return DataStartLine;
	}
	public void setDataStartLine(Long dataStartLine) {
		DataStartLine = dataStartLine;
	}

}
