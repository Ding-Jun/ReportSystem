package com.funtest.analysis.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.collections.functors.FalsePredicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import com.funtest.analysis.bean.Bar;
import com.funtest.analysis.bean.Chart;
import com.funtest.analysis.bean.ColumnInfo;
import com.funtest.analysis.bean.DataConfig;
import com.funtest.analysis.bean.DataInfo;
import com.funtest.analysis.bean.FileInfo;
import com.funtest.analysis.bean.Report;
import com.funtest.analysis.bean.ReportItem;
import com.funtest.analysis.exception.AnalysisException;
import com.funtest.analysis.exception.DataNotFoundException;
import com.funtest.analysis.exception.LimitLineNotMatchException;
import com.funtest.analysis.exception.TestItemLineNotMatchException;
import com.funtest.analysis.util.CpkUtils;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;
import com.mysql.jdbc.Buffer;

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
	 * @throws IOException 
	 */
	public DataInfo buildDataInfo(String[] fileNames,InputStream[] ins,
										String reportName,String chipName,Integer mode) throws IOException{
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
	
	/**
	 * 根据dataInfo的信息处理数据文件，生成报告
	 * @param dataInfo  用户确认后的dataInfo
	 * @return Report 报告包含测试项 图表元数据
	 */
	public Report buildReport(DataInfo dataInfo){
		//TODO
		Report report=new Report();
		List<ColumnInfo> columnInfos=dataInfo.getColumns();
		List<FileInfo> files=dataInfo.getFiles();
		List<String> srcFiles=new ArrayList<String>();
		//2.0 reportItem信息填充 Chart的数据列柱子就位 初始高度为零
		List<ReportItem> reportItems = getInitialReportItems(columnInfos);
		long totalCount=0;
		//2.1 遍历fileInfoList
		for(int i=0;i<files.size();i++){
			FileInfo fileInfo=files.get(i);
			//2.2 判断数据文件的处理结果是不是成功，不成功则跳过，成功则继续
			if(!Constants.PROCESS_STATUS_DONE.equals(fileInfo.getStatus()) ){
				continue;
			}
			srcFiles.add(fileInfo.getFileName());
			
			try {
				//处理当前数据文件
				totalCount+=processFile(fileInfo,reportItems);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}catch(ArrayIndexOutOfBoundsException e){
				throw new AnalysisException(e.getMessage(),e);
			}
		}
		this.doBuildBars(reportItems);
		
		report.setReportName(dataInfo.getReportName());
		report.setSrcFile(StringUtils.join(srcFiles, ","));
		report.setChipName(dataInfo.getChipName());
		report.setMode(dataInfo.getMode());
		report.setTestCount(Long.toString(totalCount));
		report.setReportItems(reportItems);
		
		doBuildReportMeta(report);
		report.setTime(new Date());
		//System.out.println(new Gson().toJson(report));
		
		return report;
		
	}
	/**
	 * 填充剩余的可直接计算的report信息
	 * @param report
	 */
	private void doBuildReportMeta(Report report){
		Pattern osPattern=Pattern.compile(dataConfig.getTestItemColumnFlag().replace(",", "^"));//以OS或者PIN开头的测试巷
		DecimalFormat df = new DecimalFormat("#0.####");
		List<ReportItem> reportItems=report.getReportItems();
		long osFailCount=0;
		long totalFailCount=0;
		int testNo=1;
		double totalCount = Double.parseDouble(report.getTestCount());
		for(ReportItem reportItem:reportItems){
			String failRate=df.format(reportItem.getFailCount()/(double)reportItem.getPassCount());
			if(Long.compare(reportItem.getPassCount(), 0) <=0){
				failRate="0.0";
			}else{
				reportItem.setFailRate(failRate);
				if(osPattern.matcher(reportItem.getColumnName()).find()){
					osFailCount+=reportItem.getFailCount();
				}
				totalFailCount=reportItem.getFailCount();
				double sigma=Math.sqrt(reportItem.getSigma()/totalCount);
				double limitMin=reportItem.getLimitMin();
				double limitMax=reportItem.getLimitMax();
				if(!Double.isInfinite(limitMax-limitMin)){
					double ca=CpkUtils.calculateCa(limitMin, limitMax, reportItem.getRealAverage());
					double cp=CpkUtils.calculateCp(limitMin, limitMax, sigma);
					double cpk=CpkUtils.calculateCpk(cp, ca);
					if(Double.isInfinite(ca)) ca=99.99;
					if(Double.isInfinite(cp)) cp=99.99;
					if(Double.isInfinite(cpk)) cpk=99.99;
					reportItem.setCa(ca);
					reportItem.setCp(cp);
					reportItem.setCpk(cpk);
				}
				//System.out.println("debug col:"+reportItem.getColumnname()+" :"+cp+" "+ca+" "+cpk);
				reportItem.setSigma(sigma);
				
			}
			
			
			reportItem.setTestNo(testNo++);
			//totalCount  totalValue 没必要  就不算了
		}
		//Report osfailCount
		report.setOsFailCount(osFailCount);
		//Report osFailRate
		String passRate=df.format(1-totalFailCount/totalCount);
		String osFailRate=df.format(osFailCount/totalCount);
		report.setOsFailRate(osFailRate);
		report.setPassPercent(passRate);
	}
	/**
	 * 根据reportItem画图
	 * @param reportItems
	 * @return
	 */
	private List<ReportItem> doBuildBars(List<ReportItem> reportItems) {
		DecimalFormat df = new DecimalFormat("#0.####");
		for(int i=0;i<reportItems.size();i++){
			ReportItem reportItem=reportItems.get(i);
			Chart passChart=reportItem.getPassChart();
			Chart failChart=reportItem.getFailChart();
			
			if(passChart!=null){
				List<Bar> bars=passChart.getBars();
				StringBuilder sb=new StringBuilder("[");
				long maxHeight=0;
				for(int j=0;j<bars.size();j++){
					Bar bar=bars.get(j);
					if(bar.getHeight()==0){
						bars.remove(j);
						j--;
					}else{
						maxHeight=Math.max(maxHeight, bar.getHeight());
						double axis = bar.getAxis();
						bar.setAxis(0.0);
						bar.setAxisStr(df.format(axis));
						sb.append(bar.toString()+",");
					}
				}
				passChart.setQuantityMax(maxHeight);
				sb.deleteCharAt(sb.length()-1);
				sb.append("]");
				passChart.setDatas(sb.toString());
			}
			if(failChart!=null){
				List<Bar> bars=failChart.getBars();
				StringBuilder sb=new StringBuilder("[");
				long maxHeight=0;
				for(int j=0;j<bars.size();j++){
					Bar bar=bars.get(j);
					if(bar.getHeight()==0){
						bars.remove(j);
						j--;
					}else{
						maxHeight=Math.max(maxHeight, bar.getHeight());
						double axis = bar.getAxis();
						bar.setAxis(0.0);
						bar.setAxisStr(df.format(axis));
						sb.append(bar.toString()+",");
					}
				}
				failChart.setQuantityMax(maxHeight);
				sb.deleteCharAt(sb.length()-1);
				sb.append("]");
				failChart.setDatas(sb.toString());
			}
		}
		return reportItems;
	}

	/**
	 * 处理单个数据文件
	 * @param fileInfo 文件信息
	 * @param reportItems 测试项信息
	 * @return totalCount 总棵树
	 * @throws FileNotFoundException
	 */
	private long processFile(FileInfo fileInfo,List<ReportItem> reportItems) throws FileNotFoundException{
		//TODO
		if(dataConfig == null){
			throw new AnalysisException("DataConfig不存在");
		}
		Pattern passPattern = Pattern.compile(dataConfig.getDutPassTrueString());
		Pattern failPattern = Pattern.compile(dataConfig.getDutPassFalseString());
		
		String fileName=fileInfo.getLocalFileName();
		Resource dataFile=new FileSystemResource(fileName);
		long startline=fileInfo.getDataStartLine();
		long lineNumber=-1;
		long totalCount=0;
		String curLine=null;
		BufferedReader br=null;
		try {
			br=new BufferedReader(new InputStreamReader(dataFile.getInputStream()));
			//2.3 读取数据文件并跳转到数据行
			for(;startline>0;startline--){
				br.readLine();
				lineNumber++;
			}
			//2.4 逐行读取数据 判断true还是false
			while((curLine=br.readLine()) !=null){
				lineNumber++;
				
				if(passPattern.matcher(curLine).find()){
					//若pass  逐列测试项与判限比较 PassChart对应柱子数量+1,pass数量+1 计算标准差
					String[] datas=curLine.split(",");
					int length=reportItems.size();
					for(int i=0;i<length;i++){
						ReportItem reportItem=reportItems.get(i);
						Chart passChart=reportItem.getPassChart();
						String colDataStr=datas[reportItem.getCol()];
						double colData=0.0;
						if(StringUtils.isEmpty(colDataStr)){
							continue;
						}
						colData=Double.parseDouble(colDataStr) ;
						//求对应柱子下标
						double spacing=passChart.getSpacing();
						double rangeMin=passChart.getRangeMin();
						long index=Math.round((colData-rangeMin)/spacing);
						//pass数量+1
						long passCount=reportItem.getPassCount()+1;
						long chartCount=passChart.getTotalCnt()+1;
						//标准差
						double average=reportItem.getRealAverage();
						double sigma=reportItem.getSigma()+Math.pow((colData-average), 2);
						
						//zhuzi +1
						passChart.getBars().get((int)index).increase();
						passChart.setTotalCnt(chartCount);
						reportItem.setSigma(sigma);
						reportItem.setPassCount(passCount);
					}

					totalCount++;
				}else if(failPattern.matcher(curLine).find()){
					//若fail  仅fail的测试项的fail数量+1 FailChart对应柱子数量+1  （已经预处理so fail的就是最后一项）
					//TODO
					String[] datas=curLine.split(",");
					int lastData=datas.length-1;
					String colDataStr=datas[lastData];
					boolean isFailFind=false;
					for(int i=0;i<reportItems.size();i++){
						ReportItem reportItem=reportItems.get(i);
						//zhaodao fail data
						if(lastData==reportItem.getCol()){
							Chart failChart=reportItem.getFailChart();
							double colData=Double.parseDouble(colDataStr) ;
							double spacing=failChart.getSpacing();
							double rangeMin=failChart.getRangeMin();
							long index=Math.round((colData-rangeMin)/spacing);
							//fail的测试项的fail数量+1
							long failCount = reportItem.getFailCount()+1;
							long chartCount=failChart.getTotalCnt()+1;
							//FailChart对应柱子数量+1
							failChart.getBars().get((int)index).increase();
							failChart.setTotalCnt(chartCount);
							reportItem.setFailCount(failCount);
							isFailFind=true;
						}
					}
					totalCount++;
				}
				else{
					//do nothing
				}
						
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalCount;
		
	}
	private List<ReportItem> getInitialReportItems(List<ColumnInfo> columnInfos) {
		List<ReportItem> reportItemList=new ArrayList<ReportItem>();
		for(ColumnInfo columnInfo: columnInfos){
			//Pass Chart
			
			Chart passChart=null;
			if(columnInfo.getTotalCountInLimit() >0){
				Integer passGroups=columnInfo.getPassGroups();
				passChart =new Chart(Constants.CHART_PASS,passGroups);
				double pRealMin=columnInfo.getRealMinInLimit();
				double pRealMax=columnInfo.getRealMaxInLimit();
				double limitMin=columnInfo.getLimitMin();
				double limitMax=columnInfo.getLimitMax();
				double rangeMin=Math.floor(pRealMin);
				double rangeMax=Math.ceil(pRealMax);
				double spacing=(rangeMax-rangeMin)/(double)passGroups;
				if(spacing<0.001) spacing=0.001;
				List<Bar> bars=new ArrayList<Bar>();
				for(double value=rangeMin;value<=rangeMax;value+=spacing){
					Bar bar=new Bar(value,0);
					bars.add(bar);
				}
				
				passChart.setBars(bars);
				passChart.setSpacing(spacing);
				passChart.setRealMin(pRealMin);
				passChart.setRealMax(pRealMax);
				passChart.setRangeMin(rangeMin);
				passChart.setRangeMax(rangeMax);
				passChart.setLimit(limitMin, limitMax);
				passChart.setTitle(columnInfo.getColumnName()+"的良品分布图");
			}
			
			//Fail Chart
			Chart failChart=null;
			if(columnInfo.getTotalCountOutOfLimit()>0){
				Integer failGroups=columnInfo.getFailGroups();
				failChart=new Chart(Constants.CHART_FAIL, failGroups);
				double fRealMin=columnInfo.getRealMinOutOfLimit();
				double fRealMax=columnInfo.getRealMaxOutOfLimit();
				double limitMin=columnInfo.getLimitMin();
				double limitMax=columnInfo.getLimitMax();
				double rangeMin=Math.floor(fRealMin);
				double rangeMax=Math.ceil(fRealMax);
				double spacing=(rangeMax-rangeMin)/(double)failGroups;
				if(spacing<0.001) spacing=0.001;
				List<Bar> bars=new ArrayList<Bar>();
				for(double value=rangeMin;value<=rangeMax;value+=spacing){
					Bar bar=new Bar(value,0);
					bars.add(bar);
				}
				failChart.setBars(bars);
				failChart.setSpacing(spacing);
				failChart.setRealMin(fRealMin);
				failChart.setRealMax(fRealMax);
				failChart.setRangeMin(rangeMin);
				failChart.setRangeMax(rangeMax);
				failChart.setLimit(limitMin, limitMax);
				failChart.setTitle(columnInfo.getColumnName()+"的不良品分布图");
			}
			
			
			ReportItem reportItem=new ReportItem();
			reportItem.setCol(columnInfo.getId());
			reportItem.setColumnName(columnInfo.getColumnName());
			reportItem.setLimitMin(columnInfo.getLimitMin());
			reportItem.setLimitMax(columnInfo.getLimitMax());
			reportItem.setLimitUnit(columnInfo.getLimitUnit());
			reportItem.setRealAverage(columnInfo.getRealAverage());
			reportItem.setPassChart(passChart);
			reportItem.setFailChart(failChart);
			reportItemList.add(reportItem);
		}
		return reportItemList;
	}

	private DataInfo doBuildDataInfo(String[] fileNames,InputStream[] ins) throws IOException{
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
		Pattern patternDataLine=Pattern.compile("^\\d");
		
		List<FileInfo> fileInfoList=new ArrayList<FileInfo>();
		List<ColumnInfo> columnInfoList=null;
		
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
			
			long lineNumber=-1;//当前读取的行号
			String curLine=null;//当前行字符串
			BufferedReader br= new BufferedReader(new InputStreamReader(ins[i]));
			String localFileName=dir+"\\"+UUID.randomUUID().toString()+".csv";
			PrintWriter pw=null;
			fileInfo.setStatus(Constants.PROCESS_STATUS_INITIAL_STATE);
			try {
				pw=new PrintWriter( new BufferedWriter(new FileWriter(localFileName)));
				while((curLine = br.readLine() )!= null ){
					pw.println(curLine);
					lineNumber++;
					if(lineNumber >10){
						break;
					}
					System.out.println("line"+(lineNumber)+": "+curLine);
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
										recordLimitMinStr(curLine);
									}
									if(limitMaxStr== null 
											&& patternLimitMaxLine.matcher(curLine).find()){
										limitMaxStr=curLine;
										recordLimitMaxStr(curLine);
									}
								}
								if(limitMinStr!=null && limitMaxStr !=null){
									
									//看看是否有单位行  此处限定unitLine为Min and Max的下一行
									curLine = br.readLine();
									lineNumber++;
									if(curLine !=null && patternLimitUnitLine.matcher(curLine).find()){
										recordLimitUnitStr(curLine);
										fileInfo.setStatus(Constants.PROCESS_STATUS_HEAD_DONE_WITH_UNIT);
									}else{
										fileInfo.setStatus(Constants.PROCESS_STATUS_HEAD_DONE_WITHOUT_UNIT);
									}
									
									break;//跳出判限查找
								}
							}
						}
						if(curLine==null){
							throw new DataNotFoundException("判限未找到",Constants.PROCESS_STATUS_CANT_FIND_LIMIT);
						}
						if(fileInfo.getStatus()!=Constants.PROCESS_STATUS_INITIAL_STATE){
							break;//跳出测试项查找
						}
					}
				}//循环结束
				if(curLine==null){
					throw new DataNotFoundException("测试项未找到",Constants.PROCESS_STATUS_CANT_FIND_TESTITEM);
				}
				
				//2.2填充ColumnInfo
				columnInfoList = getColumnInfoList(dataConfig);
				//copy剩余部分
				//正常到这的话是刚刚判断了单位行 未保存该行 且lineNumber++了
				if(fileInfo.getStatus() == Constants.PROCESS_STATUS_HEAD_DONE_WITH_UNIT){
					//do nothing
				}else if (fileInfo.getStatus() == Constants.PROCESS_STATUS_HEAD_DONE_WITHOUT_UNIT) {
					//todo
				}else {
					throw new AnalysisException("未知异常");
				}
				lineNumber--;
				//找到数据起始行
				do{
					lineNumber++;
					if(patternDataLine.matcher(curLine).find()){
						fileInfo.setDataStartLine(lineNumber);
						break;
					}
					pw.println(curLine);
					
					
				}while((curLine = br.readLine())!= null);
				
				lineNumber--;
				do{
					lineNumber++;
					//2.3数据预处理   包括尾部冗余数据处理，realMin，realMax，realAverage计算
					//如果是数据 就预处理
					curLine=preprocessData(columnInfoList,patternDutPassTrue,patternDutPassFalse,curLine);
					
					
					pw.println(curLine);
				}while((curLine = br.readLine())!= null);
				//到这说明 测试项判限都匹配上了
				fileInfo.setLocalFileName(localFileName);
				fileInfo.setStatus(Constants.PROCESS_STATUS_DONE);
				fileInfoList.add(fileInfo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(TestItemLineNotMatchException e){
				fileInfo.setStatus(Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
				fileInfo.setMessage("测试项未匹配成功");
			}catch(LimitLineNotMatchException e){
				fileInfo.setStatus(Constants.PROCESS_STATUS_LIMIT_NOT_MATCH);
				fileInfo.setMessage("判限未匹配成功");
			}catch(DataNotFoundException e){
				fileInfo.setStatus(Constants.PROCESS_STATUS_LIMIT_NOT_MATCH);
				fileInfo.setMessage("未发现测试数据，错误代码："+e.getCode());
			}catch(AnalysisException e){
				fileInfo.setMessage(e.getMessage());
			}
			finally{
				if(pw !=null){
					pw.close();
				}
				if(br!=null){
					br.close();
				}
			}
		}//循环结束
		//补充总数和平均数
		for(ColumnInfo columnInfo:columnInfoList){
			long total=columnInfo.getTotalCountInLimit()+columnInfo.getTotalCountOutOfLimit();
			double average=0.0;
			if(total !=0){
				average= columnInfo.getTotalValue()/(double)total;
			}
			else{
				columnInfo.setIsProcess(false);
			}
			columnInfo.setTotalCountAll(total);
			columnInfo.setRealAverage(average);
		}
		dataInfo.setFiles(fileInfoList);
		dataInfo.setColumns(columnInfoList);
		return dataInfo;
		
	}
	/**
	 * 数据预处理  按照测试项要求处理当前行
	 * @param columnInfoList 测试项列表
	 * @param curLine 当前行
	 * @return String 处理后的当前行
	 */
	private String preprocessData(List<ColumnInfo> columnInfoList,Pattern passPattern,Pattern failPattern,String curLine) {
		if(columnInfoList==null){
			throw new DataNotFoundException("没有提供测试项信息");
		}
		String[] datas=curLine.split(",");
		int dutPass=dataInfo.getDutPassCol();
		
		if(dutPass >=datas.length){
			return curLine;
		}
		int length=columnInfoList.size();
		
		
		if(passPattern.matcher(curLine).find()){
			//如果是pass的
			for(int i=0;i<length;i++){
				ColumnInfo columnInfo = columnInfoList.get(i);
				String colDataStr=datas[columnInfo.getId()];
				double colData=0.0;
				if(StringUtils.isEmpty(colDataStr)){
					continue;
				}
				colData=Double.parseDouble(colDataStr);
				long totalCountInLimit=columnInfo.getTotalCountInLimit()+1;
				double totalValue=columnInfo.getTotalValue()+colData;
				double realMin=columnInfo.getRealMinInLimit();
				double realMax=columnInfo.getRealMaxInLimit();
				realMin=colData < realMin ? colData : realMin;
				realMax=colData > realMax ? colData : realMax;
				
				
				columnInfo.setTotalCountInLimit(totalCountInLimit);
				columnInfo.setTotalValue(totalValue);
				columnInfo.setRealMinInLimit(realMin);
				columnInfo.setRealMaxInLimit(realMax);
			}
		}else if(failPattern.matcher(curLine).find()){
			//如果是fail的double limitMin=
			boolean isFailFind=false;
			for(int i=0;i<length;i++){
				ColumnInfo columnInfo = columnInfoList.get(i);
				if(datas.length <= columnInfo.getId()){
					break;
				}
				if(isFailFind){
					datas[columnInfo.getId()]="";
					continue;
				}
				String colDataStr=datas[columnInfo.getId()];
				double colData=0.0;
				
				if(StringUtils.isNotEmpty(colDataStr)){
					colData=Double.parseDouble(colDataStr) ;
				}
				
				double limitMin=columnInfo.getLimitMin();
				double limitMax=columnInfo.getLimitMax();
				
				if(colData >limitMin && colData <limitMax){
					//如果 数据在判限内
					continue;
				}
				else if(colData <limitMin || colData >limitMax){
					//如果数据在判限外
					isFailFind=true;
				}else{
					//如果数据等于判限 那么情况就比较复杂了     SB chuangchuan 的坑
					//如果下一列有没有数据 在不在判限内 有且在就continue 
					//否则这就是fail列了
					int id=columnInfo.getId()+1;
					String nextColDataStr=datas[columnInfo.getId()];
					double nextColData=0.0;
					if(StringUtils.isNotEmpty(nextColDataStr)){
						nextColData=Double.parseDouble(nextColDataStr) ;
					}
					if((datas.length > id)
							&& length>id){
						ColumnInfo nextColumn= columnInfoList.get(id);
						if(nextColData >=nextColumn.getLimitMin() 
								&& nextColData <= nextColumn.getLimitMax()){
							continue;
						}
					}
					isFailFind=true;
				}
				double realMax=columnInfo.getRealMaxOutOfLimit();
				double realMin=columnInfo.getRealMinOutOfLimit();
				long totalCountOutOfLimit=columnInfo.getTotalCountOutOfLimit()+1;
				double totalValue=columnInfo.getTotalValue()+colData;
				
				realMin=colData < realMin ? colData : realMin;
				realMax=colData > realMax ? colData : realMax;
				
				columnInfo.setTotalValue(totalValue);
				columnInfo.setTotalCountOutOfLimit(totalCountOutOfLimit);
				columnInfo.setRealMinOutOfLimit(realMin);
				columnInfo.setRealMaxOutOfLimit(realMax);
			}
		}else{
			//anything else   do nothing
		}
		return StringUtils.join(datas, ",");
	}
	
	/**
	 * 根据dataConfig定义的正则来获取测试项
	 * @param dataConfig 包含一些正则
	 * @return List<ColumnInfo> 测试项列表
	 */
	private List<ColumnInfo> getColumnInfoList(DataConfig dataConfig) {
		if(dataInfo == null){
			throw new AnalysisException("dataInfo 为 null");
		}
		if(dataInfo.getColumns()!=null){
			return dataInfo.getColumns();
		}
		String testItemStr=dataInfo.getTestItemStr();
		String limitMinStr=dataInfo.getLimitMinStr();
		String limitMaxStr=dataInfo.getLimitMaxStr();
		String limitUnitStr=dataInfo.getLimitUnitStr();
		if(testItemStr ==null || limitMinStr==null || limitMaxStr == null){
			throw new AnalysisException("未知的testItemStr||limitMinStr||limitMaxStr");
		}
		List<ColumnInfo> columns=new ArrayList<ColumnInfo>();
		
		String[] testItems=testItemStr.split(",");
		String[] limitMins=limitMinStr.split(",");
		String[] limitMaxs=limitMaxStr.split(",");
		String[] limitUnits=null;
		if(limitUnitStr!=null) limitUnits=limitUnitStr.split(",");
		Pattern osPattern=Pattern.compile(dataConfig.getTestItemColumnFlag().replace(",", "^"));
		Pattern passPattern=Pattern.compile(dataConfig.getDutPassColumnFlag().replace(",", ""));
		Pattern sitePattern=Pattern.compile(dataConfig.getSiteNoColumnFlag().replace(",", ""));
		Pattern indexPattern=Pattern.compile(dataConfig.getDutNoColumnFlag().replace(",", ""));
		Pattern ignorePattern=Pattern.compile(dataConfig.getIgnoreColumnFlag());
		boolean isTestItem=false;
		for(int i=0;i<testItems.length;i++){
			if(isTestItem == false){
				//找到dut_pass列
				if(passPattern.matcher(testItems[i]).find()){
					dataInfo.setDutPassCol(i);
					continue;
				}
				//找到Site_No列
				if(sitePattern.matcher(testItems[i]).find()){
					dataInfo.setSiteCol(i);
					continue;
				}
				//找到dut_no列
				if(indexPattern.matcher(testItems[i]).find()){
					dataInfo.setIndexCol(i);
					continue;
				}
				//找到测试项列
				if(osPattern.matcher(testItems[i]).find()){
					isTestItem=true;
				}
			}
			if(isTestItem ==true ){
				if(StringUtils.isBlank(testItems[i])){
					continue;
				}
				Double min,max;
				String unit;
				Boolean isProcess;
				//min处理
				if(i < limitMins.length){
					if(StringUtils.isBlank(limitMins[i])){
						min=-Double.MAX_VALUE;
					}else{
						min=Double.parseDouble(limitMins[i]);
						//SB changchuan de 9999.999 chuli
						if(min <-9998 && min > -10001){
							min=-Double.MAX_VALUE;
						}
					}
				
				}else{
					continue;
				}
				//max处理
				if(i < limitMaxs.length){
					if(StringUtils.isBlank(limitMaxs[i])){
						max=Double.MAX_VALUE;
					}else{
						max= Double.parseDouble(limitMaxs[i]);
						//SB changchuan de 9999.999 chuli
						if(max >9998 && max <10001){
							max=Double.MAX_VALUE;
						}
					}
				}else{
					continue;
				}
				//unit处理
				if(limitUnits !=null && i < limitUnits.length){
					unit=limitUnits[i];
				}
				else{
					unit="";
				}
				//判断是否要处理
				if(ignorePattern.matcher(testItems[i]).find()
						|| osPattern.matcher(testItems[i]).find()){
					isProcess=Boolean.FALSE;
				}
				else{
					isProcess=Boolean.TRUE;
				}
				ColumnInfo columnInfo=new ColumnInfo();
				columnInfo.setId(i);
				columnInfo.setColumnName(testItems[i]);
				columnInfo.setLimitMin(min);
				columnInfo.setLimitMax(max);
				columnInfo.setLimitUnit(unit);
				columnInfo.setTotalValue(0);
				columnInfo.setTotalCountAll(0);
				columnInfo.setTotalCountInLimit(0);
				columnInfo.setTotalCountOutOfLimit(0);
				columnInfo.setRealAverage(0);
				columnInfo.setRealMaxInLimit(-Double.MAX_VALUE);
				columnInfo.setRealMaxOutOfLimit(-Double.MAX_VALUE);
				columnInfo.setRealMinInLimit(Double.MAX_VALUE);
				columnInfo.setRealMinOutOfLimit(Double.MAX_VALUE);
				columnInfo.setPassGroups(500);
				columnInfo.setFailGroups(50);
				columnInfo.setIsProcess(isProcess);
				columns.add(columnInfo);
				
				//System.out.println("debug: testItems: "+testItems[i]);
			}
			
		}
		return columns;
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
	private void recordLimitMinStr(String str){
		if(dataInfo.getLimitMinStr() == null){
			dataInfo.setLimitMinStr(str);
		}
		else{
			if(!dataInfo.getLimitMinStr().equals(str)){
				throw new LimitLineNotMatchException(dataInfo.getLimitMinStr(),str);
				//return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
			}
		}
	}
	private void recordLimitMaxStr(String str){
		if(dataInfo.getLimitMaxStr() == null){
			dataInfo.setLimitMaxStr(str);
		}
		else{
			if(!dataInfo.getLimitMaxStr().equals(str)){
				throw new LimitLineNotMatchException(dataInfo.getLimitMaxStr(),str);
			}
		}
	}	
	private void recordLimitUnitStr(String str){
		if(dataInfo.getLimitUnitStr()==null){
			dataInfo.setLimitUnitStr(str);
		}
		else{
			if(!dataInfo.getLimitUnitStr().equals(str)){
				throw new LimitLineNotMatchException(dataInfo.getLimitMaxStr(),str);
			}
		}
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
