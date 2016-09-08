package com.funtest.core.bean;

import com.funtest.core.bean.constant.Constants;

/**
 * 返回前台通用信息格式 默认:code=1，表示调用接口正确，无异常
 * 
 * @author lzs 2014-5-13 下午02:10:31
 */
public class ReturnMsg {

    private Integer code = Constants.RETURN_MSG_INIT;
    private String message;
    private String callback; // 用于jsonp
    private Object data;

    /**
     * callback constructor
     * @param callback 前端指定的回调方法名
     */
    public ReturnMsg(String callback) {
        super();
        this.callback = callback;
    }

    /**
     * default constructor
     */
    public ReturnMsg() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @return Integer
     */
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
