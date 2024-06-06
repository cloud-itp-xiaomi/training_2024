package com.winter.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 手动改实现注入的功能
 * */
@Component
public class BeanUtils implements ApplicationContextAware {

    protected static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.applicationContext == null){
            this.applicationContext = applicationContext;
        }
    }

    /**
     * 获取bean的注入实例对象 ---> 根据名字
     * */
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    /**
     * 手动获取bean的注入实例对象  ---> 根据Class对象
     * */
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
}
