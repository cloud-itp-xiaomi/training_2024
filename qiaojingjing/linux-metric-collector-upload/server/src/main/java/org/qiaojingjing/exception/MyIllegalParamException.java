package org.qiaojingjing.exception;

public class MyIllegalParamException extends BaseException{
    public MyIllegalParamException() {
    }

    public MyIllegalParamException(String message) {
        super(message);
    }
}
