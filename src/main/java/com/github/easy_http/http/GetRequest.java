package com.github.easy_http.http;

import com.github.easy_http.context.RequestContext;

/**
 * @Author: zt
 * @Date: 2021/2/1 7:30 下午
 */
public class GetRequest extends AbstractRequest {

    public GetRequest(RequestContext requestContext) {
        super(requestContext.getUrl());
        setCookieMap(requestContext.getCookieMap());
        setHeaderMap(requestContext.getHeaderMap());
        setFromMap(requestContext.getFormMap());
        setPathMap(requestContext.getPathMap());
    }
}
