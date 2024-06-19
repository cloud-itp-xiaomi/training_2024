package com.example.springcloud.controller.request;

import lombok.Data;

/**
 * @ClassName TagRequest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 20:57
 **/
@Data
public class TagRequest {
    /**
     * key
     */
    private String key;
    /**
     * 标签名
     */
    private String label;
}
