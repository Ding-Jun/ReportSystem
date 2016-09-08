package com.funtest.analysis.bean;

import java.util.List;

public class DataInfo {
	String id;//uuid
	String dataFiles;//数据文件名
	String reportName;//报告名
	String chipName;//芯片名
	Integer mode;//处理模式 FT   or  FT&RT
	List<ColumnInfo> columns;
	
	public class ColumnInfo{
		Integer id;//第几列
		String name;//列名称
		Double max;//判限最大值
		Double min;//判限最小值
		String unit;//单位
		Boolean isProcess;//是否要处理该列
	}
}
