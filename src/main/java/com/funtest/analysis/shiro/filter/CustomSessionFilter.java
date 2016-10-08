package com.funtest.analysis.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.funtest.core.bean.constant.Constants;
import com.funtest.core.util.LogUtil;
import com.hexin.dl.util.CustomSessionUtil;

/**
 * 
 * 类CustomSessionFilter.java的实现描述：TODO 类实现描述 
 * @author Administrator 2015年12月10日 下午3:06:32
 */
public class CustomSessionFilter extends UserFilter {

    private static Logger logger = LoggerFactory.getLogger(CustomSessionFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest arg0, ServletResponse arg1, Object arg2) {

        //判断是否被强制退出（用于权限修改即时生效）
        if (CustomSessionUtil.isForcedToLogout()) {
            return false;
        }

        //RestrictionParser.setLoginUser(CustomSessionUtil.getLoginUser());
        
        return super.isAccessAllowed(arg0, arg1, arg2);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        HttpServletRequest httpRequest = WebUtils.toHttp(request);

        //        if (httpRequest.getSession(false) == null
        //                || CustomSessionUtil.getLoginUser() == null) {
        if (httpRequest.getHeader("x-requested-with") != null
                && httpRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) // 如果是ajax请求响应头会有，x-requested-with；
        { // ajax
            httpResponse.setHeader("sessionstatus", "timeout"); // 在响应头设置session状态
            httpResponse.setStatus(Constants.RESPONSE_STATUS_NOT_FOUND);

            LogUtil.info(logger, "Loggin session is timeout, please login again");

            //                return false;
        } else { // 非ajax访问超时处理
            LogUtil.info(logger, "Loggin session is timeout, please login again");

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/html/login.html");
        }
        //        }

        return false;
    }

}
