package com.github.easy_http.http;

import java.util.Map;

/**
 * 网络请求参数
 * 
 * @Author: zt
 * @Date: 2021/2/1 7:25 下午
 */
public abstract class AbstractRequest {
    /**
     * 网络请求地址 带协议,域名/ip,端口,api
     */
    private String url;
    /**
     * 链接上的参数
     */
    private String queryStr;

    /**
     * form data列表
     */
    private Map<String, String> fromMap;

    /**
     * url路径中@PathVariable 对应的kv
     */
    private Map<String, String> pathMap;

    /**
     * header 参数
     */
    private Map<String, String> headerMap;

    /**
     * cookie 参数
     */
    private Map<String, String> cookieMap;

    /**
     * 上一次请求地址
     */
    private String referer;

    public AbstractRequest(String url) {
        this.url = url;
    }

    public String getQueryStr() {
        return queryStr;
    }

    public String getUrl() {
        return url;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    public Map<String, String> getFromMap() {
        return fromMap;
    }

    public void setFromMap(Map<String, String> fromMap) {
        this.fromMap = fromMap;
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

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
