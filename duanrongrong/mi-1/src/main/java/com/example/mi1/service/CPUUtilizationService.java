package com.example.mi1.service;

import com.example.mi1.domain.UploadParam;
import com.example.mi1.domain.vo.QueryVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CPUUtilizationService {
    List<QueryVO> getUtilization(String endpoint, String metric, Long startTs, Long endTs, int currentPage); // 返回CPU使用率

    void setCPUUtilization(UploadParam uploadParam);

    void setMemUtilization(UploadParam uploadParam);

    List<String> getEndpoints(); // 返回所有可用的endpoint

    List<String> getFile(); // 返回所有可用的file

    void checkEndpoint(String endpoint);

    void checkFile(String file);
}
