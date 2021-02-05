package com.github.easy_http.annotation;

import java.lang.annotation.*;

/**
 * @Author: zt
 * @Date: 2021/1/23 4:51 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface XmlParam {
    /**
     * 参数key名
     * 
     * @return 参数Key名
     */
    String value();
}
