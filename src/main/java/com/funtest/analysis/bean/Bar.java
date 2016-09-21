package com.funtest.analysis.bean;

/**
 * Bar For Bar Chart
 * @author admin
 *
 */
public class Bar {
	private double axis;
	private String axisStr;
	private long height=0;
	public Bar(){
		super();
	}
	
	public long increase(){
		this.height=height+1;
		return height;
	}
	
	public Bar(double axis,long height){
		this.axis=axis;
		this.height=height;
	}
	public double getAxis() {
		return axis;
	}
	public void setAxis(double axis) {
		this.axis = axis;
	}
	public String getAxisStr() {
		return axisStr;
	}

	public void setAxisStr(String axisStr) {
		this.axisStr = axisStr;
	}

	public long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	
}
