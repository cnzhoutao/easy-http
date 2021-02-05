package com.github.easy_http.http;

import com.github.easy_http.response.ResponseInfo;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 网络请求工厂
 *
 * @Author: zt
 * @Date: 2021/2/1 7:12 下午
 */
public abstract class NetworkRequest {

    /**
     * 根据http、https获取不同的httpclient
     *
     * @param https 是否是https请求
     * @return httpclient
     */
    HttpClient getHttpClient(boolean https) {
        if (https) {
            try {
                return new SSLClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return new DefaultHttpClient();
        }
    }

    /**
     * post 请求
     *
     * @param postRequest post请求参数
     * @return resp
     */
    abstract ResponseInfo doPost(PostRequest postRequest);

    /**
     * get请求
     *
     * @param getRequest get请求参数
     * @return resp
     */
    abstract ResponseInfo doGet(GetRequest getRequest);

}
