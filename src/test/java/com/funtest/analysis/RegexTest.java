package com.funtest.analysis;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

public class RegexTest {

	@Test
	public void test() {
		Pattern patternDutNoColumn = Pattern.compile("Dut_No");
		System.out.println("true?"+patternDutNoColumn.matcher("Dut_No,Site_No,Dut_Pass,SW_Bin,Data_Num,OS_VCC,OS_SW,Vf,Isw,Ivcc,Vcc,Vcc1,Icc_5V,Icc_1V,Icc3,Vcc_on,Vcc_off,SW_Freq_debu").find());
	}

}
