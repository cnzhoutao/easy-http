package com.github.easy_http.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author zt
 * @Date: 2021/2/23 7:29 下午
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext = applicationContext;
    }

    public static  <T> T getBean(Class<? extends T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
