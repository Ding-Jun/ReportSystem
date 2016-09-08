package com.hexin.dl.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 类GsonUtil.java的实现描述：json util to change the object to json or get the object
 * from the json
 * 
 * @author BinWu 2015年12月28日 下午9:05:41
 */
public class GsonUtil {
    private static Gson gson = new Gson();

    public static String toJson(Object obj, Type type) {
        return gson.toJson(obj, type);
    }

    public static Object fromJson(String str, Type type) {
        return gson.fromJson(str, type);
    }

    public static Object fromJson(String str, TypeToken typeToken) {
        return gson.fromJson(str, typeToken.getType());
    }
}
