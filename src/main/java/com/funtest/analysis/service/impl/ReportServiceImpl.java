package com.funtest.analysis.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.dao.DataConfigDao;
import com.funtest.analysis.dao.ReportBuilder;
import com.funtest.analysis.service.ReportService;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
	private static Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
	
	@Autowired 
	DataConfigDao configDao; 
	
	public Report createReport(DataInfo dataInfo){
		ReportBuilder rBuilder= new ReportBuilder();
		rBuilder.setDataConfig(configDao.queryDataConfig(1));
		Report report= rBuilder.buildReport(dataInfo);
		return report;
		
	}
	public DataInfo createDataInfo(String[] fileNames, InputStream[] ins,String reportName,String chipName,Integer mode) throws IOException{
		ReportBuilder rBuilder= new ReportBuilder();
		rBuilder.setDataConfig(configDao.queryDataConfig(1));
		return rBuilder.buildDataInfo(fileNames, ins, reportName, chipName, mode);
	}
	
}
