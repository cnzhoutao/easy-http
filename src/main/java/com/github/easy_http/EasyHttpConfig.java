package com.github.easy_http;

import com.github.easy_http.service.DefaultHttpMethodPostProcessor;
import com.github.easy_http.service.DefaultMethodInterceptorStrategyService;
import com.github.easy_http.service.HttpMethodPostProcessor;
import com.github.easy_http.service.MethodInterceptorStrategyService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zt
 * @Date: 2021/2/24 7:04 下午
 */
@Configuration
public class EasyHttpConfig {

    @Bean
    @ConditionalOnMissingBean(value = HttpMethodPostProcessor.class)
    public HttpMethodPostProcessor httpMethodPostProcessor() {
        return new DefaultHttpMethodPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(value = MethodInterceptorStrategyService.class)
    public MethodInterceptorStrategyService methodInterceptorStrategyService() {
        return new DefaultMethodInterceptorStrategyService();
    }


}
