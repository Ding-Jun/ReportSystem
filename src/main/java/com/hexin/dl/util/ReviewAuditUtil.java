/*
 * Copyright 2011-2015 10jqka.com.cn All right reserved. This software is the
 * confidential and proprietary information of 10jqka.com.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with 10jqka.com.cn.
 */
package com.hexin.dl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类ReviewAuditUtil.java的实现描述：TODO 类实现描述 
 * @author ZhengTianyu 2016年4月6日 下午3:06:10
 */
public class ReviewAuditUtil {

    protected static Logger logger = LoggerFactory.getLogger(ReviewAuditUtil.class);
    
    private static final String HOST_ADDRESS = "http://10.25.3.25:8080/o2mq/comment/sync";
//    private static final String HOST_ADDRESS = "http://120.27.163.114/o2mq/comment/sync";
//    private static final String HOST_ADDRESS = "http://192.168.27.117:8080/o2mq/comment/sync";
    
    public static String post(String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(HOST_ADDRESS);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            logger.info("push audit reviews:" + param);;
        } catch (Exception e) {            
            logger.error(e.getMessage());
            throw new RuntimeException();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
