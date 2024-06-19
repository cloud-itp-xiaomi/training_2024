package org.example.exception;

/**
 * @author liuhaifeng
 * @date 2024/05/30/0:07
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }
}
