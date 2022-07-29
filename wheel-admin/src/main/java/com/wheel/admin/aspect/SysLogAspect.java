package com.wheel.admin.aspect;


import com.alibaba.fastjson.JSON;
import com.wheel.admin.annotation.SystemLogController;
import com.wheel.admin.annotation.SystemLogService;
import com.wheel.admin.model.SysLog;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.service.SysLogService;
import com.wheel.admin.utils.IpUtiles;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 用户操作日志切面
 * @date 2022/7/29 23:09
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;


    @Pointcut("@annotation(com.wheel.admin.annotation.SystemLogService)")
    public void serviceAspect(){}

    @Pointcut("@annotation(com.wheel.admin.annotation.SystemLogController)")
    public void controllerAspect(){}

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //读取用户
        String  name = SecurityContextHolder.getContext().getAuthentication().getName();
        //获取ip
        String ip = IpUtiles.getRealIp(request);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        try {
            //*========控制台输出=========*//
            log.info("==============前置通知开始==============");
            log.info("请求接口 uri:{}"+request.getRequestURI().toString()); //接口
            log.info("请求方式 method:{}"+request.getMethod());
            log.info("方法描述 method_describe:{}" + getControllerMethodDescription(joinPoint));
            log.info("参数信息 params:{}" + Arrays.toString(joinPoint.getArgs()));
            log.info("请求人 username:{}"+name);
            log.info("请求 ip:{}"+ip);
            log.info("请求时间 create_date:{}"+df.format(new Date()));
            String ua = request.getHeader("User-Agent");
            //转成UserAgent对象
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            //获取浏览器信息
            Browser browser = userAgent.getBrowser();
            String browserName = browser.getName();
            log.info("浏览器 browser:{}"+browserName);

            //*========数据库日志=========*//
            SysLog sysLog=new SysLog();
            sysLog.setUri(request.getRequestURI().toString());//请求接口
            sysLog.setMethod(request.getMethod());//请求方式
            sysLog.setMethodDescribe(getControllerMethodDescription(joinPoint));//描述
            sysLog.setParams(Arrays.toString(joinPoint.getArgs()));//参数信息
            sysLog.setUsername(name);//请求人
            sysLog.setIp(ip);//ip
            sysLog.setCreateDate(LocalDateTime.now());//请求时间
            sysLog.setBrowser(browserName);//浏览器类型
            sysLogService.insertSysLog(sysLog);

        }catch (Exception e){
            //记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息：{}",e.getMessage());
        }
    }


    @AfterReturning(returning = "ret", pointcut = "controllerAspect()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("RESPONSE : {}" , ret);
    }



    /**
     * @Description  异常通知 用于拦截service层记录异常日志
     * @date 2018年9月3日 下午5:43
     */
    @AfterThrowing(pointcut = "serviceAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable e){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        SysUser user = (SysUser) session.getAttribute("user");
        //获取请求ip
        String ip = IpUtiles.getRealIp(request);
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        if (joinPoint.getArgs()!=null&&joinPoint.getArgs().length>0){
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params += JSON.parseObject((byte[]) joinPoint.getArgs()[i],JSON.class);
            }
        }
        try{
            /*========控制台输出=========*/
            log.info("=====异常通知开始=====");
            log.info("异常代码:{}" + e.getClass().getName());
            log.info("异常信息:{}" + e.getMessage());
            log.info("异常方法:{}" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.info("方法描述:{}" + getServiceMethodDescription(joinPoint));
            log.info("请求人:{}" + user.getUserName());
            log.info("请求IP:{}" + ip);
            log.info("请求参数:{}" + params);
            /*==========数据库日志=========*/

        }catch (Exception ex){
            //记录本地异常日志
            log.error("==异常通知异常==");
            log.error("异常信息:{}", ex.getMessage());
        }
    }


    /**
     * @Description  获取注解中对方法的描述信息 用于service层注解
     *
     */
    public static String getServiceMethodDescription(JoinPoint joinPoint)throws Exception{
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(SystemLogService.class).description();
                    break;
                }
            }
        }
        return description;
    }



    /**
     * @Description  获取注解中对方法的描述信息 用于Controller层注解
     * @date
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(SystemLogController.class).description();
                    break;
                }
            }
        }
        return description;
    }

}
