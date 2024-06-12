package com.lx.collector.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class GetBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 通过类的class从容器中手动获取对象
     */
    public static  <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public  void setApplicationContext(ApplicationContext app) throws BeansException {
        GetBeanUtil.applicationContext = app;
    }
}
