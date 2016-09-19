package com.funtest.analysis.exception;

public class TestItemLineNotMatchException extends AnalysisException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7421023319658999412L;

	public TestItemLineNotMatchException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TestItemLineNotMatchException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TestItemLineNotMatchException(String expect ,String actual) {
		super("TestItemLine not match:\n"+
				"expect was:"+expect+"\n"+
				"actually was:"+actual);
	}
	
}
