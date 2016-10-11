package com.funtest.analysis.controller;

import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.service.ReportService;
import com.funtest.core.bean.ReturnMsg;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;
import org.dom4j.io.OutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
@RequestMapping(value = "report")
public class ReportController {
	@Autowired
	ReportService service;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 用户表单上传处理
	 * @param files
	 * @param reportName
	 * @param chipName
	 * @param mode
	 * @return DataInfo
	 */
	@RequestMapping(value = "buildDataInfo")
	@ResponseBody
	public Object buildDataInfo(@RequestParam(value="files",required = true) MultipartFile[] files,
			@RequestParam(value = "reportName", required = true) String reportName,
			@RequestParam(value = "chipName", required = false) String chipName,
			@RequestParam(value = "mode", required = false) String mode) {
		Integer processMode=null;
		if(mode!=null){
			processMode=Constants.PROCESS_MODE_DELETE_FT_FAIL;
		}
		ReturnMsg rm = new ReturnMsg();
		try {
			String[] fileNames=new String[files.length];
			InputStream[] ins=new InputStream[files.length];
			for(int i=0;i<files.length;i++){
				fileNames[i]=files[i].getOriginalFilename();
				ins[i]=files[i].getInputStream();
			}
			DataInfo dataInfo = service.createDataInfo(fileNames, ins, reportName, chipName, processMode);
			rm.setData(dataInfo);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
	
	/**
	 * 创建报告
	 * @param dataInfo
	 * @return ReportId
	 */
	@RequestMapping(value="buildReport",method = RequestMethod.POST)
	@ResponseBody
	public Object buildReport(@RequestParam(value="dataInfo",required=true) String dataInfoStr){
		
		ReturnMsg rm = new ReturnMsg();
		try {
			DataInfo dataInfo=new Gson().fromJson(dataInfoStr,DataInfo.class);
			Integer id = service.createReport(dataInfo);
			rm.setData(id);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
	
	/**
	 * 删除报告  软删除
	 * @param id
	 * @return 
	 */
	@RequestMapping(value="deleteReport/{id}")
	@ResponseBody
	public Object deleteReport(@PathVariable(value="id")Integer id){
		ReturnMsg rm = new ReturnMsg();
		try {
			 service.deleteReport(id);
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
	
	/**
	 * 查询报告
	 * @param id
	 * @return report
	 */
	@RequestMapping(value="queryReport/{id}")
	@ResponseBody
	public Object queryReport(@PathVariable(value="id")Integer id){
		ReturnMsg rm = new ReturnMsg();
		try {
			 rm.setData(service.queryReport(id));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
	/**
	 * 分页查询
	 * @param id
	 * @return report
	 */
	@RequestMapping(value="queryPage/{curPage}")
	@ResponseBody
	public Object queryPage(@PathVariable(value="curPage")Integer curPage,
							@RequestParam(value="size",required=false)Integer pageSize){
		ReturnMsg rm = new ReturnMsg();
		try {
			 rm.setData(service.queryPage(curPage,pageSize));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
	
	@RequestMapping(value="isExists")
	@ResponseBody
	public Object isExists(@RequestParam(value="reportName",required=false)String reportName){
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setData(service.queryExists(reportName));
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
	@RequestMapping(value="downloadReport")

	public void downloadReport(@RequestParam("report")String reportJson,@RequestParam("type")String type,
								 final HttpServletResponse response){
		ReturnMsg rm = new ReturnMsg();
		try {
			/*
			File file = new File(Constants.FILE_UPLOAD_DIR+"/report.json");
			PrintWriter pw=new PrintWriter(new FileWriter(file));
			logger.info("to:{}",file.getAbsolutePath());
			pw.write(reportJson);
			pw.close();
			Report report=new Gson().fromJson(reportJson,Report.class);
			service.downloadReport(report,type,response.getOutputStream());
			rm.setCode(Constants.RETURN_MSG_SUCCESS);*/
			Report report=new Gson().fromJson(reportJson,Report.class);
			//set filename
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(report.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String time = sdf.format(calendar.getTime());
			String sourseFile="FT_Template.xml";
			String downFilename=time+"_FT_"+report.getFinalName()+"("+report.getChipName()+")_"+(int)Math.ceil(new Double(report.getTestCount())/1000.0)+"K.xml";

			response.setContentType("text/plain");
			response.setHeader("Location",downFilename);
			response.setHeader("Content-Disposition", "attachment; filename=" + downFilename);

			service.downloadReport(report,type,response.getOutputStream());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}

		//return rm;
	}

	@RequestMapping(value="test")
	@ResponseBody
	public Object test(){
		ReturnMsg rm = new ReturnMsg();
		try {
			rm.setCode(Constants.RETURN_MSG_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rm.setCode(Constants.RETURN_MSG_FAILURE);
			rm.setMessage(e.getMessage());
		}
		return rm;
	}
}
