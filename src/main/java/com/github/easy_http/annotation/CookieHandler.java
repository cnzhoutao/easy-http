package com.github.easy_http.annotation;

import com.github.easy_http.service.CookieOpService;

import java.lang.annotation.*;

/**
 * @Author: zt
 * @Date: 2021/1/13 8:50 上午
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CookieHandler {
    /**
     * 操作cookie的类
     *
     * @return CookieOpService的实现类
     */
    Class<? extends CookieOpService> cookieHandlerCls();
}
