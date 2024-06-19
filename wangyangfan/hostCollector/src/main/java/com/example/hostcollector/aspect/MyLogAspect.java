package com.example.hostcollector.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class MyLogAspect {

    @Around("execution(* com.example.hostcollector.service.*.*(..))")
    public Object recordUpload(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getName();
        Object res = joinPoint.proceed();
        log.info("Method: " + name + " successfully uploaded data at timestamp " + System.currentTimeMillis() / 1000 + "(seconds)");
        return res;
    }

    @AfterThrowing(pointcut = "execution(* com.example.hostcollector.service.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex){
        String name = joinPoint.getSignature().getName();
        log.error("Method: " + name + " failed to upload data at timestamp " + System.currentTimeMillis() / 1000 + "(seconds)");
    }

}
