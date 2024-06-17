package com.cl.server.exception;

/**
 * 异常包装
 *
 * @author: tressures
 * @date: 2024/6/2
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

}