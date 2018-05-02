package com.quora.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/25 19:50
 * @version: 1.0
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.quora.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object obj: joinPoint.getArgs()) {
            sb.append(obj + ", ");
        }
        logger.info("before method: " + sb.toString());
    }

    @After("execution(* com.quora.controller.*.*(..))")
    public void afterMethod() {
        logger.info("after method");
    }
}
