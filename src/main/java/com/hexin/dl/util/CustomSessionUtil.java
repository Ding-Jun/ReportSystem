package com.hexin.dl.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.BooleanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.funtest.analysis.bean.Admin;
import com.funtest.core.bean.constant.Constants;
import com.hexin.core.exceptions.BmsException;
import com.hexin.core.util.LogUtil;

/**
 * 
 * 类CustomSessionUtil.java的实现描述：TODO 类实现描述 
 * @author Administrator 2015年12月10日 下午2:50:36
 */
public class CustomSessionUtil {

    protected static Logger logger = LoggerFactory.getLogger(CustomSessionUtil.class);
    private static Map<Integer, Boolean> forcedLogoutMap = new HashMap<Integer, Boolean>();

    private static Properties properties = PropertyUtil.loadProperties("common.properties");

    private static final String RMI_ACCOUNT_SERVICE_SERVER_ADDR = properties
            .getProperty("rmi.account.service.server.address");
    private static final int RMI_ACCOUNT_SERVICE_SERVER_port = Integer.parseInt(properties
            .getProperty("rmi.account.service.server.port"));

    /**
     * 获取登陆用户
     * 
     * @return Admin
     */
    public static Admin getLoginAdmin() {
        return (Admin) SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_USER_KEY);
    }

    /**
     * 获取登陆用户角色
     * 
     * @return Admin
     */
    public static Integer getLoginAdminRole() {
        Admin curAdmin = (Admin) SecurityUtils.getSubject().getSession()
                .getAttribute(Constants.SESSION_USER_KEY);

        if (curAdmin != null) {
            //return curAdmin.getColRole();
        }

        return null;
    }

    /**
     * 获取登陆用户id
     * 
     * @return Admin
     */
    public static Integer getLoginAdminId() {
        Admin curAdmin = (Admin) SecurityUtils.getSubject().getSession()
                .getAttribute(Constants.SESSION_USER_KEY);

        if (curAdmin != null) {
            return curAdmin.getId();
        }

        return null;
    }

    public static String getLoginAdminName() {
        Admin curAdmin = (Admin) SecurityUtils.getSubject().getSession()
                .getAttribute(Constants.SESSION_USER_KEY);

        if (curAdmin != null) {
            return curAdmin.getName();
        }

        return null;
    }

    public static Session getSession() {
        Subject currentAdmin = SecurityUtils.getSubject();
        Session session = currentAdmin.getSession();

        return session;
    }

    public static String getSessionId() {
        Session session = getSession();

        if (null == session) {
            return null;
        }

        return getSession().getId().toString();
    }

    public static void markAdminForcedToLogout(Integer userId) {

        if (!BooleanUtils.isTrue(forcedLogoutMap.get(userId))) {
            forcedLogoutMap.put(userId, true);

            logger.info("############## marked 【{}】  as forced to logout  ------ {} #################",
                    userId, CustomDateUtil.getCurrentTime());
        }

    }

    public static boolean isForcedToLogout() {
        Integer userId = getLoginAdminId();

        if (BooleanUtils.isTrue(forcedLogoutMap.get(userId))) {
            return true;
        }

        return false;
    }

    public static void cancelAdminForcedToLogoutMark() {
        Integer userId = getLoginAdminId();

        if (BooleanUtils.isTrue(forcedLogoutMap.get(userId))) {
            forcedLogoutMap.remove(userId);

            LogUtil.info(logger, "############## remove kick out user 【{}-{}】 ------ {} #################",
                    getSessionId(), getLoginAdminName(), CustomDateUtil.getCurrentTime());
        }
    }

    public static void markRemoteAdminForcedToLogout(Integer colAdminId) throws BmsException {
        /*try {
            String accountServicePath = "rmi://" + RMI_ACCOUNT_SERVICE_SERVER_ADDR + ":"
                    + RMI_ACCOUNT_SERVICE_SERVER_port + "/accountService";

            LogUtil.info(logger, "############## lookup remote account service path : {} ", accountServicePath);

            RMIAccountService service = (RMIAccountService) Naming.lookup(accountServicePath);

            LogUtil.info(logger,
                    "############## try to remote mark 【{}】  as forced to logout  ------ {} #################",
                    colAdminId, CustomDateUtil.getCurrentTime());

            service.markAdminForcedToLogout(colAdminId);

        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            logger.error(e.getMessage(), e);
//            throw new BmsException(e);
        }*/
    }

}
