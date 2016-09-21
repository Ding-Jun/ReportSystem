package com.funtest.analysis.controller;

import java.io.InputStream;

import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.service.ReportService;
import com.funtest.core.bean.ReturnMsg;
import com.funtest.core.bean.constant.Constants;

@Controller
@RequestMapping(value = "report")
public class ReportController {
	@Autowired
	ReportService service;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "buildDataInfo")
	@ResponseBody
	public Object buildDataInfo(@RequestParam(value="files",required = true) MultipartFile[] files,
			@RequestParam(value = "reportName", required = true) String reportName,
			@RequestParam(value = "chipName", required = true) String chipName,
			@RequestParam(value = "mode", required = false) Integer mode) {

		ReturnMsg rm = new ReturnMsg();
		try {
			String[] fileNames=new String[files.length];
			InputStream[] ins=new InputStream[files.length];
			for(int i=0;i<files.length;i++){
				fileNames[i]=files[i].getOriginalFilename();
				ins[i]=files[i].getInputStream();
			}
			DataInfo dataInfo = service.createDataInfo(fileNames, ins, reportName, chipName, mode);
			rm.setData(dataInfo);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
	
	@RequestMapping(value="buildReport")
	@ResponseBody
	public Object buildReport(@RequestParam(value="dataInfo",required=true)DataInfo dataInfo){
		ReturnMsg rm = new ReturnMsg();
		try {
			Report report = service.createReport(dataInfo);
			rm.setData(report);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
}
