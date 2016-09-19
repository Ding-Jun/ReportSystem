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
		DataInfo dataInfo = new DataInfo();
		dataInfo.setReportName(reportName);
		dataInfo.setChipName(chipName);
		dataInfo.setMode(mode);
		dataInfo.setFiles(new ArrayList<com.funtest.analysis.bean.FileInfo>());
		DataConfig config= configDao.queryDataConfig(1);
		for(int i=0;i<fileNames.length;i++){
			//查找文件中的数据信息
			FileInfo fileInfo=new FileInfo();
			fileInfo.setFileName(fileNames[i]);
			if(!fileNames[i].endsWith(".csv")){
				logger.info("{} skipped in createReportSnaps..",fileNames[i]);
				fileInfo.setStatus(Constants.PROCESS_STATUS_ERROR_FORMAT);
				fileInfo.setMessage("不是csv文件");
				continue;
			}
			logger.info("start handle with {}",fileNames[i]);
			Integer status=_findFileInfo(dataInfo,fileInfo, ins[i], config);
			fileInfo.setStatus(status);
			dataInfo.getFiles().add(fileInfo);
			
			switch(status){
				case Constants.PROCESS_STATUS_DONE:
					break;
				case Constants.PROCESS_STATUS_ERROR_FORMAT:
					fileInfo.setMessage("未知异常 status仍是初始化状态");
					break;
				case Constants.PROCESS_STATUS_CANT_FIND_TESTITEM:
					fileInfo.setMessage("未知异常 status仍是初始化状态");
					break;
				case Constants.PROCESS_STATUS_CANT_FIND_LIMIT:
					fileInfo.setMessage("未找到测试项的判限");
					break;
				case Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH:
					fileInfo.setMessage("测试项未匹配成功");
					break;
				case Constants.PROCESS_STATUS_LIMIT_NOT_MATCH:
					fileInfo.setMessage("测试项的判限未匹配成功");
					break;
				default:
					fileInfo.setMessage("发生了未知异常");
					break;
			}
		}
		//查找测试项
		List<ColumnInfo> columns=_findColumnInfo(dataInfo, config);
		dataInfo.setColumns(columns);
		//3.返回测试项信息
		//logger.info("测试项信息: {}",new Gson().toJson(dataInfo));
		return dataInfo;
		
	}
	public Integer _findFileInfo(DataInfo dataInfo,FileInfo fileInfo ,InputStream in,DataConfig config){
		String dir="upload";
		File dirFile=new File(dir);
		if(!dirFile.exists() && !dirFile.isDirectory()){
			dirFile.mkdir();
		}
		
		System.out.println(new Gson().toJson(config));
		//各种判断的正则表达式
		Pattern patternDutNoColumn = Pattern.compile(config.getDutNoColumnFlag());
		Pattern patternSiteNoColumn = Pattern.compile(config.getSiteNoColumnFlag());
		Pattern patternDutPassColumn = Pattern.compile(config.getDutPassColumnFlag());
		Pattern patternTestItemColumnFlag = Pattern.compile(config.getTestItemColumnFlag());
		Pattern patternDutPassTrue = Pattern.compile(config.getDutPassTrueString());
		Pattern patternDutPassFalse = Pattern.compile(config.getDutPassFalseString());
		Pattern patternLimitMaxLine = Pattern.compile(config.getLimitMaxLineFlag());
		Pattern patternLimitMinLine = Pattern.compile(config.getLimitMinLineFlag());
		Pattern patternLimitLine=Pattern.compile(config.getLimitMinLineFlag()+"|"+config.getLimitMaxLineFlag());
		Pattern patternLimitUnitLine=Pattern.compile("^\\D");
		
		long line=0;
		String str=null;
		BufferedReader br= new BufferedReader(new InputStreamReader(in));
		String postFileName=dir+"\\"+UUID.randomUUID().toString()+".csv";
		PrintWriter pw=null;
		try {
			pw=new PrintWriter( new BufferedWriter(new FileWriter(postFileName)));
			while((str = br.readLine() )!= null ){
				pw.println(str);
				line++;
				if(line >10){
					break;
				}
				System.out.println("line"+(line)+": "+str);
				//匹配测试项所在行成功
				if(patternDutNoColumn.matcher(str).find()
						&& patternDutPassColumn.matcher(str).find()
						&& patternSiteNoColumn.matcher(str).find()
						&& patternTestItemColumnFlag.matcher(str).find()){
					logger.info("测试项所在行："+(line-1));
					
					if(dataInfo.getTestItemStr() == null){
						dataInfo.setTestItemStr(str);
					}
					else{
						if(!dataInfo.getTestItemStr().equals(str)){
							return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
						}
					}
					
					
					//开始查找limit
					String limitMinStr=null;
					String limitMaxStr=null;
					while((str = br.readLine() )!= null){
						pw.println(str);
						line++;
						if(limitMinStr==null || limitMaxStr==null){
							//匹配limit成功
							if(patternLimitLine.matcher(str).find()){
								if(limitMinStr==null 
										&& patternLimitMinLine.matcher(str).find()){
									limitMinStr=str;
									if(dataInfo.getLimitMinStr() == null){
										dataInfo.setLimitMinStr(str);
									}
									else{
										if(!dataInfo.getLimitMinStr().equals(str)){
											return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
										}
									}
								}
								if(limitMaxStr== null 
										&& patternLimitMaxLine.matcher(str).find()){
									limitMaxStr=str;
									if(dataInfo.getLimitMaxStr() == null){
										dataInfo.setLimitMaxStr(str);
									}
									else{
										if(!dataInfo.getLimitMaxStr().equals(str)){
											return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
										}
									}
								}
							}
							if(limitMinStr!=null && limitMaxStr !=null){
								
								//看看是否有单位行
								str = br.readLine();
								pw.println(str);
								line++;
								if(str !=null && patternLimitUnitLine.matcher(str).find()){
									if(dataInfo.getLimitUnitStr()==null){
										dataInfo.setLimitUnitStr(str);
									}
									else{
										if(!dataInfo.getLimitUnitStr().equals("str")){
											return (Constants.PROCESS_STATUS_UNIT_NOT_MATCH);
										}
									}
									fileInfo.setDataStartLine(line);
								}else{
									fileInfo.setDataStartLine(line-1);
								}
								break;
							}
						}
					}
					if(str==null){
						return (Constants.PROCESS_STATUS_CANT_FIND_LIMIT);
					}
				}
			}
			if(str==null){
				return (Constants.PROCESS_STATUS_CANT_FIND_TESTITEM);
			}
			
			//copy剩余部分
			if(!patternLimitUnitLine.matcher(str).find()){
			while((str = br.readLine() )!= null){
				
			}
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(pw !=null){
				pw.close();
			}
		}
		
		
		
		fileInfo.setLocalFileName(postFileName);
		return Constants.PROCESS_STATUS_DONE;
	}
	public List<ColumnInfo> _findColumnInfo(DataInfo dataInfo,DataConfig config){
		//各种判断的正则表达式
		Pattern patternTestItemColumnFlag = Pattern.compile(config.getTestItemColumnFlag().replace(",",""));
		Pattern patternIgnoreColumnFlag=Pattern.compile(config.getIgnoreColumnFlag());
				
		if(dataInfo.getTestItemStr() ==null 
				|| dataInfo.getLimitMinStr() ==null 
				|| dataInfo.getLimitMinStr() ==null){
			return null;
		}
		List<ColumnInfo> columns=new ArrayList<ColumnInfo>();
		Boolean osOcurred=false;
		String[] testItems=dataInfo.getTestItemStr().split(",");
		String[] limitMaxs=dataInfo.getLimitMaxStr().split(",");
		String[] limitMins=dataInfo.getLimitMinStr().split(",");
		String limitUnitStr=dataInfo.getLimitUnitStr();
		String[] limitUnits=limitUnitStr==null?new String[]{""}:limitUnitStr.split(",");
		
		System.out.println("limitUnits to String:"+Arrays.toString(limitUnits));
		for(int i=0;i<testItems.length;i++){
			double max,min;
			String unit;
			if(!osOcurred){
				if(patternTestItemColumnFlag.matcher(testItems[i]).find()){
					osOcurred=true;
				}
			}
			else{
				min=new Double((limitMins.length<i)?"-9999.9999":limitMins[i]);
				max=new Double((limitMaxs.length<i)?"-9999.9999":limitMaxs[i]);
				unit=(limitUnits.length<i)?"":limitUnits[i];
				ColumnInfo info=new ColumnInfo();
				info.setId(i);
				//不处理的策略
				info.setIsProcess(!patternIgnoreColumnFlag.matcher(testItems[i]).find());
				info.setLimitMin(min);
				info.setLimitMax(max);
				info.setLimitUnit(unit);
				info.setColumnName(testItems[i]);
				columns.add(info);
			}
		}
		return columns;
		
	}
}
