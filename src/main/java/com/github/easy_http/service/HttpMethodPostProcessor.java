package com.github.easy_http.service;

/**
 * @author zt
 * @Date: 2021/2/23 7:08 下午
 */
public interface HttpMethodPostProcessor {

    /**
     * 执行http请求发生异常
     *
     * @param ex 异常信息
     */
    public void onException(Exception ex);

    /**
     * 执行http请求后
     *
     * @param args 参数
     */
    public void after(Object[] args);

    /**
     * 执行http请求前
     *
     * @param args 参数
     */
    public void before(Object[] args);
}
