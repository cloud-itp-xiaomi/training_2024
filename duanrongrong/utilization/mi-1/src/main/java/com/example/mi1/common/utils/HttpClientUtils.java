package com.example.mi1.common.utils;

import com.alibaba.fastjson2.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author uchin/李玉勤
 * @date 2023/5/18 09:47
 * @description
 */
public class HttpClientUtils {
    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(30000)
            .setConnectTimeout(30000)
            .setSocketTimeout(30000)
            .build();
    public static final CloseableHttpClient HTTP_CLIENT;

    static {
        HttpClientBuilder builder = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(30);
        HTTP_CLIENT = builder.build();
    }

    public static final ResponseHandler<String> defaultResHandler = httpResponse -> {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = httpResponse.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    };

    public static <T> T post(String url, Map<String, Object> parameters, ResponseHandler<T> responseHandler) throws IOException {
        return post(url, null, parameters, responseHandler);
    }

    public static <T> T post(String url, Header header, Map<String, Object> parameters, ResponseHandler<T> responseHandler) throws IOException {
        HttpPost post = new HttpPost(url);
        return post(post, header, parameters, responseHandler);
    }

    public static <T> T post(String url, ResponseHandler<T> responseHandler) throws IOException {
        return post(new HttpPost(url), responseHandler);
    }

    public static <T> T post(HttpPost post, ResponseHandler<T> responseHandler) throws IOException {
        return post(post, null, responseHandler);
    }

    public static <T> T post(HttpPost post, Map<String, Object> parameters, ResponseHandler<T> responseHandler) throws IOException {
        return post(post, null, parameters, responseHandler);
    }

    public static <T> T post(HttpPost post, Header header, Map<String, Object> parameters, ResponseHandler<T> responseHandler) throws IOException {
        String parameterStr = null;
        if (parameters != null) {
            parameterStr = JSON.toJSONString(parameters);
        }
        return post(post, header, parameterStr, responseHandler);
    }

    public static <T> T post(String url, String entity, ResponseHandler<T> responseHandler) throws IOException {
        return post(url, null, entity, responseHandler);
    }

    public static <T> T post(String url, Header header, String entity, ResponseHandler<T> responseHandler) throws IOException {
        HttpPost post = new HttpPost(url);
        return post(post, header, entity, responseHandler);
    }

    /**
     * post请求
     *
     * @param post            post请求
     * @param header          http头
     * @param entity          参数
     * @param responseHandler 响应处理
     * @param <T>             T 类
     * @return T Obj
     * @throws IOException IOException
     */
    public static <T> T post(HttpPost post, Header header, String entity, ResponseHandler<T> responseHandler) throws IOException {
        if (entity != null) {
            StringEntity parameterEntity = new StringEntity(entity, ContentType.APPLICATION_JSON);
            post.setEntity(parameterEntity);
        }
        post.setHeader(header);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        return execute(post, responseHandler);
    }

    public static <T> T get(String url, ResponseHandler<T> responseHandler) throws IOException {
        return get(url, null, responseHandler);
    }

    public static <T> T get(String url, Header header, ResponseHandler<T> responseHandler) throws IOException {
        HttpGet get = new HttpGet(url);
        get.setHeader(header);
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Accept", "application/json");
        return execute(get, responseHandler);
    }

    public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) throws IOException {
        return HTTP_CLIENT.execute(request, responseHandler);
    }

    public static void checkStatus(HttpResponse response) throws IllegalStateException {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            throw new IllegalStateException("response failed, " + statusLine + ": " + response);
        }
    }
}

