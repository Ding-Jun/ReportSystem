package com.funtest.analysis.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "t_reportItem")
public class ReportItem {
    private int id;
    private int col;
    private int testNo;
    private String columnName;
    private long failCount;
    private long passCount;
    private long totalCount;
    private String failRate = "0.0";
    private Integer rank;
    private double totalValue;//相对于 pass and fail
    private double realAverage;//相对于 pass and fail
    private double limitMin;
    private double limitMax;
    private String limitUnit;

    private Chart passChart;
    private Chart failChart;
    //private int reportId;
    private Boolean visible = Boolean.TRUE;
    private Boolean isDeleted = Boolean.FALSE;

    public ReportItem() {

    }

    public ReportItem(String columnName) {
        this.columnName = columnName;
    }

    public ReportItem(String columnName, String unit) {
        this.columnName = columnName;
        this.limitUnit = unit;
    }

    public ReportItem(int testNo, String columnName, String unit) {
        this.columnName = columnName;
        this.limitUnit = unit;
        this.testNo = testNo;
    }

    @Column(name = "id")
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column
    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Column
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    /*
	@Column
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}*/

    @Column
    public int getTestNo() {
        return testNo;
    }

    public void setTestNo(int testNo) {
        this.testNo = testNo;
    }

    @JoinColumn(name = "passChartId")
    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Chart getPassChart() {
        return passChart;
    }

    public void setPassChart(Chart passChart) {
        this.passChart = passChart;
    }

    @JoinColumn(name = "failChartId")
    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Chart getFailChart() {
        return failChart;
    }

    public void setFailChart(Chart failChart) {
        this.failChart = failChart;
    }

    @Column
    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    @Column
    public long getPassCount() {
        return passCount;
    }

    public void setPassCount(long passCount) {
        this.passCount = passCount;
    }

    @Column
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    @Column
    public String getFailRate() {
        return failRate;
    }

    public void setFailRate(String failRate) {
        this.failRate = failRate;
    }

    @Column
    public Integer getRank() {
        return rank;
    }

    public ReportItem setRank(Integer rank) {
        this.rank = rank;
        return this;
    }
    @Column
    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }


    @Column
    public double getRealAverage() {
        return realAverage;
    }

    public void setRealAverage(double realAverage) {
        this.realAverage = realAverage;
    }

    @Column
    public double getLimitMin() {
        return limitMin;
    }

    public void setLimitMin(double limitMin) {
        this.limitMin = limitMin;
    }

    @Column
    public double getLimitMax() {
        return limitMax;
    }

    public void setLimitMax(double limitMax) {
        this.limitMax = limitMax;
    }

    @Column
    public String getLimitUnit() {
        return limitUnit;
    }

    public void setLimitUnit(String limitUnit) {
        this.limitUnit = limitUnit;
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


}
