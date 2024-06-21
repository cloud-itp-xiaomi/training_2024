package com.example.springcloud.service;

import com.example.springcloud.controller.base.Response;
import com.example.springcloud.controller.request.ReaderRequest;
import com.example.springcloud.controller.response.ReaderResponse;



public interface ReaderService {
    Response<ReaderResponse> readerMsg(ReaderRequest request);
}
