package com.funtest.analysis.util;

public class CpkUtils {
	
	/**
	 * 制程准确度： Ca=(X-U)/(T/2) 
	 * @param limitMin 规格下限
	 * @param limitMax 规格上限
	 * @param average  取样数据的平均值
	 * @return Ca
	 */
	public static double calculateCa(double limitMin,double limitMax,double average){
		
		double T=Math.abs(limitMax-limitMin);
		double U=(limitMax+limitMin)/2.0;
		return (average-U)/(T/2.0);
		
	}
	/**
	 * 制程精密度：Cp=T/6σ
	 * @param limitMin
	 * @param limitMax
	 * @param sigma
	 * @return Cp
	 */
	public static double calculateCp(double limitMin,double limitMax,double sigma){
		double T=Math.abs(limitMax-limitMin);
		return T/(6*sigma);
		
	}
	/**
	 * 制程能力指数：Cpk=Cp(1-|Ca|) 
	 * @param cp 制程精密度
	 * @param ca 制程准确度
	 * @return cpk
	 */
	public static double calculateCpk(double cp,double ca){
		return cp*(1-Math.abs(ca));
		
	}
}
