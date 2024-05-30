package com.xiaomi2024.txh.dubbo.spring.consumer.api;

public interface IErrorCode {
    /**
     * 返回码
     */
    long getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
