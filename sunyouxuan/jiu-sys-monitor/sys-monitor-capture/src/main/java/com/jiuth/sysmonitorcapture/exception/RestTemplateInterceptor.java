package com.jiuth.sysmonitorcapture.exception;

import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    /**
     *
     * @param request the request, containing method, URI, and headers
     * @param body the body of the request
     * @param execution the request execution
     * @return
     */
    @Override
    public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution)  {
        try {
            // 在请求执行之前做一些操作，例如记录请求信息等

            // 执行请求并获取响应
            ClientHttpResponse response = execution.execute(request, body);

            // 在获取响应后做一些操作，例如记录响应信息等

            return response;
        } catch (IOException ex) {
            // 捕获异常并输出到控制台
            System.err.println("RestTemplate Interceptor Exception: " + ex.getMessage());
            return null;
        }
    }
}