package com.txh.xiaomi2024.work.collector.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringUtil.context = applicationContext;
    }
    /**
     * 获取 Spring Bean
     *
     * @param clazz 类
     * @param <T>   泛型
     * @return 对象
     */
    public static <T> T getBean(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        return context.getBean(clazz);
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public static ApplicationContext getContext() {
        if (context == null) {
            throw new RuntimeException("There has no Spring ApplicationContext!");
        }
        return context;
    }
}
