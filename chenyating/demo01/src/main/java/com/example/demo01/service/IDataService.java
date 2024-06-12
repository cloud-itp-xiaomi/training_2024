package com.example.demo01.service;

import com.example.demo01.bean.DataItem;

import java.util.List;

public interface IDataService {
    public List<DataItem> getAllData(String metric, String endpoint, Integer start_ts, Integer end_ts);
}
