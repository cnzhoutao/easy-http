package com.github.easy_http.http;

import com.github.easy_http.context.RequestContext;

/**
 * @Author: zt
 * @Date: 2021/2/1 7:30 下午
 */
public class PostRequest extends AbstractRequest {
    /**
     * post请求的body参数
     */
    private String payLoad;

    public PostRequest(RequestContext requestContext) {
        super(requestContext.getUrl());
        setPayLoad(requestContext.getBodyJson());
        setCookieMap(requestContext.getCookieMap());
        setHeaderMap(requestContext.getHeaderMap());
        setFromMap(requestContext.getFormMap());
        setPathMap(requestContext.getPathMap());
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }
}
