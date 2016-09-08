package com.funtest.analysis.realm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.funtest.analysis.bean.Admin;
import com.funtest.analysis.bean.Permission;
import com.funtest.analysis.dao.AdminDao;
import com.funtest.analysis.service.AdminService;
import com.funtest.core.bean.constant.Constants;
import com.hexin.core.exceptions.BmsException;

public class MyRealm extends AuthorizingRealm {

	protected static Logger logger = LoggerFactory.getLogger(MyRealm.class);

	@Autowired
	private AdminService service;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName= (String) token.getPrincipal();
		Admin admin=service.queryAdminByName(userName);
		if(admin==null){
		      //木有找到用户
		      throw new UnknownAccountException("没有找到该账号");
		    }
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(admin.getName(), admin.getPassword(),getName());		
		return info;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();
		Admin admin =   service.queryAdminByName(userName);
		

	    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    authorizationInfo.addStringPermissions(service.queryAdminPermissionsForShiro(admin.getId()));
	    this.setSessionUser(admin);
	    return authorizationInfo;
	}
	
	@Override
	public String getName() {
		return getClass().getName();
	}


	  /**
     * 设置session用户
     * 
     * @param user
     *            登陆用户
     * @throws BmsException 
     */
    private void setSessionUser(Admin admin)  {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
 



        session.setAttribute(Constants.SESSION_USER_KEY, admin);
        session.setAttribute(Constants.SESSION_USER_NAME_KEY, admin.getName());

        //缓存session
//        CustomSessionHelper2.addUserToSessionMap();
        
        //          session.setTimeout(1000 * 60);

        logger.info("Session默认超时时间为[{}]分钟", session.getTimeout() / Constants.SESSION_DEFAULT_TIMEOUT);

    }
}
