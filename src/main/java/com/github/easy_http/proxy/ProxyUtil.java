package com.github.easy_http.proxy;

/**
 * @Author: zt
 * @Date: 2021/1/10 10:43 上午
 */
public class ProxyUtil<T> extends ProxyHandler<T> {

    public ProxyUtil(Class<?> cls) {
        super(cls);
    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void after(Object[] args) {

    }

    @Override
    public void before(Object[] args) {
    }

}
