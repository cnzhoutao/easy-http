package com.github.easy_http.response;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: zt
 * @Date: 2021/1/13 7:19 下午
 */
public class ResponseInfo implements Serializable {

    /**
     * header map
     */
    public Map<String, String> headerMap;

    /**
     * cookieMap
     */
    public Map<String, String> cookieMap;

    /**
     * 请求返回的数据，可能是html,也可能是json
     */
    public String res;

    /**
     * Http请求返回的状态码
     */
    public Integer statusCode;

    /**
     * 页面类型
     */
    public String pageType;

    /**
     * 重定向后的地址
     */
    public String redirectUrl;

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
