/*
 * Copyright 2011-2015 10jqka.com.cn All right reserved. This software is the
 * confidential and proprietary information of 10jqka.com.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with 10jqka.com.cn.
 */
package com.hexin.dl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * 类DateUtil.java的实现描述：TODO 类实现描述 
 * @author ZhengTianyu 2016年2月24日 下午5:11:46
 */
public class DateUtil {

    public static String getCurrentDateTime() {
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        return time;
    }
    
    public static String getTimeFromIssueTime(String issueTime) throws ParseException {
        String result = "";

        if (StringUtils.isEmpty(issueTime)) {
            return result;
        }

        result = formatDateStr(issueTime, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");

        return result;
    }
    
    /**
     * 按照给定格式 格式化时间字符串
     * 
     * @param title
     * @param string
     * @return
     * @throws ParseException
     */
    public static String formatDateStr(String dateStr, String fromFormate, String toFormat) throws ParseException {
        Date date;
        String result = "";

        if (StringUtils.isEmpty(dateStr)) {
            return result;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(fromFormate);
        date = sdf.parse(dateStr);

        sdf = new SimpleDateFormat(toFormat);
        result = sdf.format(date);

        return result;
    }
}
