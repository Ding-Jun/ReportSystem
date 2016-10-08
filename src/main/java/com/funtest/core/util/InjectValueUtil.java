package com.funtest.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.funtest.core.exceptions.BmsException;

/**
 * 
 * 类InjectValueUtil.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2015年12月10日 上午11:02:14
 */
public class InjectValueUtil {

    private static Logger logger = LoggerFactory
            .getLogger(InjectValueUtil.class);

    public static final String DEFAULT_BOLOB_CHAR_SET = "utf8";

    /**
     * 注入
     * 
     * @param obj 
     * @param filedName 
     * @param fieldValue 
     * @param <T> 
     * @throws BmsException 
     */
    public static <T> void setFieldValue(T obj, String filedName,
            Object fieldValue) throws BmsException {
        if (obj == null) {
            return;

        }

        Class<? extends Object> clz = obj.getClass();
        Field field = null;
        try {
            field = clz.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, fieldValue);
        } catch (java.lang.NoSuchFieldException e) {
            LogUtil.debug(logger, "No such field : {}", filedName);
            return;
        } catch (IllegalArgumentException e) {
            throw new BmsException(e);
        } catch (IllegalAccessException e) {
            throw new BmsException(e);
        }
    }
    
    /**
     * 
     * @param obj 
     * @param filedName 
     * @param fieldValue 
     * @param <T> 
     * @throws BmsException 
     */
    public static <T> void setFieldValue(T obj, String filedName,
            Blob fieldValue) throws BmsException {
        try {
            if (obj == null || fieldValue == null || fieldValue.length() <= 0) {
                return;
            }

            Class<? extends Object> clz = obj.getClass();
            Field field = clz.getDeclaredField(filedName);
            field.setAccessible(true);

            byte[] returnBytes = fieldValue.getBytes(1,
                    (int) fieldValue.length());
            String value = new String(returnBytes, DEFAULT_BOLOB_CHAR_SET);

            field.set(obj, value);
        } catch (java.lang.NoSuchFieldException e) {
            LogUtil.debug(logger, "No such field : {}", filedName);
            return;
        } catch (IllegalArgumentException e) {
            throw new BmsException(e);
        } catch (IllegalAccessException e) {
            throw new BmsException(e);
        } catch (SQLException e) {
            throw new BmsException(e);
        } catch (UnsupportedEncodingException e) {
            throw new BmsException(e);
        }
    }

    /**
     * 转换二进制成字符串
     * 
     * @param is 
     * @return string
     * @throws BmsException 统一异常
     */
    public static String convertStreamToString(InputStream is)
            throws BmsException {
        String result = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                // String cnt = new String(line.getBytes(), "utf-8");
                sb.append(line + "/n");
            }

            is.close();
        } catch (IOException e) {
            throw new BmsException(e);
        }

        result = sb.toString();

        return result;
    }

    /**
     * 测试
     * 
     * @param args 
     */
    public static void main(String[] args) {
        try {
            String a = "d2f23鑷不宸�imgsrc=";
            String b;
            b = new String(a.getBytes("utf8"), "gbk");
            System.out.println(b);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
