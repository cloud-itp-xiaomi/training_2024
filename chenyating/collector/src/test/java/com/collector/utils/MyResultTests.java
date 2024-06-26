package com.collector.utils;

import com.collector.bean.request.LogQueryRequest;
import com.collector.bean.request.MetircUploadRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MyResultTests {
    private List<MetircUploadRequest> metircUploadRequests = new ArrayList<>();
    private List<LogQueryRequest> logQueryRequests = new ArrayList<>();

    @Test
    void success() {
        System.out.println(MyResult.success(metircUploadRequests));
        System.out.println(MyResult.success(logQueryRequests));
    }

    @Test
    void failed() {
        System.out.println(MyResult.failed("操作失败！"));
    }
}