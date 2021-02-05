package com.github.easy_http.context;


import com.github.easy_http.enums.HttpMethodEnum;
import com.github.easy_http.service.CookieOpService;
import com.github.easy_http.service.HeaderOpService;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * @Author: zt
 * @Date: 2021/1/10 7:36 下午
 */
public class RequestContext implements Serializable {

    /**
     * 域名或者ip
     */
    private String hostName;

    /**
     * 是https?
     */
    private boolean https;

    /**
     * url路径
     */
    private String url;

    /**
     * body参数
     */
    private String bodyJson;

    /**
     * form参数
     */
    private Map<String, String> formMap;

    /**
     * url路径中@PathVariable 对应的kv
     */
    private Map<String, String> pathMap;

    /**
     * header 参数
     */
    private Map<String, String> headerMap;

    /**
     * cookie列表
     */
    private Map<String, String> cookieMap;

    // 返回值具体类型
    ParameterizedType genericReturnType;

    // 返回值类型,可能是List
    Class<?> returnType;

    /**
     * 请求类型
     */
    private HttpMethodEnum httpMethodEnum;

    /**
     * 操作cookie的类
     */
    private Class<? extends CookieOpService> cookieHandlerCls;

    /**
     * 操作header的类
     */
    private Class<? extends HeaderOpService> headerHandlerCls;

    /**
     * 是否是token的来源接口（cookie/header）
     */
    private boolean tokenSource;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBodyJson() {
        return bodyJson;
    }

    public void setBodyJson(String bodyJson) {
        this.bodyJson = bodyJson;
    }

    public Map<String, String> getFormMap() {
        return formMap;
    }

    public void setFormMap(Map<String, String> formMap) {
        this.formMap = formMap;
    }

    public Map<String, String> getPathMap() {
        return pathMap;
    }

    public void setPathMap(Map<String, String> pathMap) {
        this.pathMap = pathMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public HttpMethodEnum getHttpMethodEnum() {
        return httpMethodEnum;
    }

    public void setHttpMethodEnum(HttpMethodEnum httpMethodEnum) {
        this.httpMethodEnum = httpMethodEnum;
    }

    public ParameterizedType getGenericReturnType() {
        return genericReturnType;
    }

    public void setGenericReturnType(ParameterizedType genericReturnType) {
        this.genericReturnType = genericReturnType;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Class<? extends CookieOpService> getCookieHandlerCls() {
        return cookieHandlerCls;
    }

    public void setCookieHandlerCls(Class<? extends CookieOpService> cookieHandlerCls) {
        this.cookieHandlerCls = cookieHandlerCls;
    }

    public Class<? extends HeaderOpService> getHeaderHandlerCls() {
        return headerHandlerCls;
    }

    public void setHeaderHandlerCls(Class<? extends HeaderOpService> headerHandlerCls) {
        this.headerHandlerCls = headerHandlerCls;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public boolean isTokenSource() {
        return tokenSource;
    }

    public void setTokenSource(boolean tokenSource) {
        this.tokenSource = tokenSource;
    }
}
