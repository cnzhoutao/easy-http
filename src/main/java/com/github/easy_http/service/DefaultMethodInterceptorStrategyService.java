package com.github.easy_http.service;

/**
 * @author zt
 * @Date: 2021/2/24 7:09 下午
 */
public class DefaultMethodInterceptorStrategyService implements MethodInterceptorStrategyService {
    @Override
    public HttpMethodPostProcessor supplyMethod(String methodName) {
        return new DefaultHttpMethodPostProcessor();
    }
}
