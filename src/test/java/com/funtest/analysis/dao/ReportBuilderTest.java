package com.funtest.analysis.dao;

import com.funtest.analysis.bean.*;
import com.funtest.core.bean.constant.Constants;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class ReportBuilderTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static DataConfig dataConfig;
    private long start = 0;

    @Before
    public void beforeTest() {
        String configJson = "{\"id\":1,\"dutNoColumnFlag\":\"(?i)Dut_NO\",\"limitMinLineFlag\":\"(?i)^(-1|Min),\",\"limitMaxLineFlag\":\"(?i)^(0|Max),\",\"dutPassTrueString\":\"(?i)TRUE\",\"dutPassFalseString\":\"(?i)FALSE\",\"dutPassColumnFlag\":\"(?i)Dut_Pass\",\"siteNoColumnFlag\":\"(?i)Site_No\",\"password\":\"joulwatt\",\"testItemColumnFlag\":\"(?i),(OS|PIN)\",\"ignoreColumnFlag\":\"(?i)_debug$\"}";
        this.dataConfig = new Gson().fromJson(configJson, DataConfig.class);
        start = System.currentTimeMillis();
    }

    @After
    public void afterTest() {
        long end = System.currentTimeMillis();
        logger.info("use time : {}ms", end - start);
    }

    @Test
    public void testIsDataFile() throws InvocationTargetException, IllegalAccessException {
        String[] fileNames = {"xxx.csv", "xxx.scs", "dasdl"};
        /*Class<?> clazz= null;
        try {
			clazz = Class.forName("com.funtest.analysis.dao.ReportBuilder");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ReportBuilder rb= null;
		try {
			rb = (ReportBuilder)clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}*/
        ReportBuilder rb = new ReportBuilder();

        Method isDataFile = null;
        try {
            isDataFile = rb.getClass().getDeclaredMethod("isDataFile", String.class);
            isDataFile.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        assertTrue((Boolean) isDataFile.invoke(rb, fileNames[0]));
        assertFalse((Boolean) isDataFile.invoke(rb, fileNames[1]));
        assertEquals((Boolean) isDataFile.invoke(rb, fileNames[1]), false);
    }

    @Test
    public void testConvertToLocalFileName() throws InvocationTargetException, IllegalAccessException {
        String[] fileNames = {"xxx.csv", "YYY.xls", "hhh"};
        String[] localFileNames = new String[fileNames.length];
        ReportBuilder rb = new ReportBuilder();

        Method convertToLocalFileName = null;
        try {
            convertToLocalFileName = rb.getClass().getDeclaredMethod("convertToLocalFileName", String.class, String.class);
            convertToLocalFileName.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < fileNames.length; i++) {
            localFileNames[i] = (String) convertToLocalFileName.invoke(rb, Constants.FILE_UPLOAD_DIR, fileNames[i]);
        }


        assertEquals(".csv", localFileNames[0].substring(localFileNames[0].lastIndexOf(".")));
        assertEquals(".xls", localFileNames[1].substring(localFileNames[1].lastIndexOf(".")));
        assertNull(localFileNames[2]);

        logger.info("testConvertToLocalFileName():{},{},{}", localFileNames);
    }

    @Test
    public void testIsTestItemLine() throws InvocationTargetException, IllegalAccessException {
        DataConfig dataConfig = this.dataConfig;
        Pattern patternDutNoColumn = Pattern.compile(dataConfig.getDutNoColumnFlag());
        Pattern patternSiteNoColumn = Pattern.compile(dataConfig.getSiteNoColumnFlag());
        Pattern patternDutPassColumn = Pattern.compile(dataConfig.getDutPassColumnFlag());
        Pattern patternTestItemColumnFlag = Pattern.compile(dataConfig.getTestItemColumnFlag());

        ReportBuilder rb = new ReportBuilder();
        Method isTestItemLine = null;
        try {

            isTestItemLine = rb.getClass().getDeclaredMethod("isTestItemLine", String.class, Pattern[].class);
            isTestItemLine.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        String testLine1 = "ads,adasdm,dadsa";
        Pattern[] patterns = {patternDutNoColumn, patternSiteNoColumn, patternDutPassColumn, patternTestItemColumnFlag};
        assertFalse((Boolean) isTestItemLine.invoke(rb, testLine1, patterns));

        String testLine2 = "Dut_No,Site_No,Dut_Pass,SW_Bin,Data_Num,OS_VCC,OS_SW,Vf,Isw,Ivcc,garbage,,Vcc,Vcc1,Icc_5V,Icc_1V,Icc3,Vcc_on,Vcc_off,SW_Freq_debug,BlankingTime,Tbodydiode,Ivcc_GateOn,Iq";
        assertTrue((Boolean) isTestItemLine.invoke(rb, testLine2, patterns));
    }

    @Test
    public void testGetInitialColumnInfos() throws InvocationTargetException, IllegalAccessException {
        DataInfo dataInfo = new DataInfo();
        dataInfo.setTestItemStr("Dut_No,Site_No,Dut_Pass,SW_Bin,Data_Num,OS_VCC,OS_SW,Vf,Isw,Ivcc,garbage,,Vcc,Vcc1,Icc_5V,Icc_1V,Icc3,Vcc_on,Vcc_off,SW_Freq_debug,BlankingTime,Tbodydiode,Ivcc_GateOn,Iq");
        dataInfo.setLimitMinStr("-1,-1,TRUE,-1,-1,-0.9,-0.9,-0.7,40,220,,,4.33,7,-37,-26,69,3.75,3.5,-9999.9999,2.1,-0.001,1,68");
        dataInfo.setLimitMaxStr("0,0,FALSE,0,0,-0.3,-0.3,-0.3,60,340,,,4.59,8.5,-27,-14,103,4.05,3.8,9999.9999,3.61,0.001,3.5,102");
        dataInfo.setLimitUnitStr(",,,,,,,,,,,,,,,,,,,,,,,");

        ReportBuilder rb = new ReportBuilder();
        rb.setDataInfo(dataInfo);
        Method getInitialColumnInfos = null;
        try {
            getInitialColumnInfos = rb.getClass().getDeclaredMethod("getInitialColumnInfos", DataConfig.class);
            getInitialColumnInfos.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        List<ColumnInfo> columnInfos = (List<ColumnInfo>) getInitialColumnInfos.invoke(rb, this.dataConfig);

        String columnsJson = "[{\"id\":5,\"columnName\":\"OS_VCC\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":6,\"columnName\":\"OS_SW\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":7,\"columnName\":\"Vf\",\"limitMin\":-0.7,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":8,\"columnName\":\"Isw\",\"limitMin\":40.0,\"limitMax\":60.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":9,\"columnName\":\"Ivcc\",\"limitMin\":220.0,\"limitMax\":340.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":10,\"columnName\":\"garbage\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":12,\"columnName\":\"Vcc\",\"limitMin\":4.33,\"limitMax\":4.59,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":13,\"columnName\":\"Vcc1\",\"limitMin\":7.0,\"limitMax\":8.5,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":14,\"columnName\":\"Icc_5V\",\"limitMin\":-37.0,\"limitMax\":-27.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":15,\"columnName\":\"Icc_1V\",\"limitMin\":-26.0,\"limitMax\":-14.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":16,\"columnName\":\"Icc3\",\"limitMin\":69.0,\"limitMax\":103.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":17,\"columnName\":\"Vcc_on\",\"limitMin\":3.75,\"limitMax\":4.05,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":18,\"columnName\":\"Vcc_off\",\"limitMin\":3.5,\"limitMax\":3.8,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":19,\"columnName\":\"SW_Freq_debug\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":20,\"columnName\":\"BlankingTime\",\"limitMin\":2.1,\"limitMax\":3.61,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":21,\"columnName\":\"Tbodydiode\",\"limitMin\":-0.001,\"limitMax\":0.001,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":22,\"columnName\":\"Ivcc_GateOn\",\"limitMin\":1.0,\"limitMax\":3.5,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":23,\"columnName\":\"Iq\",\"limitMin\":68.0,\"limitMax\":102.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0}]";
        List<ColumnInfo> myColumnInfos = Arrays.asList(new Gson().fromJson(columnsJson, ColumnInfo[].class));
        System.out.println("class:" + columnInfos.getClass());
        assertEquals(myColumnInfos.size(), columnInfos.size());
        assertEquals(myColumnInfos.get(0).getColumnName(), columnInfos.get(0).getColumnName());
        assertEquals(myColumnInfos.get(0).getLimitMin(), columnInfos.get(0).getLimitMin(), .0000001);
        assertEquals(myColumnInfos.get(0).getLimitMax(), columnInfos.get(0).getLimitMax(), .0000001);
        assertEquals(myColumnInfos.get(0).getColumnName(), columnInfos.get(0).getColumnName());
        logger.info("testGetInitialColumnInfos() ColumnInfos:{}", new Gson().toJson(columnInfos));
    }

    @Test
    public void testPreprocessData() throws InvocationTargetException, IllegalAccessException {
        DataConfig dataConfig = this.dataConfig;
        Pattern passPattern = Pattern.compile(dataConfig.getDutPassTrueString());
        Pattern failPattern = Pattern.compile(dataConfig.getDutPassFalseString());
        String columnsJson = "[{\"id\":5,\"columnName\":\"OS_VCC\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":6,\"columnName\":\"OS_SW\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":7,\"columnName\":\"Vf\",\"limitMin\":-0.7,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":8,\"columnName\":\"Isw\",\"limitMin\":40.0,\"limitMax\":60.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":9,\"columnName\":\"Ivcc\",\"limitMin\":220.0,\"limitMax\":340.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":10,\"columnName\":\"garbage\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":12,\"columnName\":\"Vcc\",\"limitMin\":4.33,\"limitMax\":4.59,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":13,\"columnName\":\"Vcc1\",\"limitMin\":7.0,\"limitMax\":8.5,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":14,\"columnName\":\"Icc_5V\",\"limitMin\":-37.0,\"limitMax\":-27.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":15,\"columnName\":\"Icc_1V\",\"limitMin\":-26.0,\"limitMax\":-14.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":16,\"columnName\":\"Icc3\",\"limitMin\":69.0,\"limitMax\":103.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":17,\"columnName\":\"Vcc_on\",\"limitMin\":3.75,\"limitMax\":4.05,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":18,\"columnName\":\"Vcc_off\",\"limitMin\":3.5,\"limitMax\":3.8,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":19,\"columnName\":\"SW_Freq_debug\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":20,\"columnName\":\"BlankingTime\",\"limitMin\":2.1,\"limitMax\":3.61,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":21,\"columnName\":\"Tbodydiode\",\"limitMin\":-0.001,\"limitMax\":0.001,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":22,\"columnName\":\"Ivcc_GateOn\",\"limitMin\":1.0,\"limitMax\":3.5,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":23,\"columnName\":\"Iq\",\"limitMin\":68.0,\"limitMax\":102.0,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0}]";
        List<ColumnInfo> columnInfos = Arrays.asList(new Gson().fromJson(columnsJson, ColumnInfo[].class));

        ReportBuilder rb = new ReportBuilder();
        rb.setDataInfo(new DataInfo().setDutPassCol(2));
        Method preprocessData = null;
        try {
            preprocessData = rb.getClass().getDeclaredMethod("preprocessData",
                    List.class,
                    Pattern.class, Pattern.class,
                    String.class,
                    boolean.class);
            preprocessData.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        String[] lines = {
                "2,0,TRUE,1,17,-0.5068,-0.4791,-0.607,49.6151,297.2035,,,4.3829,7.7596,-30.5558,-18.284,78.697,3.82,3.64,10.9637,3.4,0,1.214,83.6636",
                "43,0,FALSE,3,4,-0.5059,-0.4706,-0.61,61.8249,22222,,,,,,,,,,,,,,"
        };
        boolean deleteFail=false;
        String str0 = (String) preprocessData.invoke(rb, columnInfos, passPattern, failPattern, lines[0],deleteFail);
        String str1 = (String) preprocessData.invoke(rb, columnInfos, passPattern, failPattern, lines[1],deleteFail);
        String str2 = (String) preprocessData.invoke(rb, columnInfos, passPattern, failPattern, lines[1],!deleteFail);
        logger.info("columnInfos:{}", new Gson().toJson(columnInfos));
        assertEquals(lines[0], str0);
        assertEquals("43,0,FALSE,3,4,-0.5059,-0.4706,-0.61,61.8249,", str1);
        assertEquals(1, columnInfos.get(3).getTotalCountOutOfLimit());
        assertEquals("",str2);
        logger.info("columnInfos:{}", new Gson().toJson(columnInfos));

    }


    @Test
    public void testBuildDataInfo() throws IOException {
        ReportBuilder rb = new ReportBuilder();
        String configJson = "{\"id\":1,\"dutNoColumnFlag\":\"(?i)Dut_NO\",\"limitMinLineFlag\":\"(?i)^(-1|Min),\",\"limitMaxLineFlag\":\"(?i)^(0|Max),\",\"dutPassTrueString\":\"(?i)TRUE\",\"dutPassFalseString\":\"(?i)FALSE\",\"dutPassColumnFlag\":\"(?i)Dut_Pass\",\"siteNoColumnFlag\":\"(?i)Site_No\",\"password\":\"joulwatt\",\"testItemColumnFlag\":\"(?i),(OS|PIN)\",\"ignoreColumnFlag\":\"(?i)_debug$\"}";
        String dataInfoJson ="{\"reportName\":\"report1212\",\"chipName\":\"JW7878\",\"mode\":0,\"columns\":[{\"id\":5,\"columnName\":\"OS_VCC\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.509,\"realMaxInLimit\":-0.3429,\"realAverage\":-0.508576374018558,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":-1.1436,\"realMaxOutOfLimit\":-0.0374,\"isProcess\":false,\"passGroups\":100,\"failGroups\":20,\"totalValue\":-1425.0309999999997,\"totalCountAll\":2802,\"totalCountInLimit\":2786,\"totalCountOutOfLimit\":16},{\"id\":6,\"columnName\":\"OS_SW\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.5033,\"realMaxInLimit\":-0.3916,\"realAverage\":-0.4789445082555634,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":-0.2895,\"realMaxOutOfLimit\":-0.2895,\"isProcess\":false,\"passGroups\":100,\"failGroups\":20,\"totalValue\":-1334.3393999999996,\"totalCountAll\":2786,\"totalCountInLimit\":2784,\"totalCountOutOfLimit\":2},{\"id\":7,\"columnName\":\"Vf\",\"limitMin\":-0.7,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.628,\"realMaxInLimit\":-0.552,\"realAverage\":-0.6072191091954042,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":-1690.4980000000053,\"totalCountAll\":2784,\"totalCountInLimit\":2784,\"totalCountOutOfLimit\":0},{\"id\":8,\"columnName\":\"Isw\",\"limitMin\":40.0,\"limitMax\":60.0,\"limitUnit\":\"\",\"realMinInLimit\":49.3434,\"realMaxInLimit\":59.2324,\"realAverage\":61.13291659482754,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":60.8859,\"realMaxOutOfLimit\":500.7632,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":170194.03979999988,\"totalCountAll\":2784,\"totalCountInLimit\":2686,\"totalCountOutOfLimit\":98},{\"id\":9,\"columnName\":\"Ivcc\",\"limitMin\":220.0,\"limitMax\":340.0,\"limitUnit\":\"\",\"realMinInLimit\":229.8137,\"realMaxInLimit\":313.7296,\"realAverage\":280.93959113924086,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":357.4558,\"realMaxOutOfLimit\":357.4558,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":754603.7418000009,\"totalCountAll\":2686,\"totalCountInLimit\":2684,\"totalCountOutOfLimit\":2},{\"id\":10,\"columnName\":\"garbage\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":100,\"failGroups\":20,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":12,\"columnName\":\"Vcc\",\"limitMin\":4.33,\"limitMax\":4.59,\"limitUnit\":\"\",\"realMinInLimit\":4.3753,\"realMaxInLimit\":4.3985,\"realAverage\":4.382183830104323,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":11761.781400000003,\"totalCountAll\":2684,\"totalCountInLimit\":2684,\"totalCountOutOfLimit\":0},{\"id\":13,\"columnName\":\"Vcc1\",\"limitMin\":7.0,\"limitMax\":8.5,\"limitUnit\":\"\",\"realMinInLimit\":7.6288,\"realMaxInLimit\":7.8994,\"realAverage\":7.768901788375562,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":20851.732400000008,\"totalCountAll\":2684,\"totalCountInLimit\":2684,\"totalCountOutOfLimit\":0},{\"id\":14,\"columnName\":\"Icc_5V\",\"limitMin\":-37.0,\"limitMax\":-27.0,\"limitUnit\":\"\",\"realMinInLimit\":-34.2478,\"realMaxInLimit\":-29.5495,\"realAverage\":-31.952747690014863,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":-85761.17479999989,\"totalCountAll\":2684,\"totalCountInLimit\":2684,\"totalCountOutOfLimit\":0},{\"id\":15,\"columnName\":\"Icc_1V\",\"limitMin\":-26.0,\"limitMax\":-14.0,\"limitUnit\":\"\",\"realMinInLimit\":-20.88,\"realMaxInLimit\":-17.762,\"realAverage\":-19.426289865871812,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":-52140.161999999946,\"totalCountAll\":2684,\"totalCountInLimit\":2684,\"totalCountOutOfLimit\":0},{\"id\":16,\"columnName\":\"Icc3\",\"limitMin\":69.0,\"limitMax\":103.0,\"limitUnit\":\"\",\"realMinInLimit\":75.6541,\"realMaxInLimit\":88.1004,\"realAverage\":79.84458055141579,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":214302.85419999997,\"totalCountAll\":2684,\"totalCountInLimit\":2684,\"totalCountOutOfLimit\":0},{\"id\":17,\"columnName\":\"Vcc_on\",\"limitMin\":3.75,\"limitMax\":4.05,\"limitUnit\":\"\",\"realMinInLimit\":3.78,\"realMaxInLimit\":4.01,\"realAverage\":3.8327272727272854,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":10287.040000000034,\"totalCountAll\":2684,\"totalCountInLimit\":2684,\"totalCountOutOfLimit\":0},{\"id\":18,\"columnName\":\"Vcc_off\",\"limitMin\":3.5,\"limitMax\":3.8,\"limitUnit\":\"\",\"realMinInLimit\":3.62,\"realMaxInLimit\":3.7,\"realAverage\":3.651751117734652,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":4.0,\"realMaxOutOfLimit\":4.0,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":9801.299999999806,\"totalCountAll\":2684,\"totalCountInLimit\":2682,\"totalCountOutOfLimit\":2},{\"id\":19,\"columnName\":\"SW_Freq_debug\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":0.0,\"realMaxInLimit\":113.6364,\"realAverage\":20.54676099925397,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":100,\"failGroups\":20,\"totalValue\":55106.41299999915,\"totalCountAll\":2682,\"totalCountInLimit\":2682,\"totalCountOutOfLimit\":0},{\"id\":20,\"columnName\":\"BlankingTime\",\"limitMin\":2.1,\"limitMax\":3.61,\"limitUnit\":\"\",\"realMinInLimit\":3.2,\"realMaxInLimit\":3.6,\"realAverage\":3.4432346010439256,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":2.0,\"realMaxOutOfLimit\":8.8888,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":9234.755199999809,\"totalCountAll\":2682,\"totalCountInLimit\":2672,\"totalCountOutOfLimit\":10},{\"id\":21,\"columnName\":\"Tbodydiode\",\"limitMin\":-0.001,\"limitMax\":0.001,\"limitUnit\":\"\",\"realMinInLimit\":0.0,\"realMaxInLimit\":0.0,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":0.0,\"totalCountAll\":2672,\"totalCountInLimit\":2672,\"totalCountOutOfLimit\":0},{\"id\":22,\"columnName\":\"Ivcc_GateOn\",\"limitMin\":1.0,\"limitMax\":3.5,\"limitUnit\":\"\",\"realMinInLimit\":1.154,\"realMaxInLimit\":1.289,\"realAverage\":1.20852395209581,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":0.776,\"realMaxOutOfLimit\":0.839,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":3229.176000000004,\"totalCountAll\":2672,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":4},{\"id\":23,\"columnName\":\"Iq\",\"limitMin\":68.0,\"limitMax\":102.0,\"limitUnit\":\"\",\"realMinInLimit\":79.941,\"realMaxInLimit\":90.0453,\"realAverage\":84.53574302848548,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":100,\"failGroups\":20,\"totalValue\":225541.36239999926,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0}],\"files\":[{\"fileName\":\"hello.txt\",\"message\":\"不是csv文件\",\"status\":1001},{\"fileName\":\"JW7707.csv\",\"localFileName\":\"upload\\\\40d9e6f4-5022-4008-aeca-0678be3777c3.csv\",\"status\":1111,\"DataStartLine\":7},{\"fileName\":\"rubbish.csv\",\"message\":\"未发现测试数据，错误代码：1002\",\"status\":1005},{\"fileName\":\"JW7707v2.csv\",\"localFileName\":\"upload\\\\b29dc7a2-e5d2-4e90-b50a-f592e6a9b63b.csv\",\"status\":1111,\"DataStartLine\":7}],\"testItemStr\":\"Dut_No,Site_No,Dut_Pass,SW_Bin,Data_Num,OS_VCC,OS_SW,Vf,Isw,Ivcc,garbage,,Vcc,Vcc1,Icc_5V,Icc_1V,Icc3,Vcc_on,Vcc_off,SW_Freq_debug,BlankingTime,Tbodydiode,Ivcc_GateOn,Iq,\",\"limitMinStr\":\"-1,-1,TRUE,-1,-1,-0.9,-0.9,-0.7,40,220,,,4.33,7,-37,-26,69,3.75,3.5,-9999.9999,2.1,-0.001,1,68\",\"limitMaxStr\":\"0,0,FALSE,0,0,-0.3,-0.3,-0.3,60,340,,,4.59,8.5,-27,-14,103,4.05,3.8,9999.9999,3.61,0.001,3.5,102\",\"limitUnitStr\":\",,,,,,,,,,,,,,,,,,,,,,,\",\"dutPassCol\":2,\"indexCol\":0,\"siteCol\":1}";
        String[] fileNames = {"hello.txt", "FT1234.csv", "rubbish.csv"};
        List<InputStream> ins = new ArrayList<InputStream>();
        for (String fileName : fileNames) {
            Resource resource = new ClassPathResource("file/" + fileName);
            InputStream in = resource.getInputStream();
            ins.add(in);
        }
        int size = ins.size();
        DataConfig dataConfig = new Gson().fromJson(configJson, DataConfig.class);
        DataInfo myDataInfo = new Gson().fromJson(dataInfoJson, DataInfo.class);
        rb.setDataConfig(dataConfig);
        long start, end;
        start = System.currentTimeMillis();
        DataInfo dataInfo = rb.buildDataInfo(fileNames, (InputStream[]) ins.toArray(new InputStream[size]), "report1212", "JW7878", Constants.PROCESS_MODE_NORMAL);
        end = System.currentTimeMillis();
        List<ColumnInfo> columns = dataInfo.getColumns();
        /*List<ColumnInfo> myColumns = myDataInfo.getColumns();

        //全面的测试
        for (int i = 0; i < columns.size(); i++) {
            ColumnInfo columnInfo = columns.get(i);
            ColumnInfo myColumnInfo = myColumns.get(i);
            assertEquals(myColumnInfo.getColumnName(), columnInfo.getColumnName());
            assertEquals(myColumnInfo.getId(), columnInfo.getId());
            assertEquals(myColumnInfo.getRealMinInLimit(), columnInfo.getRealMinInLimit(), 0.0001);
            assertEquals(myColumnInfo.getRealMaxInLimit(), columnInfo.getRealMaxInLimit(), 0.0001);
            assertEquals(myColumnInfo.getRealMinOutOfLimit(), columnInfo.getRealMinOutOfLimit(), 0.0001);
            assertEquals(myColumnInfo.getRealMaxOutOfLimit(), columnInfo.getRealMaxOutOfLimit(), 0.0001);
            assertEquals(myColumnInfo.getTotalCountInLimit(), columnInfo.getTotalCountInLimit());
            assertEquals(myColumnInfo.getTotalCountOutOfLimit(), columnInfo.getTotalCountOutOfLimit());
            assertEquals(myColumnInfo.getRealAverage(), columnInfo.getRealAverage(), 0.0001);
        }*/
        ColumnInfo columnInfo=columns.get(3);
        //列名
        assertEquals("Isw555",columnInfo.getColumnName());
        //数量
        assertEquals(1392,columnInfo.getTotalCountAll());
        assertEquals(1334,columnInfo.getTotalCountInLimit());
        assertEquals(49,columnInfo.getTotalCountOutOfLimit());

        //平均值
        double accuracy=0.0001;
        assertEquals(61.1329,columnInfo.getRealAverage(),accuracy);
        assertEquals(50.8271,columnInfo.getRealAverageInLimit(),accuracy);
        assertEquals(343.5754,columnInfo.getRealAverageOutOfLimit(),accuracy);

        /*
        assertEquals(0, columns.get(5).getTotalCountOutOfLimit());
        assertEquals(0, columns.get(5).getTotalCountInLimit());
        assertFalse(columns.get(0).getIsProcess());
        assertFalse(columns.get(5).getIsProcess());
        assertTrue(columns.get(10).getIsProcess());*/
        logger.info("column 'Isw555': {}",new Gson().toJson(columnInfo));
        logger.info("testBuildDataInfo() use time: {} ms", end - start);
    }

    @Test
    public void testGetInitalReportItems() throws InvocationTargetException, IllegalAccessException {
        ReportBuilder rb = new ReportBuilder();
        String dataInfoJson = "{\"reportName\":\"report1212\",\"chipName\":\"JW7878\",\"mode\":0,\"columns\":[{\"id\":5,\"columnName\":\"OS_VCC\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.509,\"realMaxInLimit\":-0.5053,\"realAverage\":-0.5088583457526077,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":-1.1436,\"realMaxOutOfLimit\":-0.0374,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-1365.775799999999,\"totalCountAll\":2684,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":16},{\"id\":6,\"columnName\":\"OS_SW\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.5013,\"realMaxInLimit\":-0.4613,\"realAverage\":-0.4789851685393253,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":-0.2895,\"realMaxOutOfLimit\":-0.2895,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-1278.8903999999986,\"totalCountAll\":2670,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":2},{\"id\":7,\"columnName\":\"Vf\",\"limitMin\":-0.7,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.628,\"realMaxInLimit\":-0.596,\"realAverage\":-0.6070967016491776,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-1619.7340000000058,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":8,\"columnName\":\"Isw\",\"limitMin\":40.0,\"limitMax\":60.0,\"limitUnit\":\"\",\"realMinInLimit\":49.3434,\"realMaxInLimit\":59.2324,\"realAverage\":61.19922950108454,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":60.8859,\"realMaxOutOfLimit\":500.7632,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":169277.06879999983,\"totalCountAll\":2766,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":98},{\"id\":9,\"columnName\":\"Ivcc\",\"limitMin\":220.0,\"limitMax\":340.0,\"limitUnit\":\"\",\"realMinInLimit\":247.7224,\"realMaxInLimit\":312.5095,\"realAverage\":280.9707630711614,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":357.4558,\"realMaxOutOfLimit\":357.4558,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":750191.9374000009,\"totalCountAll\":2670,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":2},{\"id\":10,\"columnName\":\"garbage\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":12,\"columnName\":\"Vcc\",\"limitMin\":4.33,\"limitMax\":4.59,\"limitUnit\":\"\",\"realMinInLimit\":4.3753,\"realMaxInLimit\":4.3939,\"realAverage\":4.382166641679163,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":11691.620600000006,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":13,\"columnName\":\"Vcc1\",\"limitMin\":7.0,\"limitMax\":8.5,\"limitUnit\":\"\",\"realMinInLimit\":7.6288,\"realMaxInLimit\":7.8994,\"realAverage\":7.76893350824588,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":20727.514600000006,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":14,\"columnName\":\"Icc_5V\",\"limitMin\":-37.0,\"limitMax\":-27.0,\"limitUnit\":\"\",\"realMinInLimit\":-34.2478,\"realMaxInLimit\":-29.5495,\"realAverage\":-31.951552173913008,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-85246.7411999999,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":15,\"columnName\":\"Icc_1V\",\"limitMin\":-26.0,\"limitMax\":-14.0,\"limitUnit\":\"\",\"realMinInLimit\":-20.88,\"realMaxInLimit\":-17.762,\"realAverage\":-19.42581484257869,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-51828.07399999994,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":16,\"columnName\":\"Icc3\",\"limitMin\":69.0,\"limitMax\":103.0,\"limitUnit\":\"\",\"realMinInLimit\":75.6541,\"realMaxInLimit\":85.2761,\"realAverage\":79.8413751874063,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":213016.789,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":17,\"columnName\":\"Vcc_on\",\"limitMin\":3.75,\"limitMax\":4.05,\"limitUnit\":\"\",\"realMinInLimit\":3.78,\"realMaxInLimit\":3.89,\"realAverage\":3.832638680659683,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":10225.480000000034,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":18,\"columnName\":\"Vcc_off\",\"limitMin\":3.5,\"limitMax\":3.8,\"limitUnit\":\"\",\"realMinInLimit\":3.62,\"realMaxInLimit\":3.7,\"realAverage\":3.651782771535509,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":4.0,\"realMaxOutOfLimit\":4.0,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":9750.25999999981,\"totalCountAll\":2670,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":2},{\"id\":19,\"columnName\":\"SW_Freq_debug\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":0.0,\"realMaxInLimit\":69.8812,\"realAverage\":20.41714137931003,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":54472.93319999916,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":20,\"columnName\":\"BlankingTime\",\"limitMin\":2.1,\"limitMax\":3.61,\"limitUnit\":\"\",\"realMinInLimit\":3.2,\"realMaxInLimit\":3.6,\"realAverage\":3.4430004480955225,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":2.0,\"realMaxOutOfLimit\":8.8888,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":9220.355199999809,\"totalCountAll\":2678,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":10},{\"id\":21,\"columnName\":\"Tbodydiode\",\"limitMin\":-0.001,\"limitMax\":0.001,\"limitUnit\":\"\",\"realMinInLimit\":0.0,\"realMaxInLimit\":0.0,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":22,\"columnName\":\"Ivcc_GateOn\",\"limitMin\":1.0,\"limitMax\":3.5,\"limitUnit\":\"\",\"realMinInLimit\":1.154,\"realMaxInLimit\":1.289,\"realAverage\":1.20852395209581,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":0.776,\"realMaxOutOfLimit\":0.839,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":3229.176000000004,\"totalCountAll\":2672,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":4},{\"id\":23,\"columnName\":\"Iq\",\"limitMin\":68.0,\"limitMax\":102.0,\"limitUnit\":\"\",\"realMinInLimit\":79.941,\"realMaxInLimit\":90.0453,\"realAverage\":84.53574302848548,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":225541.36239999926,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0}],\"files\":[{\"fileName\":\"hello.txt\",\"message\":\"不是csv文件\",\"status\":1001},{\"fileName\":\"JW7707.csv\",\"localFileName\":\"upload\\\\5b2cd44d-d08d-4901-a768-4b607114d47d.csv\",\"status\":1111,\"DataStartLine\":7},{\"fileName\":\"rubbish.csv\",\"message\":\"未发现测试数据，错误代码：1002\",\"status\":1005},{\"fileName\":\"JW7707v2.csv\",\"localFileName\":\"upload\\\\6c2a3b39-f1e0-438b-8380-850f0fa9a1fa.csv\",\"status\":1111,\"DataStartLine\":7}],\"testItemStr\":\"Dut_No,Site_No,Dut_Pass,SW_Bin,Data_Num,OS_VCC,OS_SW,Vf,Isw,Ivcc,garbage,,Vcc,Vcc1,Icc_5V,Icc_1V,Icc3,Vcc_on,Vcc_off,SW_Freq_debug,BlankingTime,Tbodydiode,Ivcc_GateOn,Iq,\",\"limitMinStr\":\"-1,-1,TRUE,-1,-1,-0.9,-0.9,-0.7,40,220,,,4.33,7,-37,-26,69,3.75,3.5,-9999.9999,2.1,-0.001,1,68\",\"limitMaxStr\":\"0,0,FALSE,0,0,-0.3,-0.3,-0.3,60,340,,,4.59,8.5,-27,-14,103,4.05,3.8,9999.9999,3.61,0.001,3.5,102\",\"limitUnitStr\":\",,,,,,,,,,,,,,,,,,,,,,,\",\"dutPassCol\":2,\"indexCol\":0,\"siteCol\":1}";
        DataInfo dataInfo = new Gson().fromJson(dataInfoJson, DataInfo.class);
        List<ColumnInfo> columnInfos = dataInfo.getColumns();

        Method getInitalReportItems = null;
        try {
            getInitalReportItems = rb.getClass().getDeclaredMethod("getInitialReportItems", List.class);
            getInitalReportItems.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //invoke
        List<ReportItem> reportItemList = (List<ReportItem>) getInitalReportItems.invoke(rb, columnInfos);

        logger.info("reportItems:{}", new Gson().toJson(reportItemList));
    }

    @Test
    public void testProcessFile() throws InvocationTargetException, IllegalAccessException, IOException {
        ReportBuilder rb = new ReportBuilder();
        String configJson = "{\"id\":1,\"dutNoColumnFlag\":\"(?i)Dut_NO\",\"limitMinLineFlag\":\"(?i)^(-1|Min),\",\"limitMaxLineFlag\":\"(?i)^(0|Max),\",\"dutPassTrueString\":\"(?i)TRUE\",\"dutPassFalseString\":\"(?i)FALSE\",\"dutPassColumnFlag\":\"(?i)Dut_Pass\",\"siteNoColumnFlag\":\"(?i)Site_No\",\"password\":\"joulwatt\",\"testItemColumnFlag\":\"(?i),(OS|PIN)\",\"ignoreColumnFlag\":\"(?i)_debug$\"}";
        String dataInfoJson = "{\"reportName\":\"report1212\",\"chipName\":\"JW7878\",\"mode\":0,\"columns\":[{\"id\":5,\"columnName\":\"OS_VCC\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.509,\"realMaxInLimit\":-0.5053,\"realAverage\":-0.5088583457526077,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":-1.1436,\"realMaxOutOfLimit\":-0.0374,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-1365.775799999999,\"totalCountAll\":2684,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":16},{\"id\":6,\"columnName\":\"OS_SW\",\"limitMin\":-0.9,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.5013,\"realMaxInLimit\":-0.4613,\"realAverage\":-0.4789851685393253,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":-0.2895,\"realMaxOutOfLimit\":-0.2895,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-1278.8903999999986,\"totalCountAll\":2670,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":2},{\"id\":7,\"columnName\":\"Vf\",\"limitMin\":-0.7,\"limitMax\":-0.3,\"limitUnit\":\"\",\"realMinInLimit\":-0.628,\"realMaxInLimit\":-0.596,\"realAverage\":-0.6070967016491776,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-1619.7340000000058,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":8,\"columnName\":\"Isw\",\"limitMin\":40.0,\"limitMax\":60.0,\"limitUnit\":\"\",\"realMinInLimit\":49.3434,\"realMaxInLimit\":59.2324,\"realAverage\":61.19922950108454,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":60.8859,\"realMaxOutOfLimit\":500.7632,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":169277.06879999983,\"totalCountAll\":2766,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":98},{\"id\":9,\"columnName\":\"Ivcc\",\"limitMin\":220.0,\"limitMax\":340.0,\"limitUnit\":\"\",\"realMinInLimit\":247.7224,\"realMaxInLimit\":312.5095,\"realAverage\":280.9707630711614,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":357.4558,\"realMaxOutOfLimit\":357.4558,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":750191.9374000009,\"totalCountAll\":2670,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":2},{\"id\":10,\"columnName\":\"garbage\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":1.7976931348623157E308,\"realMaxInLimit\":-1.7976931348623157E308,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":0,\"totalCountInLimit\":0,\"totalCountOutOfLimit\":0},{\"id\":12,\"columnName\":\"Vcc\",\"limitMin\":4.33,\"limitMax\":4.59,\"limitUnit\":\"\",\"realMinInLimit\":4.3753,\"realMaxInLimit\":4.3939,\"realAverage\":4.382166641679163,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":11691.620600000006,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":13,\"columnName\":\"Vcc1\",\"limitMin\":7.0,\"limitMax\":8.5,\"limitUnit\":\"\",\"realMinInLimit\":7.6288,\"realMaxInLimit\":7.8994,\"realAverage\":7.76893350824588,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":20727.514600000006,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":14,\"columnName\":\"Icc_5V\",\"limitMin\":-37.0,\"limitMax\":-27.0,\"limitUnit\":\"\",\"realMinInLimit\":-34.2478,\"realMaxInLimit\":-29.5495,\"realAverage\":-31.951552173913008,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-85246.7411999999,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":15,\"columnName\":\"Icc_1V\",\"limitMin\":-26.0,\"limitMax\":-14.0,\"limitUnit\":\"\",\"realMinInLimit\":-20.88,\"realMaxInLimit\":-17.762,\"realAverage\":-19.42581484257869,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":-51828.07399999994,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":16,\"columnName\":\"Icc3\",\"limitMin\":69.0,\"limitMax\":103.0,\"limitUnit\":\"\",\"realMinInLimit\":75.6541,\"realMaxInLimit\":85.2761,\"realAverage\":79.8413751874063,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":213016.789,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":17,\"columnName\":\"Vcc_on\",\"limitMin\":3.75,\"limitMax\":4.05,\"limitUnit\":\"\",\"realMinInLimit\":3.78,\"realMaxInLimit\":3.89,\"realAverage\":3.832638680659683,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":10225.480000000034,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":18,\"columnName\":\"Vcc_off\",\"limitMin\":3.5,\"limitMax\":3.8,\"limitUnit\":\"\",\"realMinInLimit\":3.62,\"realMaxInLimit\":3.7,\"realAverage\":3.651782771535509,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":4.0,\"realMaxOutOfLimit\":4.0,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":9750.25999999981,\"totalCountAll\":2670,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":2},{\"id\":19,\"columnName\":\"SW_Freq_debug\",\"limitMin\":-1.7976931348623157E308,\"limitMax\":1.7976931348623157E308,\"limitUnit\":\"\",\"realMinInLimit\":0.0,\"realMaxInLimit\":69.8812,\"realAverage\":20.41714137931003,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":false,\"passGroups\":500,\"failGroups\":50,\"totalValue\":54472.93319999916,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":20,\"columnName\":\"BlankingTime\",\"limitMin\":2.1,\"limitMax\":3.61,\"limitUnit\":\"\",\"realMinInLimit\":3.2,\"realMaxInLimit\":3.6,\"realAverage\":3.4430004480955225,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":2.0,\"realMaxOutOfLimit\":8.8888,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":9220.355199999809,\"totalCountAll\":2678,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":10},{\"id\":21,\"columnName\":\"Tbodydiode\",\"limitMin\":-0.001,\"limitMax\":0.001,\"limitUnit\":\"\",\"realMinInLimit\":0.0,\"realMaxInLimit\":0.0,\"realAverage\":0.0,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":0.0,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0},{\"id\":22,\"columnName\":\"Ivcc_GateOn\",\"limitMin\":1.0,\"limitMax\":3.5,\"limitUnit\":\"\",\"realMinInLimit\":1.154,\"realMaxInLimit\":1.289,\"realAverage\":1.20852395209581,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":0.776,\"realMaxOutOfLimit\":0.839,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":3229.176000000004,\"totalCountAll\":2672,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":4},{\"id\":23,\"columnName\":\"Iq\",\"limitMin\":68.0,\"limitMax\":102.0,\"limitUnit\":\"\",\"realMinInLimit\":79.941,\"realMaxInLimit\":90.0453,\"realAverage\":84.53574302848548,\"realAverageInLimit\":0.0,\"realMinOutOfLimit\":1.7976931348623157E308,\"realMaxOutOfLimit\":-1.7976931348623157E308,\"isProcess\":true,\"passGroups\":500,\"failGroups\":50,\"totalValue\":225541.36239999926,\"totalCountAll\":2668,\"totalCountInLimit\":2668,\"totalCountOutOfLimit\":0}],\"files\":[{\"fileName\":\"hello.txt\",\"message\":\"不是csv文件\",\"status\":1001},{\"fileName\":\"JW7707.csv\",\"localFileName\":\"upload\\\\6c5685f9-510c-4fee-9589-5adc62090b60.csv\",\"status\":1111,\"DataStartLine\":7},{\"fileName\":\"rubbish.csv\",\"message\":\"未发现测试数据，错误代码：1002\",\"status\":1005},{\"fileName\":\"JW7707v2.csv\",\"localFileName\":\"upload\\\\2d410de5-b550-4594-854e-0636fb80f6f7.csv\",\"status\":1111,\"DataStartLine\":7}],\"testItemStr\":\"Dut_No,Site_No,Dut_Pass,SW_Bin,Data_Num,OS_VCC,OS_SW,Vf,Isw,Ivcc,garbage,,Vcc,Vcc1,Icc_5V,Icc_1V,Icc3,Vcc_on,Vcc_off,SW_Freq_debug,BlankingTime,Tbodydiode,Ivcc_GateOn,Iq,\",\"limitMinStr\":\"-1,-1,TRUE,-1,-1,-0.9,-0.9,-0.7,40,220,,,4.33,7,-37,-26,69,3.75,3.5,-9999.9999,2.1,-0.001,1,68\",\"limitMaxStr\":\"0,0,FALSE,0,0,-0.3,-0.3,-0.3,60,340,,,4.59,8.5,-27,-14,103,4.05,3.8,9999.9999,3.61,0.001,3.5,102\",\"limitUnitStr\":\",,,,,,,,,,,,,,,,,,,,,,,\",\"dutPassCol\":2,\"indexCol\":0,\"siteCol\":1}";
        String[] fileNames = {"hello.txt", "FT1234.csv", "rubbish.csv"};
        List<InputStream> ins = new ArrayList<InputStream>();
        for (String fileName : fileNames) {
            Resource resource = new ClassPathResource("file/" + fileName);
            InputStream in = resource.getInputStream();
            ins.add(in);
        }
        int size = ins.size();
        DataConfig dataConfig = new Gson().fromJson(configJson, DataConfig.class);
        DataInfo myDataInfo = new Gson().fromJson(dataInfoJson, DataInfo.class);
        rb.setDataConfig(dataConfig);
        DataInfo dataInfo = rb.buildDataInfo(fileNames, (InputStream[]) ins.toArray(new InputStream[size]), "report1212", "JW7878", Constants.PROCESS_MODE_NORMAL);
        List<ColumnInfo> columnInfos = dataInfo.getColumns();

        Method getInitalReportItems = null;
        try {
            getInitalReportItems = rb.getClass().getDeclaredMethod("getInitialReportItems", List.class);
            getInitalReportItems.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //invoke
        List<ReportItem> reportItemList = (List<ReportItem>) getInitalReportItems.invoke(rb, columnInfos);

        //start
        List<FileInfo> fileInfos = dataInfo.getFiles();
        logger.info("deal with fileInfo :{}", fileInfos.get(1).getFileName());
        Method processFile = null;
        try {
            processFile = rb.getClass().getDeclaredMethod("processFile", FileInfo.class, List.class);
            processFile.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        long totalCount = (Long) processFile.invoke(rb, fileInfos.get(1), reportItemList);
        assertEquals(1401, totalCount);
        assertEquals(1334, reportItemList.get(0).getPassCount());
        logger.info("processFile() columnInfos :{}", new Gson().toJson(reportItemList));
        logger.info("processFile() columnInfo 'Isw555' :{}", new Gson().toJson(reportItemList.get(3)));
    }

    @Test
    public void testCalculateReportItemRank() throws InvocationTargetException, IllegalAccessException {
        ReportBuilder rb = new ReportBuilder();
        Method calculateReportItemRank = null;
        try {
            calculateReportItemRank = rb.getClass().getDeclaredMethod("calculateReportItemRank", String.class);
            calculateReportItemRank.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        String[] rateArr = {"96.7", "1.00", "0.01"};
        Integer rank0 = (Integer) calculateReportItemRank.invoke(rb, rateArr[0]);
        Integer rank1 = (Integer) calculateReportItemRank.invoke(rb, rateArr[1]);
        Integer rank2 = (Integer) calculateReportItemRank.invoke(rb, rateArr[2]);

        assertEquals(Constants.RANK_LOW, rank0);
        assertEquals(Constants.RANK_LOW, rank1);
        assertEquals(Constants.RANK_HIGH, rank2);
        logger.info("Rate: {}, Result: Rank: {}", rateArr[0], rank0);
        logger.info("Rate: {}, Result: {}", rateArr[1], rank1);
        logger.info("Rate: {}, Result: {}", rateArr[2], rank2);
    }

    @Test
    public void testCalculateReportRank() throws InvocationTargetException, IllegalAccessException {
        ReportBuilder rb = new ReportBuilder();
        Method calculateReportRank = null;
        try {
            calculateReportRank = rb.getClass().getDeclaredMethod("calculateReportRank", String.class);
            calculateReportRank.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        String[] rateArr = {"96.7", "93.00", "0.01"};
        Integer[] rank = new Integer[3];
        for (int i = 0; i < rateArr.length; i++) {
            rank[i] = (Integer) calculateReportRank.invoke(rb, rateArr[i]);
            logger.info("ReportRate: {} , Result:rank: {}", rateArr[i], rank[i]);
        }
        assertEquals(Constants.RANK_HIGH, rank[0]);
        assertEquals(Constants.RANK_MEDIUM, rank[1]);
        assertEquals(Constants.RANK_LOW, rank[2]);
    }

    @Test
    public void testcalculateRestBars() throws InvocationTargetException, IllegalAccessException {
        ReportBuilder rb = new ReportBuilder();
        List<ReportItem> reportItems = new ArrayList<ReportItem>();
        ReportItem reportItem = new ReportItem("Isw");
        Chart failChart = new Chart();
        Bar[] barArrs = {new Bar(50, 5), new Bar(60, 6), new Bar(70, 0)};
        List<Bar> bars = new ArrayList<Bar>(Arrays.asList(barArrs));
        failChart.setBars(bars);
        reportItem.setFailChart(failChart);
        reportItems.add(reportItem);
        Method calculateRestBars = null;
        try {
            calculateRestBars = rb.getClass().getDeclaredMethod("calculateRestBars", List.class);
            calculateRestBars.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        calculateRestBars.invoke(rb, reportItems);
        logger.info("reportItems:{}", new Gson().toJson(reportItems));

    }

    @Test
    public void testBuildReport() throws IOException {
        ReportBuilder rb = new ReportBuilder();
        String configJson = "{\"id\":1,\"dutNoColumnFlag\":\"(?i)Dut_NO\",\"limitMinLineFlag\":\"(?i)^(-1|Min),\",\"limitMaxLineFlag\":\"(?i)^(0|Max),\",\"dutPassTrueString\":\"(?i)TRUE\",\"dutPassFalseString\":\"(?i)FALSE\",\"dutPassColumnFlag\":\"(?i)Dut_Pass\",\"siteNoColumnFlag\":\"(?i)Site_No\",\"password\":\"joulwatt\",\"testItemColumnFlag\":\"(?i),(OS|PIN)\",\"ignoreColumnFlag\":\"(?i)_debug$\"}";
        String[] fileNames = {"hello.txt", "FT1234.csv", "rubbish.csv"};
        List<InputStream> ins = new ArrayList<InputStream>();
        for (String fileName : fileNames) {
            Resource resource = new ClassPathResource("file/" + fileName);
            InputStream in = resource.getInputStream();
            ins.add(in);
        }
        int size = ins.size();
        DataConfig dataConfig = new Gson().fromJson(configJson, DataConfig.class);
        rb.setDataConfig(dataConfig);
        long start, end;
        //build DataInfo
        start = System.currentTimeMillis();
        DataInfo dataInfo = rb.buildDataInfo(fileNames, (InputStream[]) ins.toArray(new InputStream[size]), "report1212", "JW7878", Constants.PROCESS_MODE_NORMAL);
        end = System.currentTimeMillis();
        logger.info("testBuildDataInfo() use time: {} ms", end - start);

        //build report
        start = System.currentTimeMillis();
        Report report = rb.buildReport(dataInfo);
        end = System.currentTimeMillis();
        String json = new Gson().toJson(report);
        String debugFile = "upload/debug.txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(debugFile)));
        bw.write(json);
        bw.close();
        //ReportItem 'Isw555'
        ReportItem reportItem=report.getReportItems().get(1);
        //列名
        assertEquals("Isw555",reportItem.getColumnName());
        //失效数量
        assertEquals(49,reportItem.getFailCount());
        //失效比率
        assertEquals("3.6732",reportItem.getFailRate());
        //等级：危险
        assertEquals(Constants.RANK_LOW,reportItem.getRank());
        //Cp Cpk
        Chart passChart=reportItem.getPassChart();
        Chart failChart= reportItem.getFailChart();
        assertEquals(3.25,passChart.getCpu(),0.005);
        assertEquals(3.83,passChart.getCpl(),0.005);
        assertEquals(3.25,passChart.getCpk(),0.005);
        assertEquals(3.54,passChart.getCp(),0.005);
        //average
        double accuracy=0.0001;
        assertEquals(343.5754,failChart.getRealAverage(),accuracy);
        assertEquals(50.8271,passChart.getRealAverage(),accuracy);
        //标准差stdev
        assertEquals(0.94199658,passChart.getStdev(),accuracy);
        assertEquals(195.8187466,failChart.getStdev(),accuracy);

        assertEquals(1334, report.getReportItems().get(0).getPassCount());
        assertEquals(0, report.getReportItems().get(0).getFailCount());
        logger.info("testBuildReport() use time: {} ms", end - start);
        logger.info("testBuildReport() report: {}", new Gson().toJson(report));
        logger.info("testBuildReport() ReportItem 'Isw555': {}", new Gson().toJson(report.getReportItems().get(1)));
    }
}
