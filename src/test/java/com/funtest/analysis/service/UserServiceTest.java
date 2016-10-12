package com.funtest.analysis.service;

import com.funtest.analysis.bean.User;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author admin
 * @create 2016-10-12 10:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest {
    final static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
    @Autowired
    UserService service;
    @Test
    public void testQueryUser(){

        User user = service.queryUserByName("root");
        logger.info("user:{}",new Gson().toJson(user));
    }
}
