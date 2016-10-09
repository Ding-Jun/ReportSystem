package com.funtest.analysis.util;

public class CpkUtils {
	
	/**
	 * Cpl=(Average-LSL)/3σ
	 * @param limitMin 规格下限LSL
	 * @param average  取样数据的平均值
	 * @return Ca
	 */
	public static double calculateCpl(double limitMin, double average,double stdev){

		return (average-limitMin)/(3.0*stdev);
		
	}

	/**
	 * Cpu=(USL-Average)/3σ
	 * @param limitMax
	 * @param average
	 * @param stdev
	 * @return
	 */
	public static double calculateCpu(double limitMax, double average,double stdev){

		return (limitMax-average)/(3.0*stdev);

	}
	/**
	 * 制程精密度：Cp=T/6σ
	 * @param limitMin
	 * @param limitMax
	 * @param sigma
	 * @return Cp
	 */
	public static double calculateCp(double limitMin,double limitMax,double stdev){
		double T=Math.abs(limitMax-limitMin);
		return T/(6*stdev);
		
	}
	/**
	 * 制程能力指数：CPK= Min[ (USL- Mu)/3s, (Mu - LSL)/3s]
	 * @param cp 制程精密度
	 * @param ca 制程准确度
	 * @return cpk
	 */
	public static double calculateCpk(double cpu,double cpl){
		return Math.min(cpu,cpl);
		
	}
}
