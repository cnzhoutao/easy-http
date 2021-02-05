package com.github.easy_http.service;

import java.util.Map;

/**
 * @Author: zt
 * @Date: 2021/1/13 8:43 上午
 */
@FunctionalInterface
public interface CookieOpService {

    /**
     * cookie操作的入口
     *
     * @param cookieMap cookie信息
     */
    void opCookie(Map<String, String> cookieMap);

}
