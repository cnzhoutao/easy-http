package com.github.easy_http.service;

/**
 * 方法执行前中后筛选对应的处理器的service
 *
 * @author zt
 * @Date: 2021/2/23 7:06 下午
 */
public interface MethodInterceptorStrategyService {

    /**
     * 获取在http请求执行过程中的处理类
     *
     * @param methodName 方法名
     * @return http请求前中后的处理器
     */
    default public HttpMethodPostProcessor supplyMethod(String methodName) {
        return new DefaultHttpMethodPostProcessor();
    }

}
