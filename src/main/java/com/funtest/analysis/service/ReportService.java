package com.funtest.analysis.service;

import java.io.IOException;
import java.io.InputStream;

import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;

public interface ReportService {
	public Report createReport(DataInfo dataInfo);
	public DataInfo createDataInfo(String[] fileNames, InputStream[] ins,String reportName,String chipName,Integer mode) throws IOException;
		
}
