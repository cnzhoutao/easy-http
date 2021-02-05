package com.github.easy_http.fatory;

import com.github.easy_http.proxy.ProxyUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @Author: zt
 * @Date: 2021/1/9 5:32 下午
 */
public class ApiFactoryBean implements FactoryBean<Object>, InitializingBean {

    private String hostName;

    private boolean https;

    private String url;

    private Class<?> type;

    @Override
    public Object getObject() throws Exception {
        return getTarget();
    }

    <T> T getTarget() {
        return new ProxyUtil<T>(type).getProxy(hostName,https,url);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(type, "type must not be null!");
        Assert.notNull(url, "url must not be null!");
    }
}
