package com.funtest.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import com.funtest.analysis.service.impl.ReportServiceImpl;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ReportServiceTest {
	private static Logger logger=LoggerFactory.getLogger(ReportServiceTest.class);
	
	@Autowired
	ReportServiceImpl service;
	@Test
	public void testExportDataInfo() {
		
		InputStream in1=null;
		InputStream in2=null;
		InputStream in3=null;
		try {
			System.out.println("系统路径："+ System.getProperty("java.class.path"));//系统的classpaht路径
			Resource resource1 = new ClassPathResource("file/hello.txt"); 
			Resource resource2 = new ClassPathResource("file/JW7707.csv"); 
			Resource resource3 = new ClassPathResource("file/rubbish.csv"); 
			String fileName1 = resource1.getFilename();
			String fileName2 = resource2.getFilename();
			String fileName3 = resource3.getFilename();
		    
			 in1 = resource1.getInputStream();
			 in2 = resource2.getInputStream();
			 in3 = resource3.getInputStream();
			String[] fileNames={fileName1,fileName2,fileName3};
			InputStream[] ins={in1,in2,in3};
			DataInfo dataInfo= service.createReportSnaps(fileNames, ins, "JW0909", "JW1111", Constants.PROCESS_MODE_NORMAL);
			System.out.println("dataInfo: "+ new Gson().toJson(dataInfo));
			for(InputStream in:ins){
				if(in !=null){
					in.close();
					
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public Report testGenerateReport(String[] fileNames,InputStream[] ins, DataInfo dataInfo) throws IOException{
		BufferedReader br= new BufferedReader(new InputStreamReader(ins[1]));
		System.out.println("RRRRRRRRead:"+br.readLine()+br.readLine());
		logger.info("测试项信息: {}",new Gson().toJson(dataInfo));
		return null;
	}
}
