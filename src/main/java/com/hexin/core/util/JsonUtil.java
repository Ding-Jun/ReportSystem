package com.hexin.core.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 
 * 类JsonUtil.java的实现描述：TODO 类实现描述
 * 
 * @author fanjiangqi@myhexin.com 2016年1月18日 下午1:53:12
 */
public class JsonUtil {

    public static String getJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T getObjectFromJSONString(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> List<T> getArrayFromJSONString(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    public static <T> List<Map<String, Object>> getListsMapFromJsonString(String json) {
        return JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
        });
    }
}
