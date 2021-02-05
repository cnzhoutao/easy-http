package com.github.easy_http.util;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zt
 * @Date: 2019-03-08 15:00
 */

public class HeaderUtils {

    /**
     * 从header中提取出cookie内容
     * 
     * @param map
     *            存放header的map
     * @return cookie string
     */
    public static String constructCookieStr(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        StringBuilder cookie = new StringBuilder();
        for (Map.Entry<String, String> header : map.entrySet()) {
            if ("Set-Cookie".equals(header.getKey())) {
                String cookieInfo = header.getValue().split(";")[0];
                cookie.append(cookieInfo).append("; ");
            }
        }
        return cookie.toString();
    }

    /**
     * header[] 转map
     * 
     * @param headers
     *            header数组
     * @return map
     */
    public static Map<String, String> headerToMap(Header[] headers) {
        Map<String, String> headerMap = new HashMap<>();;
        if (headers == null || headers.length <= 0) {
            return headerMap;
        }
        for (Header header : headers) {
            headerMap.put(header.getName(), header.getValue());
        }
        return headerMap;
    }

    /**
     * map 转nameValuePair列表
     * 
     * @param formMap
     *            请求参数
     * @return list
     */
    public static List<NameValuePair> mapToNameValuePair(Map<String, String> formMap) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        if (formMap != null && formMap.size() > 0) {
            for (Map.Entry<String, String> entry : formMap.entrySet()) {
                BasicNameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                nameValuePairList.add(nameValuePair);
            }
        }
        return nameValuePairList;
    }

    /**
     * map转get查询参数
     * 
     * @param formMap
     *            map
     * @return str
     */
    public static String mapToQueryStr(Map<String, String> formMap) {
        if (formMap == null || formMap.size() == 0) {
            return null;
        }
        boolean flag = false;
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : formMap.entrySet()) {
            if (!flag) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                flag = true;
            } else {
                stringBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return stringBuilder.toString();
    }
}
