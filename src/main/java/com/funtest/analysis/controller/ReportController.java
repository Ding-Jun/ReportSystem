package com.funtest.analysis.controller;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.service.ReportService;
import com.funtest.core.bean.ReturnMsg;
import com.funtest.core.bean.constant.Constants;

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
	
	/**
	 * 创建报告
	 * @param dataInfo
	 * @return ReportId
	 */
	@RequestMapping(value="buildReport")
	@ResponseBody
	public Object buildReport(@RequestParam(value="dataInfo",required=true)DataInfo dataInfo){
		ReturnMsg rm = new ReturnMsg();
		try {
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
