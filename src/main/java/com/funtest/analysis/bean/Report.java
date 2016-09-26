package com.funtest.analysis.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_report")
public class Report {
	private int id;
	//private int testNo;
	private String reportName="untitled";
	private String srcFile="";
	private Integer mode;
	//private String testItem;
	private List<ReportItem> reportItems=new ArrayList<ReportItem>();
	
	private Date time;
	
	//private int dataTableId;
	private User user;
	
	//private int userId;
	private Boolean isDeleted=Boolean.FALSE;

    private String title="成品测试报告";
    private String chipName="JPXXXX";
    private String finalName="JWXXX";
    private String testCount="total";
    private String lotNo="Lot No";
    private String sealNo="N/A";
    private String passPercent="%%%";
    private String testMan="Test Engineer";
    private String packageStyle="packageStyle";
    private String sufeng="/";
    private String circuitPropty="2";
    private String testPurpose="";
    private String testNote="";
    private String testResultAnalysis="";
    private String testSampleAnalysis="";
    private String failureAnalysis="";
    private boolean formalProgram=true;
    private String programName="";
    private String programVersion="";
    private String productSolution="全部入库";
    private String testManagerOpinion="";
    private String reportPreparedBy="";
    private String testManagerName="";
    private String testManagerDate="";
    private long osFailCount=0;
    private String osFailRate="";
	
	@Column(name="id")
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	/*
	@Column
	public String getTestItem() {
		return testItem;
	}
	public void setTestItem(String testItem) {
		this.testItem = testItem;
	}
	
*/

	@Column
	public String getSrcFile() {
		return srcFile;
	}
	public void setSrcFile(String srcFile) {
		this.srcFile = srcFile;
	}
	@Column
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	/*
	@Column
	public int getDataTableId() {
		return dataTableId;
	}
	public void setDataTableId(int dataTableId) {
		this.dataTableId = dataTableId;
	}

	@Column
	public int getUserId() {
		return dataTableId;
	}
	public void setUserId(int dataTableId) {
		this.dataTableId = dataTableId;
	}*/

	@ManyToOne
	@JoinColumn(name="userId")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Column
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Column
	public String getCircuitPropty() {
		return circuitPropty;
	}
	public void setCircuitPropty(String circuitPropty) {
		this.circuitPropty = circuitPropty;
	}
	/*
	@Column
	public int getTestNo() {
		return testNo;
	}
	public void setTestNo(int testNo) {
		this.testNo = testNo;
	}
	*/
	@OneToMany(fetch=FetchType.EAGER)
	@Cascade({CascadeType.ALL})
	@JoinColumn(name="reportId")
	public List<ReportItem> getReportItems() {
		return reportItems;
	}
	public void setReportItems(List<ReportItem> reportItems) {
		this.reportItems = reportItems;
	}
	
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column
	public String getChipName() {
		return chipName;
	}
	public void setChipName(String chipName) {
		this.chipName = chipName;
	}
	@Column
	public String getFinalName() {
		return finalName;
	}
	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}
	@Column
	public String getTestCount() {
		return testCount;
	}
	public void setTestCount(String testCount) {
		this.testCount = testCount;
	}
	@Column
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	@Column
	public String getSealNo() {
		return sealNo;
	}
	public void setSealNo(String sealNo) {
		this.sealNo = sealNo;
	}
	@Column
	public String getPassPercent() {
		return passPercent;
	}
	public void setPassPercent(String passPercent) {
		this.passPercent = passPercent;
	}
	@Column
	public String getTestMan() {
		return testMan;
	}
	public void setTestMan(String testMan) {
		this.testMan = testMan;
	}
	@Column
	public String getPackageStyle() {
		return packageStyle;
	}
	public void setPackageStyle(String packageStyle) {
		this.packageStyle = packageStyle;
	}
	@Column
	public String getSufeng() {
		return sufeng;
	}
	public void setSufeng(String sufeng) {
		this.sufeng = sufeng;
	}
	@Column
	public String getCurcuitPropty() {
		return circuitPropty;
	}
	public void setCurcuitPropty(String curcuitPropty) {
		this.circuitPropty = curcuitPropty;
	}
	@Column
	public String getTestPurpose() {
		return testPurpose;
	}
	public void setTestPurpose(String testPurpose) {
		this.testPurpose = testPurpose;
	}
	@Column
	public String getTestNote() {
		return testNote;
	}
	public void setTestNote(String testNote) {
		this.testNote = testNote;
	}
	@Column
	public String getTestResultAnalysis() {
		return testResultAnalysis;
	}
	public void setTestResultAnalysis(String testResultAnalysis) {
		this.testResultAnalysis = testResultAnalysis;
	}
	@Column
	public String getTestSampleAnalysis() {
		return testSampleAnalysis;
	}
	public void setTestSampleAnalysis(String testSampleAnalysis) {
		this.testSampleAnalysis = testSampleAnalysis;
	}
	@Column
	public String getFailureAnalysis() {
		return failureAnalysis;
	}
	public void setFailureAnalysis(String failureAnalysis) {
		this.failureAnalysis = failureAnalysis;
	}
	@Column
	public boolean isFormalProgram() {
		return formalProgram;
	}
	public void setFormalProgram(boolean formalProgram) {
		this.formalProgram = formalProgram;
	}
	@Column
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	@Column
	public String getProgramVersion() {
		return programVersion;
	}
	public void setProgramVersion(String programVersion) {
		this.programVersion = programVersion;
	}
	@Column
	public String getProductSolution() {
		return productSolution;
	}
	public void setProductSolution(String productSolution) {
		this.productSolution = productSolution;
	}
	@Column
	public String getTestManagerOpinion() {
		return testManagerOpinion;
	}
	
	public void setTestManagerOpinion(String testManagerOpinion) {
		this.testManagerOpinion = testManagerOpinion;
	}
	@Column	
	public String getReportPreparedBy() {
		return reportPreparedBy;
	}
	
	public void setReportPreparedBy(String reportPreparedBy) {
		this.reportPreparedBy = reportPreparedBy;
	}
	@Column
	public String getTestManagerName() {
		return testManagerName;
	}
	public void setTestManagerName(String testManagerName) {
		this.testManagerName = testManagerName;
	}
	@Column
	public String getTestManagerDate() {
		return testManagerDate;
	}
	public void setTestManagerDate(String testManagerDate) {
		this.testManagerDate = testManagerDate;
	}
	
	@Column
	public long getOsFailCount() {
		return osFailCount;
	}
	public void setOsFailCount(long osFailCount) {
		this.osFailCount = osFailCount;
	}
	
	@Column
	public String getOsFailRate() {
		return osFailRate;
	}
	public void setOsFailRate(String osFailRate) {
		this.osFailRate = osFailRate;
	}


}
