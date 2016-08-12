package com.Qiao.aspact;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by white and black on 2016/8/10.
 */
@Aspect
@Component
public class LogAspect {
    private  static final Logger logger= Logger.getLogger(LogAspect.class);
    @Before("execution(* com.Qiao.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb=new StringBuilder();
        for(Object arg:joinPoint.getArgs()){
            if(arg!=null){
                sb.append("arg:"+arg.toString()+"|");
            }
            logger.info("before method:"+sb.toString());
        }
    }
    @After("execution(* com.Qiao.controller.*Controller.*(..))")
    public void afterMethod(){
        logger.info("after method"+new Date());
    }
}
