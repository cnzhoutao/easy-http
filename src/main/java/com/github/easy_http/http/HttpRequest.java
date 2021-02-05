package com.github.easy_http.http;

import com.github.easy_http.response.ResponseInfo;
import com.github.easy_http.util.HeaderUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * http请求工厂
 *
 * @Author: zt
 * @Date: 2021/2/1 7:13 下午
 */
public class HttpRequest extends NetworkRequest {

    @Override
    public ResponseInfo doPost(PostRequest request) {
        String charset = "UTF-8";
        ResponseInfo responseInfo = new ResponseInfo();
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = getHttpClient(StringUtils.startsWithIgnoreCase(request.getUrl(), "https"));
            if (StringUtils.isEmpty(request.getQueryStr())) {
                httpPost = new HttpPost(request.getUrl());
            } else {
                httpPost = new HttpPost(request.getUrl() + "?" + request.getQueryStr());
            }

            if (request.getHeaderMap() != null && request.getHeaderMap().size() > 0) {
                for (Map.Entry<String, String> entry : request.getHeaderMap().entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,la;q=0.7");
            httpPost.addHeader("User-Agent",
                    " Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
            if (request.getCookieMap() != null && request.getCookieMap().size() > 0) {
                String cookieStr = HeaderUtils.constructCookieStr(request.getCookieMap());
                httpPost.addHeader("Cookie", cookieStr);
            }

            if (!StringUtils.isEmpty(request.getReferer())) {
                httpPost.addHeader("Referer", request.getReferer());
            }

            if (request.getFromMap() != null && request.getFromMap().size() > 0) {
                UrlEncodedFormEntity entity =
                        new UrlEncodedFormEntity(HeaderUtils.mapToNameValuePair(request.getFromMap()), charset);
                httpPost.setEntity(entity);
            }

            if (StringUtils.isNotEmpty(request.getPayLoad())) {
                HttpEntity entity = new StringEntity(request.getPayLoad());
                httpPost.setEntity(entity);
            }

            HttpResponse response = httpClient.execute(httpPost);

            if (!ObjectUtils.isEmpty(response)) {
                Header[] headers = response.getAllHeaders();
                responseInfo.setHeaderMap(HeaderUtils.headerToMap(headers));
                int status = response.getStatusLine().getStatusCode();
                responseInfo.setStatusCode(status);
                if (status == 302 || status == 301) {
                    Header realUrl = response.getFirstHeader("location");
                    responseInfo.setRedirectUrl(realUrl.getValue());
                }
                HttpEntity resEntity = response.getEntity();
                if (!ObjectUtils.isEmpty(resEntity)) {
                    if (resEntity.getContentType() != null) {
                        if (!StringUtils.isEmpty(resEntity.getContentType().getValue())) {
                            responseInfo.setPageType(resEntity.getContentType().getValue());
                        }
                    }
                    result = EntityUtils.toString(resEntity, charset);
                    responseInfo.setRes(result);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (!ObjectUtils.isEmpty(httpPost)) {
                httpPost.releaseConnection();
            }
        }
        return responseInfo;
    }

    @Override
    public ResponseInfo doGet(GetRequest request) {
        ResponseInfo responseInfo = new ResponseInfo();
        String charset = "utf-8";
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        try {
            httpClient = getHttpClient(StringUtils.startsWithIgnoreCase(request.getUrl(), "https"));
            if (StringUtils.isEmpty(request.getQueryStr())) {
                httpGet = new HttpGet(request.getUrl());
            } else {
                httpGet = new HttpGet(request.getUrl() + "?" + request.getQueryStr());
            }

            if (request.getCookieMap() != null && request.getCookieMap().size() > 0) {
                String cookieStr = HeaderUtils.constructCookieStr(request.getCookieMap());
                httpGet.addHeader("Cookie", cookieStr);
            }

            if (request.getHeaderMap() != null && request.getHeaderMap().size() > 0) {
                for (Map.Entry<String, String> entry : request.getHeaderMap().entrySet()) {
                    httpGet.addHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            HttpResponse response = httpClient.execute(httpGet);
            responseInfo.setHeaderMap(HeaderUtils.headerToMap(response.getAllHeaders()));
            int status = response.getStatusLine().getStatusCode();
            responseInfo.setStatusCode(status);
            if (status == 302 || status == 301) {
                Header realUrl = response.getFirstHeader("location");
                responseInfo.setRedirectUrl(realUrl.getValue());
            }

            if (!ObjectUtils.isEmpty(response)) {
                HttpEntity resEntity = response.getEntity();
                if (!ObjectUtils.isEmpty(resEntity)) {
                    if (resEntity.getContentType() != null) {
                        if (!StringUtils.isEmpty(resEntity.getContentType().getValue())) {
                            responseInfo.setPageType(resEntity.getContentType().getValue());
                        }
                    }
                    result = EntityUtils.toString(resEntity, charset);
                    responseInfo.setRes(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!ObjectUtils.isEmpty(httpGet)) {
                httpGet.releaseConnection();
            }
        }
        return responseInfo;
    }
}
