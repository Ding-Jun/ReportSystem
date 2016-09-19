package com.funtest.analysis.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.funtest.analysis.bean.ColumnInfo;
import com.funtest.analysis.bean.DataConfig;
import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.FileInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.exception.AnalysisException;
import com.funtest.analysis.exception.TestItemLineNotMatchException;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;

public class ReportBuilder {
	DataInfo dataInfo;
	DataConfig dataConfig;

	/**
	 * 根据ins解析出DataInfo  并删除false数据的多余部分将数据文件保存到本地
	 * @param fileNames 文件名  多个
	 * @param ins		InputStream 多个 与文件名个数相等
	 * @param chipName  芯片名 （可选）
	 * @param mode 		数据处理模式     FT  or  FT&RT
	 * @return DataInfo 包含了生成报告所需的所有信息，如 测试项名称，判限等
	 */
	public DataInfo buildDataInfo(String[] fileNames,InputStream[] ins,
										String reportName,String chipName,Integer mode){
		//todo
		//1.非空验证
		if(ins==null ||ins.length ==0){
			throw new AnalysisException("没有提供数据文件~");
		}
		if(StringUtils.isBlank(reportName)){
			throw new AnalysisException("报告名称未指定~");
		}
		if(StringUtils.isBlank(chipName)){
			chipName="undefined";
		}
		if(mode==null){
			mode=Constants.PROCESS_MODE_NORMAL;
		}
		if(fileNames.length != ins.length){
			throw new AnalysisException("文件数量与InputStream数量不一致~");
		}
		
		//2.填充 dataInfo
		DataInfo dataInfo= new DataInfo();
		dataInfo.setReportName(reportName);
		dataInfo.setMode(mode);
		dataInfo.setChipName(chipName);
		this.dataInfo=dataInfo;
		
		//填充
		this.doBuildDataInfo(fileNames, ins);
		
		System.out.println(new Gson().toJson(dataInfo));
		return dataInfo;
		
	}
	
