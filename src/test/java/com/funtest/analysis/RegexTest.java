package com.funtest.analysis;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

public class RegexTest {

	@Test
	public void test() {
		Pattern patternDutNoColumn = Pattern.compile("Dut_No");
		System.out.println("true?"+patternDutNoColumn.matcher("Dut_No,Site_No,Dut_Pass,SW_Bin,Data_Num,OS_VCC,OS_SW,Vf,Isw,Ivcc,Vcc,Vcc1,Icc_5V,Icc_1V,Icc3,Vcc_on,Vcc_off,SW_Freq_debu").find());
	
		Pattern patternLimit=Pattern.compile("(?i)^(-1|Min),|(?i)^(0|Max),");
		System.out.println("true?"+patternLimit.matcher("-1,-1,TRUE,-1,-1,-0.9,-0.9,-0.7,40,220,4.33,7,-37,-26,69,3.75,3.5,-9999.9999,2.1,-0.001,1,68").find());
		
	}

}
