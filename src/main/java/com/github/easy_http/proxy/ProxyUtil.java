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
        System.out.println("代理发生异常:" + ex.getMessage());
        ex.getMessage();
    }

    @Override
    public void after(Object[] args) {
        System.out.println("执行代理方法后");

    }

    @Override
    public void before(Object[] args) {
        System.out.println("执行代理方法前");
    }

}
