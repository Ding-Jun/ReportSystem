package com.hexin.dl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 
 * 类CustomObjectConvertUtil.java的实现描述：TODO 类实现描述 
 * @author HeNeng 2016年1月12日 下午7:54:26
 */
public class CustomObjectUtil {

    protected static Logger logger = LoggerFactory.getLogger(CustomObjectUtil.class);

    /**
     * 检查字段值是否有效
     * @param value 
     * @return boolean
     */
    public static boolean assertValid(Integer value) {
        boolean result = false;

        if (value != null && value != -1) {
            result = true;
        }

        return result;
    }

    public static String trim(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        
        String result = value.trim();
        
        if (StringUtils.isEmpty(result)) {
            return null;
        }

        return result;
    }

    public static void main(String[] args) {
        String value = null;
        
        value = trim(value);
        
        System.err.println((value == null));
        System.err.println(value);
    }

}
