package com.github.easy_http.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zt
 * @Date: 2021/1/16 7:58 下午
 */
public class CHThreadLocalUtils {

    private static final ThreadLocal<Map<String, Map<String, String>>> LOCAL = new ThreadLocal<>();

    private static final String COOKIE_KEY = "cookie";

    private static final String HEADER_KEY = "header";

    private static Map<String, Map<String, String>> getAll() {
        Map<String, Map<String, String>> cookieHeaderMap = LOCAL.get();
        if (cookieHeaderMap == null) {
            cookieHeaderMap = new HashMap<>();
            LOCAL.set(cookieHeaderMap);
        }
        return cookieHeaderMap;
    }

    public static void addCookieMap(Map<String, String> cookieMap) {
        if (cookieMap == null) {
            return;
        }
        Map<String, String> localCookieMap = getAll().computeIfAbsent(COOKIE_KEY, key -> new HashMap<>());
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            localCookieMap.put(entry.getKey(), entry.getValue());
        }
    }

    public static Map<String, String> getCookieMap() {
        return getAll().computeIfAbsent(COOKIE_KEY, k -> new HashMap<>());
    }

    public static void addHeaderMap(Map<String, String> headerMap) {
        if (headerMap == null) {
            return;
        }
        Map<String, String> localHeaderMap = getAll().computeIfAbsent(HEADER_KEY, k -> new HashMap<>());
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            localHeaderMap.put(entry.getKey(), entry.getValue());
        }
    }

    public static Map<String, String> getHeaderMap() {
        return getAll().computeIfAbsent(HEADER_KEY, k -> new HashMap<>());
    }

    public static void clear() {
        LOCAL.remove();
    }

}
