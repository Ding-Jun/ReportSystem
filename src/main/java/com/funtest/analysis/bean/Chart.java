package com.funtest.analysis.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_chart")
public class Chart {
	private Integer id;
	private String title;
	private String datas="";//柱状图数据Series
	private List<Bar> bars;//柱状图柱子
	private Double spacing;//柱状图柱子间距
	private Double realMax;
	private Double realMin;
	
	private Integer groupCnt=500;			//分组数  
	private Double limitMin;
	private Double limitMax;
	private Double rangeMin;
	private Double rangeMax;
	
	private long totalCnt=0;				//该测试项Pass/Fail总颗数   即样本数
	private long quantityMax=0;			//数量最多的柱子
	
	private String chartImg="no img";	//fomat:base64
	private Integer chartType;
	private Boolean visible=Boolean.TRUE;
	private Boolean isDeleted=Boolean.FALSE;
	public Chart(){
		
	}

	public Chart(Integer chartType,Integer groupCnt){
		this.chartType=chartType;
		this.groupCnt=groupCnt;
	}
	@Column(name="id")
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Column(length=65535)
	public String getDatas() {
		return datas;
	}
	
	
	public void setDatas(String datas) {
		this.datas = datas;
	}
	
	@Transient
	public List<Bar> getBars() {
		return bars;
	}

	public void setBars(List<Bar> bars) {
		this.bars = bars;
	}

	@Column
	public Double getSpacing() {
		return spacing;
	}

	public void setSpacing(Double spacing) {
		this.spacing = spacing;
	}

	@Column
	public Double getRealMax() {
		return realMax;
	}
	public void setRealMax(Double realMax) {
		this.realMax = realMax;
	}
	
	@Column
	public Double getRealMin() {
		return realMin;
	}
	public void setRealMin(Double realMin) {
		this.realMin = realMin;
	}
	
	@Column
	public Integer getGroupCnt() {
		return groupCnt;
	}
	public void setGroupCnt(Integer groupCnt) {
		this.groupCnt = groupCnt;
	}
	
	@Column
	public Double getLimitMin() {
		return limitMin;
	}
	public void setLimitMin(Double limitMin) {
		this.limitMin = limitMin;
	}
	
	@Column
	public Double getLimitMax() {
		return limitMax;
	}
	public void setLimitMax(Double limitMax) {
		this.limitMax = limitMax;
	}
	
	@Column
	public Double getRangeMin() {
		return rangeMin;
	}

	public void setRangeMin(Double rangeMin) {
		this.rangeMin = rangeMin;
	}
	
	@Column
	public Double getRangeMax() {
		return rangeMax;
	}

	public void setRangeMax(Double rangeMax) {
		this.rangeMax = rangeMax;
	}



	
	@Column
	public long getQuantityMax() {
		return quantityMax;
	}
	public void setQuantityMax(long quantityMax) {
		this.quantityMax = quantityMax;
	}
	

	
	@Column
	public Integer getChartType() {
		return chartType;
	}
	public void setChartType(Integer chartType) {
		this.chartType = chartType;
	}
	@Column
	public long getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(long totalCnt) {
		this.totalCnt = totalCnt;
	}

	@Column(length=655350)
	public String getChartImg() {
		return chartImg;
	}

	public void setChartImg(String chartImg) {
		this.chartImg = chartImg;
	}
	
	@Column
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	@Column
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * 设置数据范围
	 * @param rangeMin
	 * @param rangeMax
	 */
	public void setLimit(Double limitMin,Double limitMax){
		//swap
		if(limitMin> limitMax){
			Double temp=limitMin;
			limitMin=limitMax;
			limitMax=temp;
		}
		this.limitMax=limitMax;
		this.limitMin=limitMin;
	}
	

	
}
