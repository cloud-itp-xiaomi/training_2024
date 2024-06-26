package com.example.springcloud.controller.response;

import lombok.Data;

import java.util.List;

/**
 * @ClassName LogQueryResponse
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-23 00:03
 **/
@Data
public class LogQueryResponse {
    private String hostName;
    private String file;
    private  List<String> logs;
}
