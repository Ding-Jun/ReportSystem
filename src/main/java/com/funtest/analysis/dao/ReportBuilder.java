package com.funtest.analysis.dao;

import com.funtest.analysis.bean.*;
import com.funtest.analysis.exception.AnalysisException;
import com.funtest.analysis.exception.DataNotFoundException;
import com.funtest.analysis.exception.LimitLineNotMatchException;
import com.funtest.analysis.exception.TestItemLineNotMatchException;
import com.funtest.analysis.util.CpkUtils;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class ReportBuilder {
    private final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
    DataInfo dataInfo;
    DataConfig dataConfig;

    /**
     * 根据ins解析出DataInfo  并删除false数据的多余部分将数据文件保存到本地
     *
     * @param fileNames 文件名  多个
     * @param ins       InputStream 多个 与文件名个数相等
     * @param chipName  芯片名 （可选）
     * @param mode      数据处理模式     FT  or  FT&RT
     * @return DataInfo 包含了生成报告所需的所有信息，如 测试项名称，判限等
     * @throws IOException
     */
    public DataInfo buildDataInfo(String[] fileNames, InputStream[] ins,
                                  String reportName, String chipName, Integer mode) throws IOException {

        //1.非空验证
        if (ins == null || ins.length == 0) {
            throw new AnalysisException("没有提供数据文件~");
        }
        if (StringUtils.isBlank(reportName)) {
            throw new AnalysisException("报告名称未指定~");
        }
        if (StringUtils.isBlank(chipName)) {
            chipName = "undefined";
        }
        if (mode == null) {
            mode = Constants.PROCESS_MODE_NORMAL;
        }
        if (fileNames.length != ins.length) {
            throw new AnalysisException("文件数量与InputStream数量不一致~");
        }

        //2.填充 dataInfo
        DataInfo dataInfo = new DataInfo();
        dataInfo.setReportName(reportName);
        dataInfo.setMode(mode);
        dataInfo.setChipName(chipName);
        this.dataInfo = dataInfo;

        //填充
        this.doBuildDataInfo(fileNames, ins);
        if (logger.isInfoEnabled()) {
            logger.info("DataInfo 生成完毕。JSON FORMAT：{}", new Gson().toJson(dataInfo));
        }
        return dataInfo;
    }

    /**
     * 根据dataInfo的信息处理数据文件，生成报告
     *
     * @param dataInfo 用户确认后的dataInfo
     * @return Report 报告包含测试项 图表元数据
     */
    public Report buildReport(DataInfo dataInfo) {
        if(dataInfo.getColumns().size()==0){
            throw new AnalysisException("没有合法的列");
        }
        Report report = new Report();
        List<ColumnInfo> columnInfos = dataInfo.getColumns();
        List<FileInfo> files = dataInfo.getFiles();
        List<String> srcFiles = new ArrayList<String>();
        //2.0 reportItem信息填充 Chart的数据列柱子就位 初始高度为零
        List<ReportItem> reportItems = getInitialReportItems(columnInfos);
        long totalCount = 0;
        //2.1 遍历fileInfoList
        for (int i = 0; i < files.size(); i++) {
            FileInfo fileInfo = files.get(i);
            //2.2 判断数据文件的处理结果是不是成功，不成功则跳过，成功则继续
            if (!Constants.PROCESS_STATUS_DONE.equals(fileInfo.getStatus())) {
                logger.info("跳过文件 {}", fileInfo.getFileName());
                continue;
            }
            srcFiles.add(fileInfo.getFileName());

            try {
                //处理当前数据文件
                totalCount += processFile(fileInfo, reportItems);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new AnalysisException(e.getMessage(), e);
            }
        }//循环结束
        this.calculateRestBars(reportItems);

        report.setReportName(dataInfo.getReportName());
        report.setSrcFile(StringUtils.join(srcFiles, ","));
        report.setChipName(dataInfo.getChipName());
        report.setMode(dataInfo.getMode());
        report.setTestCount(Long.toString(totalCount));
        report.setReportItems(reportItems);

        calculateRestReport(report);
        //remove  reportitem which don't need analysis
        this.removeExtraReportItems(reportItems, columnInfos);
        report.setTime(new Date());
        //System.out.println(new Gson().toJson(report));

        return report;

    }

    /**
     * 填充剩余的可直接计算的report信息
     *
     * @param report
     */
    private Report calculateRestReport(Report report) {
        Pattern osPattern = Pattern.compile(dataConfig.getTestItemColumnFlag().replace(",", "^"));//以OS或者PIN开头的测试巷
        DecimalFormat df = new DecimalFormat("#0.####");
        List<ReportItem> reportItems = report.getReportItems();
        long osFailCount = 0;
        long totalFailCount = 0;
        int testNo = 1;
        double totalCount = Double.parseDouble(report.getTestCount());

        //统计失效数 OS失效数 cpk
        for (ReportItem reportItem : reportItems) {
            String failRate = df.format(reportItem.getFailCount() / (double) reportItem.getPassCount()*100);
            if (reportItem.getPassCount() <= 0) {
                failRate = "0.0";
            } else {
                reportItem.setFailRate(failRate);
                if (osPattern.matcher(reportItem.getColumnName()).find()) {
                    osFailCount += reportItem.getFailCount();
                }
                totalFailCount += reportItem.getFailCount();
                //deal with pass     如是抽样（即估算样本方差），根号内除以（n-1）（对应excel函数：STDEV）；
                double passStdev=Math.sqrt(reportItem.getPassStdev() / (double)(reportItem.getPassCount()-1));
                if(Double.isNaN(passStdev)){passStdev=0.0;}
                double limitMin = reportItem.getLimitMin();
                double limitMax = reportItem.getLimitMax();
                if (!Double.isInfinite(limitMax - limitMin) && passStdev!=0.0) {
                    double cpl = CpkUtils.calculateCpl(limitMin,reportItem.getPassRealAverage(),passStdev);
                    double cpu =CpkUtils.calculateCpu(limitMax,reportItem.getPassRealAverage(),passStdev);
                    double cp = CpkUtils.calculateCp(limitMin, limitMax, passStdev);
                    double cpk = CpkUtils.calculateCpk(cpu, cpl);
                    reportItem.setCpl(cpl);
                    reportItem.setCpu(cpu);
                    reportItem.setCp(cp);
                    reportItem.setCpk(cpk);
                }
                //System.out.println("debug col:"+reportItem.getColumnname()+" :"+cp+" "+ca+" "+cpk);
                reportItem.setPassStdev(passStdev);

                //deal with fail       如是抽样（即估算样本方差），根号内除以（n-1）（对应excel函数：STDEV）；
                if(reportItem.getFailCount()>1){
                    double failStdev =Math.sqrt(reportItem.getFailStdev() / (double)(reportItem.getFailCount()-1));
                    reportItem.setFailStdev(failStdev);
                }

            }

            reportItem.setRank(calculateReportItemRank(failRate));
            reportItem.setTestNo(testNo++);
            //totalCount  totalValue 没必要  就不算了
        }
        //Report osfailCount
        report.setOsFailCount(osFailCount);
        //Report osFailRate
        String passRate = df.format(100 - totalFailCount / (double) totalCount*100);
        String osFailRate = df.format(osFailCount / (double) totalCount*100);
        report.setOsFailRate(osFailRate);
        report.setPassPercent(passRate);
        report.setOsRank(calculateReportItemRank(osFailRate));
        report.setRank(calculateReportRank(passRate));
        return report;
    }

    /**
     * 计算ReportItem失效等级  超过1%为高
     * @param failRate  失效率 字符串
     * @return  ReportItem失效等级
     */
    private Integer calculateReportItemRank(String failRate){
        double rate= Double.parseDouble(failRate);
        Integer rank;
        if(rate<1){
            rank=Constants.RANK_HIGH;
        }
        else{
            rank=Constants.RANK_LOW;
        }
        return rank;
    }
    private Integer calculateReportRank(String failRate){
        double rate= Double.parseDouble(failRate);
        Integer rank;
        if(rate>95){
            rank=Constants.RANK_HIGH;
        }
        else if(rate>90){
            rank=Constants.RANK_MEDIUM;
        }else {
            rank=Constants.RANK_LOW;
        }
        return rank;
    }
    /**
     * 移除isProcess为False的测试项，并重新编号
     * @param reportItems   完整的reportItem信息
     * @param columnInfos   有isProcess信息
     * @return 移除的ReportItem数量
     */
    private int removeExtraReportItems(List<ReportItem> reportItems, List<ColumnInfo> columnInfos) {
        //reportItems columnInfos 顺序相同
        int removeCount=0;
        for (ColumnInfo columnInfo : columnInfos) {
            //如果不需要处理
            if (Boolean.FALSE.equals(columnInfo.getIsProcess())) {
                for (int i = 0; i < reportItems.size(); i++) {
                    ReportItem reportItem = reportItems.get(i);
                    //找到reportItem 移出
                    if(columnInfo.getId().equals(reportItem.getCol())){
                        reportItems.remove(reportItem);
                        removeCount++;
                        break;
                    }
                }
            }

        }
        //整理列编号
        int testNo=1;
        for(ReportItem reportItem: reportItems){
            reportItem.setTestNo(testNo++);
        }
        return removeCount;
    }

    /**
     * 根据reportItem画图
     *
     * @param reportItems
     * @return
     */
    private List<ReportItem> calculateRestBars(List<ReportItem> reportItems) {
        DecimalFormat df = new DecimalFormat("#0.####");
        for (int i = 0; i < reportItems.size(); i++) {
            ReportItem reportItem = reportItems.get(i);
            Chart passChart = reportItem.getPassChart();
            Chart failChart = reportItem.getFailChart();

            if (passChart != null) {
                doCalculateRestBars(passChart,df);
            }
            if (failChart != null) {
                doCalculateRestBars(failChart,df);
            }
        }
        return reportItems;
    }
    private void doCalculateRestBars(Chart chart,DecimalFormat df){
        List<Bar> bars = chart.getBars();
        StringBuilder sb = new StringBuilder("[");
        long maxHeight = 0;
        for (int j = 0; j < bars.size(); j++) {
            Bar bar = bars.get(j);
            if (bar.getHeight() == 0) {
                bars.remove(j);
                j--;
            } else {
                maxHeight = Math.max(maxHeight, bar.getHeight());
                double axis = bar.getAxis();
                bar.setAxis(0.0);
                bar.setAxisStr(df.format(axis));
                sb.append(bar.toString() + ",");
            }
        }
        chart.setQuantityMax(maxHeight);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        chart.setDatas(sb.toString());
    }
    /**
     * 处理单个数据文件
     *
     * @param fileInfo    文件信息
     * @param reportItems 测试项信息
     * @return totalCount 总棵树
     * @throws FileNotFoundException
     */
    private long processFile(FileInfo fileInfo, List<ReportItem> reportItems) throws FileNotFoundException {
        //TODO
        if (dataConfig == null) {
            throw new AnalysisException("DataConfig不存在");
        }
        Pattern passPattern = Pattern.compile(dataConfig.getDutPassTrueString());
        Pattern failPattern = Pattern.compile(dataConfig.getDutPassFalseString());

        String fileName = fileInfo.getLocalFileName();
        Resource dataFile = new FileSystemResource(fileName);
        long startline = fileInfo.getDataStartLine();
        long ignoreLine = 0;
        long lineNumber = -1;
        long totalCount = 0;
        String curLine = null;
        BufferedReader br = null;
        logger.info("开始处理文件 {} <=> {}", fileInfo.getFileName(), fileInfo.getLocalFileName());
        try {
            br = new BufferedReader(new InputStreamReader(dataFile.getInputStream()));

            //2.3 读取数据文件并跳转到数据行
            for (; startline > 0; startline--) {
                br.readLine();
                lineNumber++;
            }
            logger.debug("从第{}行开始读取数据", fileInfo.getDataStartLine());
            //2.4 逐行读取数据 判断true还是false
            while ((curLine = br.readLine()) != null) {
                lineNumber++;

                if (passPattern.matcher(curLine).find()) {
                    processPassLine(curLine,reportItems);
                    totalCount++;
                } else if (failPattern.matcher(curLine).find()) {
                    processFailLine(curLine,reportItems);
                    totalCount++;
                } else {
                    //do nothing
                    ignoreLine++;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.debug("处理完毕,忽略了{}行", ignoreLine);

        return totalCount;

    }

    /**
     * 生成Report时，对Pass芯片的处理
     * @param curLine
     * @param reportItems
     */
    private void processPassLine(String curLine,List<ReportItem> reportItems){
        //若pass  逐列测试项与判限比较 PassChart对应柱子数量+1,pass数量+1 计算标准差
        String[] datas = curLine.split(",");
        int length = reportItems.size();
        for (int i = 0; i < length; i++) {
            ReportItem reportItem = reportItems.get(i);
            Chart passChart = reportItem.getPassChart();
            String colDataStr = datas[reportItem.getCol()];
            double colData = 0.0;
            if (StringUtils.isEmpty(colDataStr)) {
                continue;
            }
            colData = Double.parseDouble(colDataStr);
            //求对应柱子下标
            double spacing = passChart.getSpacing();
            double rangeMin = passChart.getRangeMin();
            long index = Math.round((colData - rangeMin) / spacing);
            //pass数量+1
            long passCount = reportItem.getPassCount() + 1;
            long chartCount = passChart.getTotalCnt() + 1;
            //标准差
            double passAverage = reportItem.getPassRealAverage();
            double passStdev = reportItem.getPassStdev() + Math.pow((colData - passAverage), 2);
            //总的

            //zhuzi +1
            passChart.getBars().get((int) index).increase();
            passChart.setTotalCnt(chartCount);
            reportItem.setPassStdev(passStdev);
            reportItem.setPassCount(passCount);
        }
    }

    /**
     * 生成Report时 对Fail芯片的处理
     * @param curLine
     * @param reportItems
     */
    private void processFailLine(String curLine,List<ReportItem> reportItems){
        //若fail  仅fail的测试项的fail数量+1 FailChart对应柱子数量+1  （已经预处理so fail的就是最后一项）
        String[] datas = curLine.split(",");
        int lastData = datas.length - 1;
        String colDataStr = datas[lastData];
        boolean isFailFind = false;
        for (int i = 0; i < reportItems.size(); i++) {
            ReportItem reportItem = reportItems.get(i);
            //zhaodao fail data
            if (lastData == reportItem.getCol()) {
                Chart failChart = reportItem.getFailChart();
                double colData = Double.parseDouble(colDataStr);
                //求对应柱子下标
                double spacing = failChart.getSpacing();
                double rangeMin = failChart.getRangeMin();
                long index = Math.round((colData - rangeMin) / spacing);
                //fail的测试项的fail数量+1
                long failCount = reportItem.getFailCount() + 1;
                long chartCount = failChart.getTotalCnt() + 1;
                //标准差
                double failAverage=reportItem.getFailRealAverage();
                double failStdev=reportItem.getFailStdev()+ Math.pow((colData - failAverage), 2);

                //FailChart对应柱子数量+1
                failChart.getBars().get((int) index).increase();
                failChart.setTotalCnt(chartCount);
                reportItem.setFailStdev(failStdev);
                reportItem.setFailCount(failCount);
                isFailFind = true;
                break;
            }
        }
        if (isFailFind == false) {
            logger.warn("发现Fail 但是此项没有被勾选 CurLine:{}", curLine);
        }
    }
    private List<ReportItem> getInitialReportItems(List<ColumnInfo> columnInfos) {
        List<ReportItem> reportItemList = new ArrayList<ReportItem>();
        for (ColumnInfo columnInfo : columnInfos) {
            //Pass Chart

            Chart passChart = null;
            if (columnInfo.getTotalCountInLimit() > 0) {
                int passGroups = columnInfo.getPassGroups();
                passChart = new Chart(Constants.CHART_PASS, passGroups);
                double pRealMin = columnInfo.getRealMinInLimit();
                double pRealMax = columnInfo.getRealMaxInLimit();
                double limitMin = columnInfo.getLimitMin();
                double limitMax = columnInfo.getLimitMax();
                double rangeMin = Math.floor(pRealMin);
                double rangeMax = Math.ceil(pRealMax);
                double spacing = (rangeMax - rangeMin) / (double) passGroups;
                if (spacing < 0.001) spacing = 0.001;
                List<Bar> bars = new ArrayList<Bar>();
                for (int i = 0; i <= passGroups; i++) {
                    double value = rangeMin + spacing * i;
                    Bar bar = new Bar(value, 0);
                    bars.add(bar);
                }

                passChart.setBars(bars);
                passChart.setSpacing(spacing);
                passChart.setRealMin(pRealMin);
                passChart.setRealMax(pRealMax);
                passChart.setRangeMin(rangeMin);
                passChart.setRangeMax(rangeMax);
                passChart.setLimit(limitMin, limitMax);
                passChart.setTitle(columnInfo.getColumnName() + "的良品分布图");
            }

            //Fail Chart
            Chart failChart = null;
            if (columnInfo.getTotalCountOutOfLimit() > 0) {
                int failGroups = columnInfo.getFailGroups();
                failChart = new Chart(Constants.CHART_FAIL, failGroups);
                double fRealMin = columnInfo.getRealMinOutOfLimit();
                double fRealMax = columnInfo.getRealMaxOutOfLimit();
                double limitMin = columnInfo.getLimitMin();
                double limitMax = columnInfo.getLimitMax();
                double rangeMin = Math.floor(fRealMin);
                double rangeMax = Math.ceil(fRealMax);
                double spacing = (rangeMax - rangeMin) / (double) failGroups;
                if (spacing < 0.001) spacing = 0.001;
                List<Bar> bars = new ArrayList<Bar>();

                for (int i = 0; i <= failGroups; i++) {
                    double value = rangeMin + spacing * i;
                    Bar bar = new Bar(value, 0);
                    bars.add(bar);
                }
                failChart.setBars(bars);
                failChart.setSpacing(spacing);
                failChart.setRealMin(fRealMin);
                failChart.setRealMax(fRealMax);
                failChart.setRangeMin(rangeMin);
                failChart.setRangeMax(rangeMax);
                failChart.setLimit(limitMin, limitMax);
                failChart.setTitle(columnInfo.getColumnName() + "的不良品分布图");
            }


            ReportItem reportItem = new ReportItem();
            reportItem.setCol(columnInfo.getId());
            reportItem.setColumnName(columnInfo.getColumnName());
            reportItem.setLimitMin(columnInfo.getLimitMin());
            reportItem.setLimitMax(columnInfo.getLimitMax());
            reportItem.setLimitUnit(columnInfo.getLimitUnit());
            reportItem.setRealAverage(columnInfo.getRealAverage());
            reportItem.setPassRealAverage(columnInfo.getRealAverageInLimit());
            reportItem.setFailRealAverage(columnInfo.getRealAverageOutOfLimit());
            reportItem.setPassChart(passChart);
            reportItem.setFailChart(failChart);
            reportItemList.add(reportItem);
        }
        return reportItemList;
    }

    private DataInfo doBuildDataInfo(String[] fileNames, InputStream[] ins) throws IOException {
        if (dataInfo == null) {
            throw new AnalysisException("DataInfo not set");
        }
        if (dataConfig == null) {
            throw new AnalysisException("DataConfig not set");
        }
        if (logger.isInfoEnabled()) {
            logger.info("文件处理列表：{}", StringUtils.join(fileNames, ","));
        }
        String dir = Constants.FILE_UPLOAD_DIR;
        File dirFile = new File(dir);
        if (!dirFile.exists() && !dirFile.isDirectory()) {
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
        Pattern patternLimitLine = Pattern.compile(dataConfig.getLimitMinLineFlag() + "|" + dataConfig.getLimitMaxLineFlag());
        Pattern patternLimitUnitLine = Pattern.compile("^\\D");
        Pattern patternDataLine = Pattern.compile("^\\d");

        Pattern[] patternsTestItemLine = {patternDutNoColumn, patternSiteNoColumn, patternDutPassColumn, patternTestItemColumnFlag};
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
        List<ColumnInfo> columnInfoList = new ArrayList<ColumnInfo>();

        for (int i = 0; i < ins.length; i++) {
            //2.1填充FileInfo
            FileInfo fileInfo = new FileInfo();
            String fileName = fileNames[i];
            fileInfo.setFileName(fileName);
            logger.debug("开始处理文件：{}", fileName);
            if (!isDataFile(fileNames[i])) {
                //logger.info("{} skipped in createReportSnaps..",fileNames[i]);
                fileInfo.setStatus(Constants.PROCESS_STATUS_ERROR_FORMAT);
                fileInfo.setMessage("不是csv文件");
                fileInfoList.add(fileInfo);
                if (logger.isInfoEnabled()) {
                    logger.info("文件{} 文件名不合法 继续处理下个文件", fileName);
                }
                continue;
            }
            //当且仅当 FT&RT模式且为FT数据时为true
            boolean deleteFail=false;
            if(Constants.PROCESS_MODE_DELETE_FT_FAIL.equals(dataInfo.getMode())
                    && !fileName.contains("RT")){
                deleteFail=true;
            }
            long lineNumber = -1;//当前读取的行号
            String curLine = null;//当前行字符串
            BufferedReader br = new BufferedReader(new InputStreamReader(ins[i]));
            String localFileName = this.convertToLocalFileName(dir, fileName);
            PrintWriter pw = null;
            fileInfo.setStatus(Constants.PROCESS_STATUS_INITIAL_STATE);
            try {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(localFileName)));
                while ((curLine = br.readLine()) != null) {
                    pw.println(curLine);
                    lineNumber++;

                    //匹配测试项所在行成功
                    if (isTestItemLine(curLine, patternsTestItemLine)) {
                        //logger.info("测试项所在行："+(lineNumber-1));

                        //record
                        recordTestItemStr(curLine);
                        logger.debug("测试项匹配成功 Line:{} String:{}", lineNumber, curLine);
                        //开始查找limit
                        String limitMinStr = null;
                        String limitMaxStr = null;
                        while ((curLine = br.readLine()) != null) {
                            pw.println(curLine);
                            lineNumber++;
                            if (limitMinStr == null || limitMaxStr == null) {
                                //匹配limit成功
                                if (patternLimitLine.matcher(curLine).find()) {
                                    if (limitMinStr == null
                                            && patternLimitMinLine.matcher(curLine).find()) {
                                        limitMinStr = curLine;
                                        recordLimitMinStr(curLine);
                                        logger.debug("找到判限最小值 Line:{} String:{}", lineNumber, curLine);
                                    }
                                    if (limitMaxStr == null
                                            && patternLimitMaxLine.matcher(curLine).find()) {
                                        limitMaxStr = curLine;
                                        recordLimitMaxStr(curLine);
                                        logger.debug("找到判限最大值 Line:{} String:{}", lineNumber, curLine);
                                    }
                                }
                                if (limitMinStr != null && limitMaxStr != null) {
                                    //看看是否有单位行  此处限定unitLine为Min and Max的下一行
                                    curLine = br.readLine();
                                    lineNumber++;
                                    if (curLine != null && patternLimitUnitLine.matcher(curLine).find()) {
                                        recordLimitUnitStr(curLine);
                                        logger.debug("单位行匹配成功 Line:{} String:{}", lineNumber, curLine);
                                        fileInfo.setStatus(Constants.PROCESS_STATUS_HEAD_DONE_WITH_UNIT);
                                    } else {
                                        logger.debug("无单位行");
                                        fileInfo.setStatus(Constants.PROCESS_STATUS_HEAD_DONE_WITHOUT_UNIT);
                                    }

                                    break;//跳出判限查找
                                }
                            }
                        }
                        if (curLine == null) {
                            throw new LimitLineNotMatchException("判限未找到");
                        }
                        if (fileInfo.getStatus() != Constants.PROCESS_STATUS_INITIAL_STATE) {
                            break;//跳出测试项查找
                        }
                    }
                }//循环结束
                if (curLine == null) {
                    throw new DataNotFoundException("测试项未找到", Constants.PROCESS_STATUS_CANT_FIND_TESTITEM);
                }

                //2.2填充ColumnInfo
                columnInfoList = getInitialColumnInfos(dataConfig);
                //copy剩余部分
                //正常到这的话是刚刚判断了单位行 未保存该行 且lineNumber++了
                if (fileInfo.getStatus() == Constants.PROCESS_STATUS_HEAD_DONE_WITH_UNIT) {
                    //do nothing
                } else if (fileInfo.getStatus() == Constants.PROCESS_STATUS_HEAD_DONE_WITHOUT_UNIT) {
                    //todo
                } else {
                    throw new AnalysisException("未知异常");
                }
                lineNumber--;
                //找到数据起始行
                do {
                    lineNumber++;
                    if (patternDataLine.matcher(curLine).find()) {
                        fileInfo.setDataStartLine(lineNumber);
                        break;
                    }
                    pw.println(curLine);


                } while ((curLine = br.readLine()) != null);

                lineNumber--;
                do {
                    lineNumber++;
                    //2.3数据预处理   包括尾部冗余数据处理，realMin，realMax，realAverage计算
                    //如果是数据 就预处理
                    curLine = preprocessData(columnInfoList, patternDutPassTrue, patternDutPassFalse, curLine,deleteFail);
                    pw.println(curLine);
                } while ((curLine = br.readLine()) != null);
                //到这说明 测试项判限都匹配上了
                fileInfo.setLocalFileName(localFileName);
                fileInfo.setStatus(Constants.PROCESS_STATUS_DONE);
                logger.debug("文件处理成功，存储本地文件名为：{}", localFileName);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                logger.error("文件{}处理异常：{}", fileName, e.getMessage());
            } catch (TestItemLineNotMatchException e) {
                fileInfo.setStatus(Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
                fileInfo.setMessage("测试项未匹配成功");
                logger.error("文件{} {}", fileName, fileInfo.getMessage());
            } catch (LimitLineNotMatchException e) {
                fileInfo.setStatus(Constants.PROCESS_STATUS_LIMIT_NOT_MATCH);
                fileInfo.setMessage("判限未匹配成功");
                logger.error("文件{} {}", fileName, fileInfo.getMessage());
            } catch (DataNotFoundException e) {
                fileInfo.setStatus(Constants.PROCESS_STATUS_LIMIT_NOT_MATCH);
                fileInfo.setMessage("未发现测试数据，错误代码：" + e.getCode());
                logger.error("文件{} {}", fileName, fileInfo.getMessage());
            } catch (AnalysisException e) {
                fileInfo.setMessage(e.getMessage());
                logger.error("文件{} {}", fileName, fileInfo.getMessage());
            } finally {
                if (pw != null) {
                    pw.close();
                }
                if (br != null) {
                    br.close();
                }
                fileInfoList.add(fileInfo);
                logger.debug("文件{}处理结束", fileName);
            }
            dataInfo.setColumns(columnInfoList);
        }//循环结束
        //补充总数和平均数
        this.calculateRestColumnInfos(columnInfoList);
        dataInfo.setFiles(fileInfoList);

        if (logger.isInfoEnabled()) {
            logger.info("所有文件处理结束 共处理{}个文件 ",
                    fileInfoList.size());
            logger.info("共发现{}个测试项 ", columnInfoList.size());
            if(columnInfoList.size()>0){
                logger.info("从Col:{} name:{}开始",
                        Integer.toString(columnInfoList.get(0).getId()),
                        columnInfoList.get(0).getColumnName());
            }


        }
        return dataInfo;

    }

    /**
     * 计算剩余可计算参数  包括  总数/判限内/判限外 平均值=总值/总数  数量为0的列勾选不处理
     *
     * @param columnInfoList
     * @return
     */
    private List<ColumnInfo> calculateRestColumnInfos(List<ColumnInfo> columnInfoList) {
        if(columnInfoList ==null){
            return columnInfoList;
        }
        for (ColumnInfo columnInfo : columnInfoList) {
            long total = columnInfo.getTotalCountAll();
            long totalInLimit = columnInfo.getTotalCountInLimit();
            long totalOutOfLimit = columnInfo.getTotalCountOutOfLimit();

            double average = 0.0;
            double averageInLimit = 0.0;
            double averageOutOfLimit = 0.0;

            if (total != 0) {
                average = columnInfo.getTotalValue() / (double) total;
            } else {
                columnInfo.setIsProcess(false);
            }

            if(totalInLimit !=0){
                averageInLimit=columnInfo.getTotalValueInLimit()/(double)totalInLimit;
            }
            if(totalOutOfLimit !=0){
                averageOutOfLimit=columnInfo.getTotalValueOutOfLimit()/(double)totalOutOfLimit;
            }

            columnInfo.setRealAverage(average);
            columnInfo.setRealAverageInLimit(averageInLimit);
            columnInfo.setRealAverageOutOfLimit(averageOutOfLimit);
        }
        return columnInfoList;
    }

    /**
     * 根据提供的正则判断 字符串是否匹配得上
     *
     * @param curLine  要匹配的字符串
     * @param patterns 正则pattern
     * @return 匹配上 true ，否则false
     */
    private boolean isTestItemLine(String curLine, Pattern... patterns) {
        for (Pattern pattern :
                patterns) {
            if (!pattern.matcher(curLine).find()) {
                return false;
            }

        }
        return true;
    }

    /**
     * 得到一个独一无二的本地文件url
     *
     * @param dir      存放目录
     * @param fileName 网络文件名
     * @return 本地文件url
     */
    private String convertToLocalFileName(String dir, String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex <= 0) {
            //throw new IllegalDataFileException(" '.' not find ");
            return null;
        }
        String suffix = fileName.substring(lastIndex);
        return dir + "\\" + UUID.randomUUID().toString() + suffix;
    }

    private boolean isDataFile(String fileName) {
        return fileName.endsWith(".csv");
    }

    /**
     * 数据预处理  按照测试项要求处理当前行  统计总值 最大值 最小值 判限内个数 判限外个数  冗余数据处理
     *
     * @param columnInfoList 测试项列表
     * @param curLine        当前行
     * @param deleteFail     删除fail FT&RT模式且为FT数据时为true
     * @return String 处理后的当前行
     */
    private String preprocessData(List columnInfoList, Pattern passPattern, Pattern failPattern, String curLine,boolean deleteFail) {
        if (columnInfoList == null) {
            throw new DataNotFoundException("没有提供测试项信息");
        }
        String[] datas = curLine.split(",");
        int dutPass = dataInfo.getDutPassCol();

        if (dutPass >= datas.length) {
            return curLine;
        }
        int length = columnInfoList.size();


        if (passPattern.matcher(curLine).find()) {
            //如果是pass的
            for (int i = 0; i < length; i++) {
                ColumnInfo columnInfo = (ColumnInfo) columnInfoList.get(i);
                String colDataStr = datas[columnInfo.getId()];
                double colData = 0.0;
                if (StringUtils.isEmpty(colDataStr)) {
                    continue;
                }
                colData = Double.parseDouble(colDataStr);
                recordPassData(colData,columnInfo,true);
            }
        } else if (failPattern.matcher(curLine).find()) {
            //如果是fail的double limitMin=
            //FT&RT模式且为FT数据时  返回"" 即删除当前行 且不记录任何数据
            if(deleteFail){
                return "";
            }
            boolean isFailFind = false;
            for (int i = 0; i < length; i++) {
                ColumnInfo columnInfo = (ColumnInfo) columnInfoList.get(i);
                if (datas.length <= columnInfo.getId()) {
                    break;
                }
                if (isFailFind) {
                    datas[columnInfo.getId()] = "";
                    continue;
                }

                String colDataStr = datas[columnInfo.getId()];
                if (StringUtils.isEmpty(colDataStr)) {
                    continue;
                }
                double colData = Double.parseDouble(colDataStr);
                double limitMin = columnInfo.getLimitMin();
                double limitMax = columnInfo.getLimitMax();

                if (colData > limitMin && colData < limitMax) {
                    //如果 数据在判限内
                    recordPassData(colData,columnInfo,false);
                    continue;
                } else if (colData < limitMin || colData > limitMax) {
                    //如果数据在判限外
                    isFailFind = true;
                } else {
                    //如果数据等于判限 那么情况就比较复杂了     SB chuangchuan 的坑
                    //如果下一列有没有数据 在不在判限内 有且在就continue
                    //否则这就是fail列了
                    int id = columnInfo.getId() + 1;
                    String nextColDataStr = datas[columnInfo.getId()];
                    if (StringUtils.isNotEmpty(nextColDataStr)
                    ||((datas.length > id)
                            && length > id)) {
                        ColumnInfo nextColumn = (ColumnInfo) columnInfoList.get(id);
                        double nextColData = Double.parseDouble(nextColDataStr);
                        if (nextColData >= nextColumn.getLimitMin()
                                && nextColData <= nextColumn.getLimitMax()) {
                            recordPassData(colData,columnInfo,false);
                            continue;
                        }
                    }
                    isFailFind = true;
                }
                recordFailData(colData,columnInfo);
            }
        } else {
            //anything else   do nothing
        }
        return StringUtils.join(datas, ",");
    }

    /**
     * 记录pass芯片的passData数据 fail芯片不记录
     * @param colData
     * @param columnInfo
     */
    private void recordPassData(double colData,ColumnInfo columnInfo,boolean isPassChip){
        //记录数据
        recordData(colData,columnInfo);

        //fail芯片的pass数据丢弃
        if(!isPassChip){
            return;
        }
        long totalCountInLimit = columnInfo.getTotalCountInLimit() + 1;
        double totalValueInLimit = columnInfo.getTotalValueInLimit() + colData;
        double realMin = columnInfo.getRealMinInLimit();
        double realMax = columnInfo.getRealMaxInLimit();
        realMin = colData < realMin ? colData : realMin;
        realMax = colData > realMax ? colData : realMax;

        columnInfo.setTotalValueInLimit(totalValueInLimit);
        columnInfo.setTotalCountInLimit(totalCountInLimit);
        columnInfo.setRealMinInLimit(realMin);
        columnInfo.setRealMaxInLimit(realMax);
    }

    /**
     * 记录fail芯片的failData的数据
     * @param colData
     * @param columnInfo
     */
    private void recordFailData(double colData,ColumnInfo columnInfo){
        //记录数据
        recordData(colData,columnInfo);

        long totalCountOutOfLimit = columnInfo.getTotalCountOutOfLimit() + 1;
        double totalValueOutOfLimit = columnInfo.getTotalValueOutOfLimit() + colData;
        double realMax = columnInfo.getRealMaxOutOfLimit();
        double realMin = columnInfo.getRealMinOutOfLimit();
        realMin = colData < realMin ? colData : realMin;
        realMax = colData > realMax ? colData : realMax;

        columnInfo.setTotalValueOutOfLimit(totalValueOutOfLimit);
        columnInfo.setTotalCountOutOfLimit(totalCountOutOfLimit);
        columnInfo.setRealMinOutOfLimit(realMin);
        columnInfo.setRealMaxOutOfLimit(realMax);
    }

    /**
     * 记录数据  不管是pass还是fail都会记录
     * @param colData
     * @param columnInfo
     */
    private void recordData(double colData,ColumnInfo columnInfo){
        long totalCount=columnInfo.getTotalCountAll()+1;
        double totalValue = columnInfo.getTotalValue()+colData;
        columnInfo.setTotalCountAll(totalCount);
        columnInfo.setTotalValue(totalValue);
    }
    /**
     * 根据dataConfig定义的正则来获取测试项
     *
     * @param dataConfig 包含一些正则
     * @return List<ColumnInfo> 测试项列表
     */
    private List<ColumnInfo> getInitialColumnInfos(DataConfig dataConfig) {
        if (dataInfo == null) {
            throw new AnalysisException("dataInfo 为 null");
        }
        if (dataInfo.getColumns()!=null && dataInfo.getColumns().size()>0) {
            return dataInfo.getColumns();
        }
        String testItemStr = dataInfo.getTestItemStr();
        String limitMinStr = dataInfo.getLimitMinStr();
        String limitMaxStr = dataInfo.getLimitMaxStr();
        String limitUnitStr = dataInfo.getLimitUnitStr();
        if (testItemStr == null || limitMinStr == null || limitMaxStr == null) {
            throw new AnalysisException("未知的testItemStr||limitMinStr||limitMaxStr");
        }
        List<ColumnInfo> columns = new ArrayList<ColumnInfo>();

        String[] testItems = testItemStr.split(",");
        String[] limitMins = limitMinStr.split(",");
        String[] limitMaxs = limitMaxStr.split(",");
        String[] limitUnits = null;
        if (limitUnitStr != null) limitUnits = limitUnitStr.split(",");
        Pattern osPattern = Pattern.compile(dataConfig.getTestItemColumnFlag().replace(",", "^"));
        Pattern passPattern = Pattern.compile(dataConfig.getDutPassColumnFlag().replace(",", ""));
        Pattern sitePattern = Pattern.compile(dataConfig.getSiteNoColumnFlag().replace(",", ""));
        Pattern indexPattern = Pattern.compile(dataConfig.getDutNoColumnFlag().replace(",", ""));
        Pattern ignorePattern = Pattern.compile(dataConfig.getIgnoreColumnFlag());
        boolean findTestItem = false;
        for (int i = 0; i < testItems.length; i++) {
            if (findTestItem == false) {
                //找到dut_pass列
                if (passPattern.matcher(testItems[i]).find()) {
                    dataInfo.setDutPassCol(i);
                    continue;
                }
                //找到Site_No列
                if (sitePattern.matcher(testItems[i]).find()) {
                    dataInfo.setSiteCol(i);
                    continue;
                }
                //找到dut_no列
                if (indexPattern.matcher(testItems[i]).find()) {
                    dataInfo.setIndexCol(i);
                    continue;
                }
                //找到测试项列
                if (osPattern.matcher(testItems[i]).find()) {
                    findTestItem = true;
                }
            }
            if (findTestItem == true) {
                if (StringUtils.isBlank(testItems[i])) {
                    continue;
                }
                Double min, max;
                String unit;
                Boolean isProcess;
                //min处理
                if (i < limitMins.length) {
                    if (StringUtils.isBlank(limitMins[i])) {
                        min = -Double.MAX_VALUE;
                    } else {
                        min = Double.parseDouble(limitMins[i]);
                        //SB changchuan de 9999.999 chuli
                        if (min < -9998 && min > -10001) {
                            min = -Double.MAX_VALUE;
                        }
                    }

                } else {
                    continue;
                }
                //max处理
                if (i < limitMaxs.length) {
                    if (StringUtils.isBlank(limitMaxs[i])) {
                        max = Double.MAX_VALUE;
                    } else {
                        max = Double.parseDouble(limitMaxs[i]);
                        //SB changchuan de 9999.999 chuli
                        if (max > 9998 && max < 10001) {
                            max = Double.MAX_VALUE;
                        }
                    }
                } else {
                    continue;
                }
                //unit处理
                if (limitUnits != null && i < limitUnits.length) {
                    unit = limitUnits[i];
                } else {
                    unit = "";
                }
                //判断是否要处理
                if (ignorePattern.matcher(testItems[i]).find()
                        || osPattern.matcher(testItems[i]).find()) {
                    isProcess = Boolean.FALSE;
                } else {
                    isProcess = Boolean.TRUE;
                }
                ColumnInfo columnInfo = new ColumnInfo();
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
                columnInfo.setPassGroups(Constants.CHART_GROUPS_PASS_DEFAULT);
                columnInfo.setFailGroups(Constants.CHART_GRUOPS_FAIL_DEFAULT);
                columnInfo.setIsProcess(isProcess);
                columns.add(columnInfo);

                //System.out.println("debug: testItems: "+testItems[i]);
            }

        }
        return columns;
    }


    private void recordTestItemStr(String str) {
        if (dataInfo.getTestItemStr() == null) {
            dataInfo.setTestItemStr(str);
        } else {
            if (!dataInfo.getTestItemStr().equals(str)) {
                throw new TestItemLineNotMatchException(dataInfo.getTestItemStr(), str);
                //return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
            }
        }
    }

    private void recordLimitMinStr(String str) {
        if (dataInfo.getLimitMinStr() == null) {
            dataInfo.setLimitMinStr(str);
        } else {
            if (!dataInfo.getLimitMinStr().equals(str)) {
                throw new LimitLineNotMatchException(dataInfo.getLimitMinStr(), str);
                //return (Constants.PROCESS_STATUS_TESTITEM_NOT_MATCH);
            }
        }
    }

    private void recordLimitMaxStr(String str) {
        if (dataInfo.getLimitMaxStr() == null) {
            dataInfo.setLimitMaxStr(str);
        } else {
            if (!dataInfo.getLimitMaxStr().equals(str)) {
                throw new LimitLineNotMatchException(dataInfo.getLimitMaxStr(), str);
            }
        }
    }

    private void recordLimitUnitStr(String str) {
        if (dataInfo.getLimitUnitStr() == null) {
            dataInfo.setLimitUnitStr(str);
        } else {
            if (!dataInfo.getLimitUnitStr().equals(str)) {
                throw new LimitLineNotMatchException(dataInfo.getLimitMaxStr(), str);
            }
        }
    }

    public DataInfo getDataInfo() {
        return dataInfo;
    }

    public ReportBuilder setDataInfo(DataInfo dataInfo) {
        this.dataInfo = dataInfo;
        return this;
    }

    public DataConfig getDataConfig() {
        return dataConfig;
    }

    public ReportBuilder setDataConfig(DataConfig dataConfig) {
        this.dataConfig = dataConfig;
        return this;
    }
}
