package com.example.demo01.service;

import com.example.demo01.bean.DataItem;
import com.example.demo01.mapper.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig( cacheNames = "dataitem" )
public class DataService implements IDataService {
    @Autowired
    private DataMapper dataMapper;

    @Override
    //含义：方法的返回值如果不为nulll，则缓存，缓存的key值="all_items"
    @Cacheable( key = "'all_items'", unless="#result == null" )
    public List<DataItem> getAllData(String metric, String endpoint, Integer start_ts, Integer end_ts) {
        List<DataItem> dataList = new ArrayList<>();
        dataList = dataMapper.getAllBy(metric, endpoint, start_ts, end_ts);
        return dataList;
    }

}
