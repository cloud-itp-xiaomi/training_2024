package com.example.mi1.service.impl;

import com.example.mi1.common.utils.BeanMapUtils;
import com.example.mi1.common.utils.IdGeneratorUtils.IdGeneratorUtils;
import com.example.mi1.dao.ESGeneral.ESGeneralDao;
import com.example.mi1.dao.ESGeneral.base.sourceBuilderHelper.CompoundQuery;
import com.example.mi1.dao.ESGeneral.base.sourceBuilderHelper.CompoundType;
import com.example.mi1.dao.constant.ESConst;
import com.example.mi1.domain.po.Log;
import com.example.mi1.domain.vo.LogQueryVO;
import com.example.mi1.service.Storage;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ElasticsearchStorage implements Storage {
    private final ESGeneralDao esGeneralDao;

    @Autowired
    public ElasticsearchStorage(ESGeneralDao esGeneralDao) {
        this.esGeneralDao = esGeneralDao;
    }

    @Override
    public void save(Log logs) {
        try {
            esGeneralDao.putData(logs, ESConst.Index.LOG.value(),String.valueOf(IdGeneratorUtils.standAloneSnowFlake()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LogQueryVO load(String hostname, String file, Integer currentPage, Integer pageSize) {
        List<CompoundQuery> queries = new ArrayList<>();
        if (!hostname.equals("")) {
            queries.add(new CompoundQuery(CompoundType.MUST, QueryBuilders.termQuery("hostname", hostname)));
        }
        if (!file.equals("")) {
            queries.add(new CompoundQuery(CompoundType.MUST, QueryBuilders.termQuery("file", file)));
        }
        List<Map<String, Object>> maps = esGeneralDao.compoundQuery(ESConst.Index.LOG.value(), queries, currentPage - 1 , pageSize);
        maps.sort(Comparator.comparingLong(map -> ((Integer) map.get("timestamp")).longValue()));
        List<String> logs = new ArrayList<>();
        for (Map<String, Object> map : maps){
            logs.add((String) map.get("log"));
        }
        return LogQueryVO.builder().logs(logs).pageNum((int) Math.ceil(((double) maps.size() / pageSize))).totalNum(maps.size()).build();
    }
}
