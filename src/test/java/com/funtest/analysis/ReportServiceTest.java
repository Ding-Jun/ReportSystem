package com.funtest.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.bean.SimpleReport;
import com.funtest.analysis.service.impl.ReportServiceImpl;
import com.funtest.core.bean.constant.Constants;
import com.funtest.core.bean.page.Page;
import com.google.gson.Gson;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ReportServiceTest {
	private static Logger logger=LoggerFactory.getLogger(ReportServiceTest.class);
	
	@Autowired
	ReportServiceImpl service;
	@Test
	public void testCreateReport() throws IOException {
		String configJson="{\"id\":1,\"dutNoColumnFlag\":\"(?i)Dut_NO\",\"limitMinLineFlag\":\"(?i)^(-1|Min),\",\"limitMaxLineFlag\":\"(?i)^(0|Max),\",\"dutPassTrueString\":\"(?i)TRUE\",\"dutPassFalseString\":\"(?i)FALSE\",\"dutPassColumnFlag\":\"(?i)Dut_Pass\",\"siteNoColumnFlag\":\"(?i)Site_No\",\"password\":\"joulwatt\",\"testItemColumnFlag\":\"(?i),(OS|PIN)\",\"ignoreColumnFlag\":\"(?i)_debug$\"}";
		String[] fileNames={"hello.txt","JW7707.csv","JW7707v2.csv","rubbish.csv"};
		List<InputStream> ins=new ArrayList<InputStream>();
		for(String fileName:fileNames){
			Resource resource=new ClassPathResource("file/"+fileName);
			InputStream in = resource.getInputStream();
			ins.add(in);
		}
		int size = ins.size();
		long start,end;
		//build DataInfo
		start=System.currentTimeMillis();
		DataInfo dataInfo = service.createDataInfo(fileNames, (InputStream[])ins.toArray(new InputStream[size]),"report1212", "JW7878",  Constants.PROCESS_MODE_NORMAL);
		end=System.currentTimeMillis();
		logger.info("testBuildDataInfo() use time: {} ms",end-start);
		
		//build report
		start=System.currentTimeMillis();
		Integer id=service.createReport(dataInfo);
		end=System.currentTimeMillis();
		logger.info("testCreateReport() id:{}. use {}ms.",id ,end-start);
		
	}

	@Test
	public void deleteReport(){
		service.deleteReport(1);
	}
	
	@Test
	public void queryPage(){
		Page<SimpleReport> page= service.queryPage(1, 2);
		logger.info("queryPage() page: \n{}",new Gson().toJson(page));
	}
	
	@Test
	public void queryReport(){
		Report report = service.queryReport(2);
		//logger.info("null?{}",report==null);
		logger.info("queryReport() Report: id：{},name: '{}'",report.getId(),report.getReportname());
	}
}
