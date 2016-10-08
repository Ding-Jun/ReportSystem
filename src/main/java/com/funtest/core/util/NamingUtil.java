package com.funtest.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.funtest.core.bean.constant.Constants;

/**
 * 类NamingUtil.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2015年12月9日 下午9:43:06
 */
public class NamingUtil {

    protected static Logger logger = LoggerFactory.getLogger(NamingUtil.class);

    /**
     * 将下划线方式命名的字符串转换为驼峰式 例如：HELLO_WORLD->helloWorld
     * 
     * @param name
     *            转换前的下划线方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String underlineToCamel(String name) {
        StringBuilder result = new StringBuilder();

        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }

        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }

            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                
                if(camel.length() > 1){
                    result.append(camel.substring(1).toLowerCase());
                }
            }
        }
        return result.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
     * 例如：HELLO_WORLD->helloWorld
     * 
     * @param param 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }

        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(Constants.UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {

                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        String camel = underlineToCamel("col_phoneNO");
        String underline = camelToUnderline("asBsdfsLde");

        System.out.println(camel);
        System.out.println(underline);

    }

}
