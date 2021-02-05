package com.github.easy_http.service;

import java.util.Map;

/**
 * @Author: zt
 * @Date: 2021/1/13 8:45 上午
 */
@FunctionalInterface
public interface HeaderOpService {

    /**
     * header操作的入口
     *
     * @param headerMap header信息
     */
    void opHeader(Map<String, String> headerMap);
}
