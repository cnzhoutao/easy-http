package com.github.easy_http.enums;

/**
 * @Author: zt
 * @Date: 2021/1/10 8:47 下午
 */
public enum HttpMethodEnum {
    //get
    GET("get"),
    //post
    POST("post");
    //请求方法
    private final String method;

    HttpMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
