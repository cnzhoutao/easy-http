package com.github.easy_http.annotation;

import java.lang.annotation.*;

/**
 * @Author: zt
 * @Date: 2021/1/1 4:11 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {

    String url() default "/";

    String hostName() default "127.0.0.1:8080";

    boolean https() default false;

}
