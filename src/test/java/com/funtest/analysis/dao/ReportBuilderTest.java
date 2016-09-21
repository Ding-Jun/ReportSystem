package com.funtest.analysis.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.funtest.analysis.bean.DataConfig;
import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;

public class ReportBuilderTest {
	public Logger logger=LoggerFactory.getLogger(this.getClass());
	@Test
	public void testBuildDataInfo() throws IOException {
		ReportBuilder rb=new ReportBuilder();
		String configJson="{\"id\":1,\"dutNoColumnFlag\":\"(?i)Dut_NO\",\"limitMinLineFlag\":\"(?i)^(-1|Min),\",\"limitMaxLineFlag\":\"(?i)^(0|Max),\",\"dutPassTrueString\":\"(?i)TRUE\",\"dutPassFalseString\":\"(?i)FALSE\",\"dutPassColumnFlag\":\"(?i)Dut_Pass\",\"siteNoColumnFlag\":\"(?i)Site_No\",\"password\":\"joulwatt\",\"testItemColumnFlag\":\"(?i),(OS|PIN)\",\"ignoreColumnFlag\":\"(?i)_debug$\"}";
		String[] fileNames={"hello.txt","JW7707.csv","rubbish.csv"};
		List<InputStream> ins=new ArrayList<InputStream>();
		for(String fileName:fileNames){
			Resource resource=new ClassPathResource("file/"+fileName);
			InputStream in = resource.getInputStream();
			ins.add(in);
		}
		int size = ins.size();
		DataConfig dataConfig=new Gson().fromJson(configJson, DataConfig.class);
		rb.setDataConfig(dataConfig);
		long start,end;
		start=System.currentTimeMillis();
		rb.buildDataInfo(fileNames, (InputStream[])ins.toArray(new InputStream[size]),"report1212", "JW7878",  Constants.PROCESS_MODE_NORMAL);
		end=System.currentTimeMillis();
		logger.info("testBuildDataInfo() use time: {} ms",end-start);
	}

	@Test
	public void testBuildReport() throws IOException{
		ReportBuilder rb=new ReportBuilder();
		String configJson="{\"id\":1,\"dutNoColumnFlag\":\"(?i)Dut_NO\",\"limitMinLineFlag\":\"(?i)^(-1|Min),\",\"limitMaxLineFlag\":\"(?i)^(0|Max),\",\"dutPassTrueString\":\"(?i)TRUE\",\"dutPassFalseString\":\"(?i)FALSE\",\"dutPassColumnFlag\":\"(?i)Dut_Pass\",\"siteNoColumnFlag\":\"(?i)Site_No\",\"password\":\"joulwatt\",\"testItemColumnFlag\":\"(?i),(OS|PIN)\",\"ignoreColumnFlag\":\"(?i)_debug$\"}";
		String[] fileNames={"hello.txt","JW7707.csv","JW7707v2.csv","rubbish.csv"};
		List<InputStream> ins=new ArrayList<InputStream>();
		for(String fileName:fileNames){
			Resource resource=new ClassPathResource("file/"+fileName);
			InputStream in = resource.getInputStream();
			ins.add(in);
		}
		int size = ins.size();
		DataConfig dataConfig=new Gson().fromJson(configJson, DataConfig.class);
		rb.setDataConfig(dataConfig);
		long start,end;
		//build DataInfo
		start=System.currentTimeMillis();
		DataInfo dataInfo = rb.buildDataInfo(fileNames, (InputStream[])ins.toArray(new InputStream[size]),"report1212", "JW7878",  Constants.PROCESS_MODE_NORMAL);
		end=System.currentTimeMillis();
		logger.info("testBuildDataInfo() use time: {} ms",end-start);
		
		//build report
		start=System.currentTimeMillis();
		Report report=rb.buildReport(dataInfo);
		end=System.currentTimeMillis();
		String json=new Gson().toJson(report);
		String debugFile="upload/debug.txt";
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File(debugFile)));
		bw.write(json);
		bw.close();
		logger.info("testBuildReport() use time: {} ms",end-start);
	}
}
