package com.example.springcloud.controller.request;

import lombok.Data;

import java.util.List;

/**
 * @ClassName LogUploadRequest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-23 22:58
 **/
@Data
public class LogUploadRequest {
    private String hostName;
    private String file;
    private List<String> logs;
}
