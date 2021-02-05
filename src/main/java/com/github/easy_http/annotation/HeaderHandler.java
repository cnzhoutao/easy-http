package com.github.easy_http.annotation;

import com.github.easy_http.service.HeaderOpService;

import java.lang.annotation.*;

/**
 * @Author: zt
 * @Date: 2021/1/13 8:55 上午
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HeaderHandler {

    /**
     * 操作header的入口
     *
     * @return HeaderOpService的实现类
     */
    Class<? extends HeaderOpService> headerHandlerCls();

}
