package com.hexin.dl.util;

import java.security.MessageDigest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.funtest.core.bean.constant.Constants;

import sun.misc.BASE64Encoder;
/**
 * MD5加密
 * 类DigestUtil.java的实现描述：TODO 类实现描述 
 * @author ZhengTianyu 2016年1月22日 下午5:23:05
 */
public class DigestUtil {
    private static final Logger logger = LogManager.getLogger(DigestUtil.class);
    private static final String ALGORITHM = "AES";
    private static final String ENCODING = "utf-8";

    /**
     * @param encryptString
     * @param encryptionKey
     * @return
     * @throws O2InternalException
     */
//    public static String aesDecrypt(String encryptString, String encryptionKey)
//            throws O2InternalException {
//        logger.info("Start To Decrypt The Encrypted Token!");
//        logger.info("EncryedToken:" + encryptString + " , userToken:"
//                + encryptionKey);
//        String decryptedString = null;
//        try {
//            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, encryptionKey);
//
//            byte[] plainBytes = cipher.doFinal(Base64
//                    .decodeBase64(encryptString));
//
//            decryptedString = new String(plainBytes, ENCODING);
//            logger.info("Decrypted String:" + decryptedString);
//        } catch (Exception e) {
//            logger.error(e.getStackTrace());
//        }
//        return decryptedString;
//
//    }

    /**
     * @param source
     * @param encryptionKey
     * @return
     * @throws O2InternalException
     */
//    public static String aesEcrypt(String source, String encryptionKey)
//            throws O2InternalException {
//        logger.info("Start To Encrypt The Encrypted Token!");
//        String returnString = null;
//        try {
//
//            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, encryptionKey);
//
//            byte[] encryptedBytes = cipher.doFinal(source.getBytes(ENCODING));
//
//            returnString = Base64.encodeBase64String(encryptedBytes);
//        } catch (Exception e) {
//            logger.error(e.getStackTrace());
//        }
//        return returnString;
//    }

    /**
     * @param cipherMode
     * @param encryptionKey
     * @return
     * @throws Exception
     */
//    private static Cipher getCipher(int cipherMode, String encryptionKey)
//            throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
        // KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        // SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // random.setSeed(encryptionKey.getBytes(ENCODING));
        // kgen.init(128, random);
        // SecretKey secretKey = kgen.generateKey();
        // byte[] enCodeFormat = secretKey.getEncoded();

        // SecretKeySpec keySpecification = new SecretKeySpec(
        // encryptionKey.getBytes(ENCODING), ALGORITHM);
//
//        SecretKeySpec keySpecification = new SecretKeySpec(
//                encryptionKey.getBytes(ENCODING), ALGORITHM);
//        IvParameterSpec iv = new IvParameterSpec(
//                encryptionKey.getBytes(ENCODING));
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
//        cipher.init(cipherMode, keySpecification, iv);
//
//        return cipher;
//    }

    /**
     * @param bytes
     * @return
     */
    private static String hexString(byte[] bytes) {
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            int val = ((int) bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * @param source
     * @return
     * @throws Exception
     */
    public static String md5HexDigest(String source) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(Constants.MD5);

        return hexString(md5.digest(source.getBytes(ENCODING)));
    }

    /**
     * @param source
     * @return
     * @throws Exception
     */
    /**
     * @param source
     * @return
     * @throws Exception
     */
    public static String md5Base64Digest(String source) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(Constants.MD5);
        BASE64Encoder base64en = new BASE64Encoder();
        return base64en.encode(md5.digest(source.getBytes(ENCODING)));
    }

}
