package com.github.easy_http.service;

/**
 * @author zt
 * @Date: 2021/2/23 7:15 下午
 */

public class DefaultHttpMethodPostProcessor implements HttpMethodPostProcessor {
    /**
     * 执行http请求发生异常
     *
     * @param ex 异常信息
     */
    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * 执行http请求后
     *
     * @param args 参数
     */
    @Override
    public void after(Object[] args) {

    }

    /**
     * 执行http请求前
     *
     * @param args 参数
     */
    @Override
    public void before(Object[] args) {
    }
}
