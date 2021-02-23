package com.github.easy_http.proxy;

import com.alibaba.fastjson.JSON;
import com.github.easy_http.annotation.*;
import com.github.easy_http.context.RequestContext;
import com.github.easy_http.enums.HttpMethodEnum;
import com.github.easy_http.http.NetWorkManager;
import com.github.easy_http.response.ResponseInfo;
import com.github.easy_http.service.*;
import com.github.easy_http.util.CHThreadLocalUtils;
import com.github.easy_http.util.JsonXmlUtils;
import com.github.easy_http.util.SpringContextUtil;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zt
 * @Date: 2021/1/10 10:42 上午
 */
public abstract class ProxyHandler<T> implements InvocationHandler {

    private Class<?> cls;

    private String hostName;

    private boolean https;

    private String url;

    public ProxyHandler(Class<?> cls) {
        this.cls = cls;
    }

    /**
     * 创建代理，如果目标类没有实现接口则返回自己
     */
    @SuppressWarnings("unchecked")
    public T getProxy(String hostName, boolean https, String url) {
        this.hostName = hostName;
        this.https = https;
        this.url = url;
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, this);
    }

    /***
     * 通过代理实现调用，在里面加上方法增强的逻辑
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HttpMethodPostProcessor httpMethodPostProcessor = new DefaultHttpMethodPostProcessor();
        MethodInterceptorStrategyService methodInterceptorStrategyService =
                SpringContextUtil.getBean(MethodInterceptorStrategyService.class);
        if (methodInterceptorStrategyService != null) {
            httpMethodPostProcessor = methodInterceptorStrategyService.supplyMethod(method.getName());
        }

        try {
            // 调用前的业务处理
            httpMethodPostProcessor.before(args);
            RequestContext requestContext = this.parseRequestParam2Context(method, args);
            // 调用后的业务处理
            httpMethodPostProcessor.after(args);
            // header前置处理
            this.executeHeaderHandler(requestContext);
            // cookie前置处理
            this.executeCookieHandler(requestContext);
            // 执行http请求或者https请求
            ResponseInfo responseInfo = this.execute(requestContext);
            if (responseInfo == null) {
                return null;
            }

            // 标注了token来源,将cookie和header放入ThreadLocal
            if (requestContext.isTokenSource()) {
                CHThreadLocalUtils.addHeaderMap(responseInfo.getHeaderMap());
                CHThreadLocalUtils.addCookieMap(responseInfo.getCookieMap());
            }

            // 包装成具体的返回值类型
            return this.wrapToReturnType(requestContext, responseInfo);
        } catch (Exception e) {
            // 自定义的异常方法
            e.printStackTrace();
            httpMethodPostProcessor.onException(e);
        }
        return null;
    }

    /**
     * 包装成返回值具体类型
     *
     * @param requestContext 请求参数
     * @param responseInfo   http请求返回参数
     * @return 解析成对象的返回值
     * @throws ClassNotFoundException 异常
     */
    private Object wrapToReturnType(RequestContext requestContext, ResponseInfo responseInfo)
            throws ClassNotFoundException {
        if (String.class.equals(requestContext.getReturnType())) {
            return responseInfo.getRes();
        } else if (List.class.equals(requestContext.getReturnType())) {
            ParameterizedType genericReturnType1 = requestContext.getGenericReturnType();
            Type[] actualTypeArguments = genericReturnType1.getActualTypeArguments();
            String typeName = actualTypeArguments[0].getTypeName();
            Class<?> aClass = Class.forName(typeName);
            return JSON.parseArray(responseInfo.getRes(), aClass);
        } else {
            return JSON.parseObject(responseInfo.getRes(), requestContext.getReturnType());
        }
    }

    /**
     * cookie 前置处理
     *
     * @param requestContext 请求参数封装对象
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 反射访问异常
     */
    private void executeCookieHandler(RequestContext requestContext)
            throws InstantiationException, IllegalAccessException {
        if (requestContext.getCookieHandlerCls() != null) {
            Class<? extends CookieOpService> cookieHandlerCls = requestContext.getCookieHandlerCls();
            CookieOpService cookieOpService = cookieHandlerCls.newInstance();
            cookieOpService.opCookie(CHThreadLocalUtils.getCookieMap());
        }
    }

    /**
     * header前置处理
     *
     * @param requestContext 请求参数的封装
     * @throws InstantiationException 实例化异常
     * @throws IllegalAccessException 反射异常
     */
    private void executeHeaderHandler(RequestContext requestContext)
            throws IllegalAccessException, InstantiationException {
        // header 前置处理
        if (requestContext.getHeaderHandlerCls() != null) {
            Class<? extends HeaderOpService> headerHandlerCls = requestContext.getHeaderHandlerCls();
            HeaderOpService headerOpService = headerHandlerCls.newInstance();
            headerOpService.opHeader(CHThreadLocalUtils.getHeaderMap());
        }
    }

    /**
     * 解析方法上的注解，及方法参数上的注解
     *
     * @param method 方法
     * @param args   入参
     * @return context
     */
    private RequestContext parseRequestParam2Context(Method method, Object[] args) {

        // 参数上的注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        // 返回值类型,可能是List
        Class<?> returnType = method.getReturnType();
        // 方法上的注解
        Annotation[] annotations = method.getAnnotations();
        RequestContext requestContext = new RequestContext();
        requestContext.setHostName(hostName);
        requestContext.setHttps(https);
        requestContext.setReturnType(returnType);

        // 是否是基本类型
        if (returnType.isPrimitive() || returnType.isAssignableFrom(String.class)) {

        } else {
            // 返回值具体类型
            ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
            requestContext.setGenericReturnType(genericReturnType);
        }

        // 解析参数上的注解
        this.parseFieldAnnotation(args, parameterAnnotations, requestContext);
        // 解析方法上的注解
        this.parseMethodAnnotation(annotations, requestContext);
        // 处理PathVariable
        this.handlePathVariable(requestContext);

        return requestContext;
    }

    /**
     * 解析参数上的注解
     *
     * @param args                 参数列表
     * @param parameterAnnotations 参数上的注解
     * @param requestContext       解析结果存放地
     */
    private void parseFieldAnnotation(Object[] args, Annotation[][] parameterAnnotations,
                                      RequestContext requestContext) {
        Map<String, String> formMap = new HashMap<>();
        String bodyJson = null;
        int paramIndex = 0;
        Map<String, String> pathMap = new HashMap<>();
        for (Annotation[] annotationList : parameterAnnotations) {
            Object arg = args[paramIndex++];
            for (Annotation itemAnno : annotationList) {
                if (isSameAnnotation(itemAnno, RequestParam.class)) {
                    RequestParam requestParam = (RequestParam) itemAnno;
                    formMap.put(requestParam.name(), String.valueOf(arg));
                } else if (isSameAnnotation(itemAnno, RequestBody.class)) {
                    if (arg != null) {
                        bodyJson = JSON.toJSONString(arg);
                    }
                } else if (isSameAnnotation(itemAnno, XmlParam.class)) {
                    XmlParam xmlParam = (XmlParam) itemAnno;
                    formMap.put(xmlParam.value(), String.valueOf(arg));
                } else if (isSameAnnotation(itemAnno, XmlBody.class)) {
                    String json = JSON.toJSONString(arg);
                    bodyJson = JsonXmlUtils.json2Xml(json);
                } else if (isSameAnnotation(itemAnno, PathVariable.class)) {
                    PathVariable pathVariable = (PathVariable) itemAnno;
                    String name = pathVariable.name();
                    String val = String.valueOf(arg);
                    pathMap.put(name, val);
                }
            }
        }

        requestContext.setBodyJson(bodyJson);
        requestContext.setFormMap(formMap);
    }

    /**
     * 解析方法上的注解，放入requestContext
     *
     * @param annotations    方法上的注解结合
     * @param requestContext 接收解析后的一些参数
     */
    private void parseMethodAnnotation(Annotation[] annotations, RequestContext requestContext) {
        String completUrl = url;
        // 遍历方法上的所有注解
        for (Annotation annotation : annotations) {
            if (isSameAnnotation(annotation, GetMapping.class)) {
                GetMapping getMapping = (GetMapping) annotation;
                requestContext.setHttpMethodEnum(HttpMethodEnum.GET);
                completUrl += getMapping.value()[0];
            } else if (isSameAnnotation(annotation, PostMapping.class)) {
                PostMapping postMapping = (PostMapping) annotation;
                requestContext.setHttpMethodEnum(HttpMethodEnum.POST);
                completUrl += postMapping.value()[0];
            } else if (isSameAnnotation(annotation, CookieHandler.class)) {
                CookieHandler cookieHandler = (CookieHandler) annotation;
                Class<? extends CookieOpService> cookieHandlerCls = cookieHandler.cookieHandlerCls();
                requestContext.setCookieHandlerCls(cookieHandlerCls);
            } else if (isSameAnnotation(annotation, HeaderHandler.class)) {
                HeaderHandler cookieHandler = (HeaderHandler) annotation;
                Class<? extends HeaderOpService> headerHandlerCls = cookieHandler.headerHandlerCls();
                requestContext.setHeaderHandlerCls(headerHandlerCls);
            } else if (isSameAnnotation(annotation, TokenSource.class)) {
                requestContext.setTokenSource(true);
            } else {
                throw new RuntimeException("次注解:" + annotation + "暂时不支持!");
            }
        }
        requestContext.setUrl(completUrl);
    }

    /**
     * 处理@PathVariable 注解
     *
     * @param requestContext 请求参数
     */
    private void handlePathVariable(RequestContext requestContext) {
        Map<String, String> pathMap = requestContext.getPathMap();
        if (pathMap == null || pathMap.size() == 0) {
            return;
        }
        String url = requestContext.getUrl();
        for (Map.Entry<String, String> entry : pathMap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            url = url.replace("{" + k + "}", v);
        }
        requestContext.setUrl(url);
    }

    /**
     * 判断annotation和 targetClas是否是同一个注解
     *
     * @param annotation 一直注解
     * @param targetCls  待确认类型注解
     * @return 是否类型相同
     */
    private boolean isSameAnnotation(Annotation annotation, Class<?> targetCls) {
        return targetCls.getName().equals(annotation.annotationType().getName());
    }

    private ResponseInfo execute(RequestContext requestContext) {
        requestContext.setCookieMap(CHThreadLocalUtils.getCookieMap());
        requestContext.setHeaderMap(CHThreadLocalUtils.getHeaderMap());
        // http
        if (!requestContext.isHttps()) {
            String url = "http://" + requestContext.getHostName() + requestContext.getUrl();
            requestContext.setUrl(url);
        } else {
            // https
            String url = "https://" + requestContext.getHostName() + requestContext.getUrl();
            requestContext.setUrl(url);
        }
        //执行请求
        return NetWorkManager.execute(requestContext);
    }
}