	private DataInfo doBuildDataInfo(String[] fileNames,InputStream[] ins){
		if(dataInfo==null){
			throw new AnalysisException("DataInfo not set");
		}
		if(dataConfig==null){
			throw new AnalysisException("DataConfig not set");
		}
		
		String dir="upload";
		File dirFile=new File(dir);
		if(!dirFile.exists() && !dirFile.isDirectory()){
			dirFile.mkdir();
		}
		
		//各种判断的正则表达式
		Pattern patternDutNoColumn = Pattern.compile(dataConfig.getDutNoColumnFlag());
		Pattern patternSiteNoColumn = Pattern.compile(dataConfig.getSiteNoColumnFlag());
		Pattern patternDutPassColumn = Pattern.compile(dataConfig.getDutPassColumnFlag());
		Pattern patternTestItemColumnFlag = Pattern.compile(dataConfig.getTestItemColumnFlag());
		Pattern patternDutPassTrue = Pattern.compile(dataConfig.getDutPassTrueString());
		Pattern patternDutPassFalse = Pattern.compile(dataConfig.getDutPassFalseString());
		Pattern patternLimitMaxLine = Pattern.compile(dataConfig.getLimitMaxLineFlag());
		Pattern patternLimitMinLine = Pattern.compile(dataConfig.getLimitMinLineFlag());
		Pattern patternLimitLine=Pattern.compile(dataConfig.getLimitMinLineFlag()+"|"+dataConfig.getLimitMaxLineFlag());
		Pattern patternLimitUnitLine=Pattern.compile("^\\D");
				
		List<FileInfo> fileInfoList=new ArrayList<FileInfo>();
		List<ColumnInfo> columnInfoList=new ArrayList<ColumnInfo>();
		
		for(int i=0;i<ins.length;i++){
			//2.1填充FileInfo
			FileInfo fileInfo=new FileInfo();
			fileInfo.setFileName(fileNames[i]);
			
			if(!fileNames[i].endsWith(".csv")){
				//logger.info("{} skipped in createReportSnaps..",fileNames[i]);
				fileInfo.setStatus(Constants.PROCESS_STATUS_ERROR_FORMAT);
				fileInfo.setMessage("不是csv文件");
				fileInfoList.add(fileInfo);
				continue;
			}
			
			long lineNumber=0;//当前读取的行号
			String curLine=null;//当前行字符串
			BufferedReader br= new BufferedReader(new InputStreamReader(ins[i]));
			String localFileName=dir+"\\"+UUID.randomUUID().toString()+".csv";
			PrintWriter pw=null;
			try {
				pw=new PrintWriter( new BufferedWriter(new FileWriter(localFileName)));
				while((curLine = br.readLine() )!= null ){
					pw.println(curLine);
					lineNumber++;
					if(lineNumber >10){
						break;
					}
					System.out.println("line"+(lineNumber)+": "+lineNumber);
					//匹配测试项所在行成功
					if(patternDutNoColumn.matcher(curLine).find()
							&& patternDutPassColumn.matcher(curLine).find()
							&& patternSiteNoColumn.matcher(curLine).find()
							&& patternTestItemColumnFlag.matcher(curLine).find()){
						//logger.info("测试项所在行："+(lineNumber-1));
						
						//record
						recordTestItemStr(curLine);
						
						
						//开始查找limit
						String limitMinStr=null;
						String limitMaxStr=null;
						while((curLine = br.readLine() )!= null){
							pw.println(curLine);
							lineNumber++;
							if(limitMinStr==null || limitMaxStr==null){
								//匹配limit成功
								if(patternLimitLine.matcher(curLine).find()){
									if(limitMinStr==null 
											&& patternLimitMinLine.matcher(curLine).find()){
										limitMinStr=curLine;
										if(dataInfo.getLimitMinStr() == null){
											dataInfo.setLimitMinStr(curLine);
										}
										else{
											if(!dataInfo.getLimitMinStr().equals(curLine)){
												return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
											}
										}
									}
									if(limitMaxStr== null 
											&& patternLimitMaxLine.matcher(curLine).find()){
										limitMaxStr=curLine;
										if(dataInfo.getLimitMaxStr() == null){
											dataInfo.setLimitMaxStr(curLine);
										}
										else{
											if(!dataInfo.getLimitMaxStr().equals(curLine)){
												return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
											}
										}
									}
								}
								if(limitMinStr!=null && limitMaxStr !=null){
									
									//看看是否有单位行
									curLine = br.readLine();
									pw.println(curLine);
									lineNumber++;
									if(curLine !=null && patternLimitUnitLine.matcher(curLine).find()){
										if(dataInfo.getLimitUnitStr()==null){
											dataInfo.setLimitUnitStr(curLine);
										}
										else{
											if(!dataInfo.getLimitUnitStr().equals(curLine)){
												return (Constants.PROCESS_STATUS_UNIT_NOT_MATCH);
											}
										}
										fileInfo.setDataStartLine(lineNumber);
									}else{
										fileInfo.setDataStartLine(lineNumber-1);
									}
									break;
								}
							}
						}
						if(curLine==null){
							return (Constants.PROCESS_STATUS_CANT_FIND_LIMIT);
						}
					}
				}
				if(curLine==null){
					return (Constants.PROCESS_STATUS_CANT_FIND_TESTITEM);
				}
				
				//copy剩余部分
				if(!patternLimitUnitLine.matcher(curLine).find()){
				while((curLine = br.readLine() )!= null){
					
				}
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(TestItemLineNotMatchException e){
				fileInfo.setStatus(Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
				fileInfo.setMessage("测试项未匹配成功");
			}
			finally{
				if(pw !=null){
					pw.close();
				}
			}
			//2.2填充ColumnInfo
			fileInfo.setLocalFileName(localFileName);
			fileInfo.setStatus(Constants.PROCESS_STATUS_DONE);
			fileInfoList.add(fileInfo);
		}
		
		
		dataInfo.setFiles(fileInfoList);
		dataInfo.setColumns(columnInfoList);
		return dataInfo;
		
	}
	/**
	 * 根据dataInfo的信息处理数据文件，生成报告
	 * @param dataInfo  用户确认后的dataInfo
	 * @return Report 报告包含测试项 图表元数据
	 */
	public Report buildReport(DataInfo dataInfo){
		//todo
		return null;
		
	}
	private void recordTestItemStr(String str){
		if(dataInfo.getTestItemStr() == null){
			dataInfo.setTestItemStr(str);
		}
		else{
			if(!dataInfo.getTestItemStr().equals(str)){
				throw new TestItemLineNotMatchException(dataInfo.getTestItemStr(),str);
				//return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
			}
		}
	}
	private Boolean matchReportItem(String str,Pattern[] patterns){
		return null;
		
	}
	
	public DataConfig getDataConfig() {
		return dataConfig;
	}

	public void setDataConfig(DataConfig dataConfig) {
		this.dataConfig = dataConfig;
	}

	public DataInfo getDataInfo() {
		return dataInfo;
	}

	public void setDataInfo(DataInfo dataInfo) {
		this.dataInfo = dataInfo;
	}
}
