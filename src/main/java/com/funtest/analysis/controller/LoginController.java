package com.funtest.analysis.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funtest.analysis.bean.Admin;
import com.funtest.analysis.bean.User;
import com.funtest.core.bean.ReturnMsg;
import com.funtest.core.bean.constant.Constants;
import com.hexin.dl.util.CustomSessionUtil;

/**
 * 
 * 类LoginController.java的实现描述：TODO 类实现描述 
 * @author Administrator 2015年12月10日 下午3:17:18
 */
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 用户登陆
     * @param colUserName 
     * @param colUserPwd 
     * @return Object
     */
    @RequestMapping(value = "login")
    @ResponseBody
    public Object login(@RequestParam(value = "name", required = true) String colUserName,
            @RequestParam(value = "password", required = true) String colUserPwd) {

        ReturnMsg rm = new ReturnMsg();
        Subject curSubject = SecurityUtils.getSubject();

        try {
            UsernamePasswordToken token = new UsernamePasswordToken(colUserName, colUserPwd);
            curSubject.login(token);

            //设置数据权限拦截中的登录用户数据
            //            RestrictionParser.setLoginUser(CustomSessionUtil.getLoginUser());
           // CustomSessionUtil.cancelUserForcedToLogoutMark();

            rm.setCode(Constants.RETURN_MSG_SUCCESS);
            curSubject.hasRole("dummy");
            logger.info("User \"" +CustomSessionUtil.getLoginAdminName()+"\" is log in.");
        } catch (AuthenticationException e) {
        	logger.error(e.getMessage(), e);
            rm.setCode(Constants.RETURN_MSG_FAILURE);
            rm.setMessage(Constants.LOGIN_FAILED);
        }      
        return rm;
    }

    /**
     * 系统登出
     * 
     * @return Object
     */
    @RequestMapping(value = "/logout")
    @ResponseBody
    public Object loginOut() {

        ReturnMsg rm = new ReturnMsg();

        try {
        	String userName=CustomSessionUtil.getLoginAdminName();
            SecurityUtils.getSubject().logout();

            //设置数据权限拦截中的登录用户为空
            //RestrictionParser.setLoginUser(null);

            rm.setCode(Constants.RETURN_MSG_SUCCESS);
            logger.info("User \"" +userName+"\" is log out.");
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            rm.setCode(Constants.RETURN_MSG_FAILURE);
            rm.setMessage(Constants.SYS_ERROR);
        }

        return rm;
    }

    /**
     * 检查是否已登录
     * 
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/checkLogin")
    @ResponseBody
    public Object checkLogin() {

        ReturnMsg rm = new ReturnMsg();

        try {
        	Admin curUser = (Admin) SecurityUtils.getSubject().getSession()
                    .getAttribute(Constants.SESSION_USER_KEY);
        	

            Integer userId = curUser.getId();

            if (userId == null) {
                rm.setCode(Constants.RETURN_MSG_FAILURE);
            } else {
                rm.setCode(Constants.RETURN_MSG_SUCCESS);
                rm.setData(userId);
            }

        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            rm.setCode(Constants.RETURN_MSG_FAILURE);
            rm.setMessage(Constants.SYS_ERROR);
        } finally {
            return rm;
        }
    }

}
