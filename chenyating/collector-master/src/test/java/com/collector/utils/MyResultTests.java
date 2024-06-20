package com.collector.utils;

import com.collector.bean.request.CollectorLogQueryRequest;
import com.collector.bean.request.CollectorUploadRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyResultTests {
    private List<CollectorUploadRequest> collectorUploadRequests = new ArrayList<>();
    private List<CollectorLogQueryRequest> collectorLogQueryRequests = new ArrayList<>();

    @Test
    void success() {
        System.out.println(MyResult.success(collectorUploadRequests));
        System.out.println(MyResult.success(collectorLogQueryRequests));
    }

    @Test
    void failed() {
        System.out.println(MyResult.failed("操作失败！"));
    }
}