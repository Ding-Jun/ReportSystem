package com.funtest.analysis.service;

import java.io.IOException;
import java.io.InputStream;

import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.bean.SimpleReport;
import com.funtest.core.bean.page.Page;
import com.funtest.core.bean.page.PageCondition;

public interface ReportService {
	public Integer createReport(DataInfo dataInfo);
	public DataInfo createDataInfo(String[] fileNames, InputStream[] ins,String reportName,String chipName,Integer mode) throws IOException;
	public void deleteReport(Integer id);
	public Report queryReport(Integer id);
	public Page<SimpleReport> queryPage(Integer curPage,Integer pageSize);
	public boolean queryExists(String reportName);
}
