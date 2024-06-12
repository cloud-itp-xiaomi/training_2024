package com.example.hostmonitor.aspect;

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

    @Around("execution(* com.example.hostmonitor.service.*.save*(..))")
    public Object recordUpload(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getName();
        Object res = joinPoint.proceed();
        log.info("Method: " + name + " successfully upload data");
        return res;
    }

    @Around("execution(* com.example.hostmonitor.service.*.query*(..))")
    public Object recordQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getName();
        Object res = joinPoint.proceed();
        log.info("Method: " + name + " successfully read data");
        return res;
    }

    @AfterThrowing(pointcut = "execution(* com.example.hostmonitor.service.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex){
        String name = joinPoint.getSignature().getName();
        log.error("Method: " + name + " execution failed");
    }

}
