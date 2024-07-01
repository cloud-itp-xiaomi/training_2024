package com.example.springcloud.controller.request;

import lombok.Data;

import java.util.List;

/**
 * @ClassName LogUploadRequest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-22 23:40
 **/
@Data
public class LogUploadRequest {
    //主机名
    private String hostName;
    //文件路径
    private String file;
    //日志内容
    private List<String> logs;
}
