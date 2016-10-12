package com.funtest.analysis.realm;

import com.funtest.analysis.bean.User;
import com.funtest.analysis.service.UserService;
import com.funtest.core.bean.constant.Constants;
import com.funtest.core.exceptions.BmsException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRealm extends AuthorizingRealm {

	protected static Logger logger = LoggerFactory.getLogger(MyRealm.class);

	@Autowired
	private UserService service;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName= (String) token.getPrincipal();
		User user=service.queryUserByName(userName);
		if(user==null){
		      //木有找到用户
		      throw new UnknownAccountException("没有找到该账号");
		    }
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getName(), user.getPassword(),getName());
		return info;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) principals.getPrimaryPrincipal();
		User user =   service.queryUserByName(userName);
		

	    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    //authorizationInfo.addStringPermissions(service.queryAdminPermissionsForShiro(admin.getId()));
	    this.setSessionUser(user);
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
    private void setSessionUser(User user)  {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
 



        session.setAttribute(Constants.SESSION_USER_KEY, user);
        session.setAttribute(Constants.SESSION_USER_NAME_KEY, user.getName());

        //缓存session
//        CustomSessionHelper2.addUserToSessionMap();
        
        //          session.setTimeout(1000 * 60);

        logger.info("Session默认超时时间为[{}]分钟", session.getTimeout() / Constants.SESSION_DEFAULT_TIMEOUT);

    }
}
