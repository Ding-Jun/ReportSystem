package com.funtest.analysis.exception;

/**
 * Analysis project
 * 通用业务异常类
 * @author admin
 *
 */
public class AnalysisException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8137685639089410649L;

 
	
	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AnalysisException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 不填充stack
	 */
	@Override
	public  Throwable fillInStackTrace() {
		return this;
	}
	

	
}
