package com.funtest.analysis.exception;

public class DataNotFoundException extends AnalysisException {

	private Integer code;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7110307112293900226L;
	
	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DataNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DataNotFoundException(String message,Integer code) {
		super( message+"\n错误代码："+code);
		this.code=code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
