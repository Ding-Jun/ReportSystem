package com.funtest.core.exceptions;

/**
 * 统一异常
 * 类BmsException.java的实现描述：TODO 类实现描述 
 * @author Administrator 2015年12月10日 下午12:20:37
 */
public class BmsException extends Exception {
    
    private static final long serialVersionUID = -4620385902959584679L;

    /**
     * default constructor
     */
    public BmsException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param message 
     * @param cause 
     */
    public BmsException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param message 
     */
    public BmsException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param cause 
     */
    public BmsException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    
    
}
