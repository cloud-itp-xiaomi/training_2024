package org.qiaojingjing.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.qiaojingjing.annotation.AutoFill;
import org.qiaojingjing.constant.AutoFillConstant;
import org.qiaojingjing.enums.OperationType;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 自动填充插入/更新数据库时，时间字段的填充
 *
 * @author qiaojingjing
 * @version 0.1.0
 * @since 0.1.0
 **/

@Aspect
@Slf4j
@Component
public class AutoFillTimeAspect {
    @Pointcut("execution(* org.qiaojingjing.mapper.*.*(..)) && @annotation(org.qiaojingjing.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFillTime(JoinPoint joinPoint) {
        log.info("进行时间字段自动填充");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod()
                .getAnnotation(AutoFill.class);
        OperationType operateType = autoFill.value();

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        switch (operateType) {
            case UPDATE:
                try {
                    Object entity = args[0];
                    Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    setUpdateTime.invoke(entity, now);
                } catch (IllegalAccessException
                         | InvocationTargetException
                         | NoSuchMethodException e) {
                    log.error("填充更新操作出错{}", e.getMessage());
                }
                break;
            case INSERT:
                if (args[0] instanceof List) {
                    List<?> entities = (List<?>) args[0];
                    for (Object entity : entities) {
                        try {
                            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                            setCreateTime.invoke(entity, now);
                            setUpdateTime.invoke(entity, now);
                        } catch (NoSuchMethodException
                                 | IllegalAccessException
                                 | InvocationTargetException e) {
                            log.error("填充插入操作出错{}", e.getMessage());
                        }
                    }
                }
                break;
            default:
                log.error("没有这样的方法...");
        }
    }
}
