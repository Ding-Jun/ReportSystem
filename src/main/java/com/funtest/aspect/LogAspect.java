package com.funtest.aspect;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.funtest.core.util.LogUtil;

/**
 * 
 * @ClassName: LogAspect
 * @Description: 日志记录AOP实现
 * @author shaojian.yu
 * 
 */
@Aspect
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private String requestPath = null; // 请求地址
    private String userName = null; // 用户名
    private Map<?, ?> inputParamMap = null; // 传入参数
    private Map<String, Object> outputParamMap = null; // 存放输出结果
    private long startTimeMillis = 0; // 开始时间
    private long endTimeMillis = 0; // 结束时间

    /**
     * 
     * @Title：doBeforeInServiceLayer
     * @Description: 方法调用前触发 记录开始时间
     * @author shaojian.yu
     * @date 2014年11月2日 下午4:45:53
     * @param joinPoint 
     */
    @Before("execution(* com.funtest.analysis.controller..*.*(..))")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
        startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
    }

    /**
     * 
     * @Title：doAfterInServiceLayer
     * @Description: 方法调用后触发 记录结束时间
     * @author shaojian.yu
     * @date 2014年11月2日 下午4:46:21
     * @param joinPoint 
     */
    @After("execution(* com.funtest.analysis.controller..*.*(..))")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
        endTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间
        this.printOptLog();
    }

    /**
     * 
     * @Title：doAround
     * @Description: 环绕触发
     * @author shaojian.yu
     * @date 2014年11月3日 下午1:58:45
     * @param pjp 
     * @return Object
     * @throws Throwable 
     */
    @Around("execution(* com.funtest.analysis.controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        /**
         * 1.获取request信息 2.获取session 3.从session中取出登录用户信息
         */
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        //AdminUser user = CustomSessionUtil.getLoginUser();

        //if (user != null) {
            //userName = user.getColUserName();
        //} else {
            //userName = Constants.ANONYMOUS_USER;
        //}

        // 获取输入参数
        inputParamMap = request.getParameterMap();
        // 获取请求地址
        requestPath = request.getRequestURI();

        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
        outputParamMap = new HashMap<String, Object>();
        Object result = pjp.proceed(); // result的值就是被拦截方法的返回值
        outputParamMap.put("result", result);

        return result;
    }

    /**
     * 
     * @Title：printOptLog
     * @Description: 输出日志
     * @author shaojian.yu
     * @date 2014年11月2日 下午4:47:09
     */
    private void printOptLog() {
        String optTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);

        //        LogUtil.info(
        //                logger,
        //                "\n user: {};  url: {};  op_time: {};  pro_time: {} ms ;\n param: {};\n result: {}",
        //                userName, requestPath, optTime,
        //                (endTimeMillis - startTimeMillis), JSON.toJSONString(inputParamMap), JSON.toJSONString(outputParamMap));
        LogUtil.info(logger, "user: {};  url: {};  op_time: {} ms ; param: {}; result: {}", userName, requestPath,
                (endTimeMillis - startTimeMillis), JSON.toJSONString(inputParamMap), JSON.toJSONString(outputParamMap));
        //JSONObject.fromObject(inputParamMap),
        //JSONObject.fromObject(outputParamMap))
    }

}
