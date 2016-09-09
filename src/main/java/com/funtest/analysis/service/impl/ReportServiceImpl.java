package com.funtest.analysis.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.funtest.analysis.bean.DataConfig;
import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.dao.DataConfigDao;
import com.funtest.analysis.bean.DataInfo.ColumnInfo;
import com.funtest.analysis.service.ReportService;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
	private static Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
	
	@Autowired 
	DataConfigDao configDao; 
	
	public Report createReport(InputStream in){
		return null;
		
	}
	public DataInfo createReportSnaps(String[] fileNames, InputStream[] ins,String reportName,String chipName,Integer mode){
		
		//1.非空验证
		if(ins==null ||ins.length ==0){
			throw new RuntimeException("没有提供数据文件~");
		}
		if(StringUtils.isBlank(reportName)){
			throw new RuntimeException("报告名称未指定~");
		}
		if(StringUtils.isBlank(chipName)){
			chipName="undefined";
		}
		if(mode==null){
			mode=Constants.PROCESS_MODE_NORMAL;
		}
		
		//2.找到数据文件里的测试项信息
		StringBuilder message=new StringBuilder();
		List<ColumnInfo> columns=new ArrayList<DataInfo.ColumnInfo>();
		for(int i=0;i<fileNames.length;i++){
			if(!fileNames[i].endsWith(".csv")){
				logger.info("{} skipped in createReportSnaps..",fileNames[i]);
				message.append(fileNames[i]+"不是csv文件,");
				continue;
				
			}
			logger.info("start handle with {}",fileNames[i]);
			
			Integer line=0;
			String str=null;
			BufferedReader br= new BufferedReader(new InputStreamReader(ins[i]));
			try {
				while((str = br.readLine() )!= null){
					if(line >10){
						break;
					}
					System.out.println("line"+(line++)+": "+str);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(str==null){
				message.append(fileNames[i]+"中未找到测试项,");
			}
		}
		
		//3.返回测试项信息
		DataInfo dataInfo = new DataInfo();
		dataInfo.setReportName(reportName);
		dataInfo.setChipName(chipName);
		dataInfo.setMode(mode);
		dataInfo.setDataFiles(StringUtils.join(fileNames, ","));
		dataInfo.setMessage(message.toString());
		logger.info("测试项信息: {}",new Gson().toJson(dataInfo));
		return dataInfo;
		
	}
	
	
}
