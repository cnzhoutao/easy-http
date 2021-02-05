package com.github.easy_http.annotation;

import com.github.easy_http.registry.ApiImportBeanDefinitionRegistry;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: zt
 * @Date: 2021/1/2 8:31 下午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(value = {ApiImportBeanDefinitionRegistry.class})
public @interface EnableApi {
    /**
     * 需要扫描的基础包路径
     *
     * @return 基础包路径
     */
    String[] basePackages() default {};
}
