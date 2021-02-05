package com.github.easy_http.http;

import com.github.easy_http.context.RequestContext;
import com.github.easy_http.enums.HttpMethodEnum;
import com.github.easy_http.response.ResponseInfo;

public final class NetWorkManager {
    /**
     * 执行具体的http请求
     *
     * @param requestContext
     *            请求参数
     * @return 网络请求结果
     */
    public static ResponseInfo execute(RequestContext requestContext) {
        if (HttpMethodEnum.GET.getMethod().equals(requestContext.getHttpMethodEnum().getMethod())) {
            return getClient().doGet(new GetRequest(requestContext));
        } else if (HttpMethodEnum.POST.getMethod().equals(requestContext.getHttpMethodEnum().getMethod())) {
            return getClient().doPost(new PostRequest(requestContext));
        }
        return null;
    }

    private static NetworkRequest getClient() {
        return new HttpRequest();
    }
}
