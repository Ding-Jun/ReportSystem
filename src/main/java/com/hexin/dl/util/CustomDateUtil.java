package com.hexin.dl.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类CustomDateUtil.java的实现描述：TODO 类实现描述 
 * @author HeNeng 2016年2月28日 下午3:48:20
 */
public class CustomDateUtil {

    protected static Logger logger = LoggerFactory.getLogger(CustomDateUtil.class);
    
    public static String getFirstDayOfLastWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        
        return sdf.format(calendar.getTime());
    }

    public static String getLastDayOfLastWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        
        return sdf.format(calendar.getTime());
    }
    
    public static String getLastDayOfLastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return sdf.format(calendar.getTime());
    }

    public static String getFirstDayOfLastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return sdf.format(calendar.getTime());
    }

    public static String getLastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -1);

        return sdf.format(calendar.getTime());
    }

    public static String getYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, -1);

        return sdf.format(calendar.getTime());
    }
    
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        
        return sdf.format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(getFirstDayOfLastMonth());
        System.out.println(getLastMonth());
        System.out.println(getYesterday());
        System.out.println(getLastDayOfLastMonth());
        System.out.println(getFirstDayOfLastWeek());
        System.out.println(getLastDayOfLastWeek());
        System.out.println(getCurrentTime());
    }

}
