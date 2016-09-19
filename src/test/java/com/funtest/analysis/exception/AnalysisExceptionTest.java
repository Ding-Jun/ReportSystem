package com.funtest.analysis.exception;

import static org.junit.Assert.*;

import org.junit.Test;

public class AnalysisExceptionTest {

	@Test
	public void testThrowException() {
		String message=null;
		try{
			throw new AnalysisException("test: some error");
		}catch(AnalysisException e){
			//e.printStackTrace();
			message=e.getMessage();
		}
		assertEquals(message,"test: some error" );
		
	}

}
