package com.hexin.core.util;

import java.io.UnsupportedEncodingException;

import org.apache.shiro.crypto.hash.Sha384Hash;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 加密 类EncryptUtil.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2015年12月10日 上午11:19:57
 */
public class EncryptUtil {

//    private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * 加密
     * 
     * @param data 源字符串
     * @return string
     */
    public static String encrypt(String data) {
        if (data == null || data.trim().length() <= 0) {
            return null;
        }

        String sha384Hex = new Sha384Hash(data).toBase64();

        return sha384Hex;
    }

    /**
     * Base64加密
     * @param str
     * @return
     */
    @SuppressWarnings("restriction")
    public static String getBase64(String str) {  
        byte[] b = null;  
        String s = null;  
        try {  
            b = str.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        if (b != null) {  
            s = new BASE64Encoder().encode(b);  
        }  
        return s;  
    }  
  
    /**
     * Base64解密
     * @param s
     * @return
     */
    @SuppressWarnings("restriction")
    public static String getFromBase64(String s) {  
        byte[] b = null;  
        String result = null;  
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);  
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  

    /**
     * 测试
     * 
     * @param args 
     */
    public static void main(String[] args) {
        System.out.println(encrypt("c4ca4238a0b923820dcc509a6f75849b"));
        System.out.println(encrypt("d0970714757783e6cf17b26fb8e2298f"));
    }

}
