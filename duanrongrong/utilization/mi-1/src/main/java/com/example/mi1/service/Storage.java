package com.example.mi1.service;

import com.example.mi1.domain.po.Log;
import com.example.mi1.domain.vo.LogQueryVO;

import java.util.List;

public interface Storage {
    void save(Log logs);

    LogQueryVO load(String hostname, String file, Integer currentPage, Integer pageSize);
}
