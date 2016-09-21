package com.funtest.analysis.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.funtest.analysis.bean.ColumnInfo;
import com.funtest.analysis.bean.DataConfig;
import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.FileInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.dao.DataConfigDao;
import com.funtest.analysis.dao.ReportBuilder;
import com.funtest.analysis.service.ReportService;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;

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
	public DataInfo createDataInfo(String[] fileNames, InputStream[] ins,String reportName,String chipName,Integer mode){
		ReportBuilder rBuilder= new ReportBuilder();
		rBuilder.setDataConfig(configDao.queryDataConfig(1));
	}
	
}
