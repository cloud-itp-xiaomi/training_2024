package com.example.springcloud.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ErrorCode
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 20:35
 **/
@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 公共错误码 0 ~ 10000
     */
    SUCCESS(0, "成功"),

    PARAM_ERROR(400, "参数不合法"),

    SYSTEM_ERROR(500, "失败"),

    FILE_EXCEPTION(1001, "文件处理错误"),

    ;

    private final int code;
    private final String msg;

    public static ErrorCode of(Integer code) {
        if (code == null) {
            return null;
        }

        for (ErrorCode errorCode : ErrorCode.values()) {
            if (code.equals(errorCode.getCode())) {
                return errorCode;
            }
        }

        return null;
    }
}
