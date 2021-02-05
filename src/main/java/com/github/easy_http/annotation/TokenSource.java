package com.github.easy_http.annotation;

import java.lang.annotation.*;

/**
 * 用来标记登录态来源的接口
 * 
 * @Author: zt
 * @Date: 2021/1/16 8:34 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TokenSource {}
