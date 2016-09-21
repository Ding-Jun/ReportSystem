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
import org.springframework.web.multipart.MultipartFile;

import com.funtest.core.bean.ReturnMsg;
import com.funtest.core.bean.constant.Constants;
import com.hexin.dl.util.CustomSessionUtil;

@Controller
@RequestMapping(value="report")
public class ReportController {
	 private  Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	    @RequestMapping(value = "creatReport")
	    @ResponseBody
	    public Object login(@RequestParam("files") MultipartFile[] files,
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
}
