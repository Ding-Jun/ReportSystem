package com.funtest.core.util;

import org.slf4j.Logger;

/**
 * 类LogUtil.java的实现描述：TODO 类实现描述
 * 
 * @author Administrator 2015年12月9日 下午9:43:26
 */
public class LogUtil {

    public static void debug(Logger logger, String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    public static void info(Logger logger, String msg) {
        if (logger.isDebugEnabled() || logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public static void debug(Logger logger, Exception e) {
        if (logger.isDebugEnabled()) {
            logger.debug(e.getMessage(), e);
        }
    }

    public static void info(Logger logger, String msg, Object... objects) {
        if (logger.isDebugEnabled() || logger.isInfoEnabled()) {
            logger.info(msg, objects);
        }
    }

    public static void debug(Logger logger, String msg, Object... objects) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg, objects);
        }
    }

    public static void error(Logger logger, String msg, Object... objects) {
        logger.error(msg, objects);
    }

    public static void error(Logger logger, Exception e) {
        logger.error(e.getMessage(), e);
    }

}
