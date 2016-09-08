package com.funtest.analysis.service.impl;

import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.funtest.analysis.bean.Report;
import com.funtest.analysis.service.ReportService;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
	public Report createReport(InputStream in){
		return null;
		
	}
	public Report createReportSnaps(InputStream in){
		return null;
		
	}
}
