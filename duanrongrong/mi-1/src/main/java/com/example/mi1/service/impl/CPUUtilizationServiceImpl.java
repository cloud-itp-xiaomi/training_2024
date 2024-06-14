package com.example.mi1.service.impl;

import com.example.mi1.common.api.CommonPage;
import com.example.mi1.domain.UploadParam;
import com.example.mi1.domain.vo.QueryVO;
import com.example.mi1.domain.vo.QueryValue;
import com.example.mi1.mapper.UtilizationMapper;
import com.example.mi1.service.CPUUtilizationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class CPUUtilizationServiceImpl implements CPUUtilizationService {
    private final UtilizationMapper utilizationMapper;
    private final SaveNewestDataServiceImpl saveNewestDataServiceImpl;

    private static final String CPU_USED_PERCENT = "cpu.used.percent";
    private static final String MEM_USED_PERCENT = "mem.used.percent";
    private static final Integer PAGE_SIZE = 10;

    private final ReentrantLock lock;

    @Autowired
    CPUUtilizationServiceImpl(UtilizationMapper utilizationMapper, SaveNewestDataServiceImpl saveNewestDataServiceImpl) {
        this.saveNewestDataServiceImpl = saveNewestDataServiceImpl;
        this.utilizationMapper = utilizationMapper;
        lock = new ReentrantLock();
    }

    private List<QueryValue> fetchDataFromRedisOrDatabase(String redisKey, String endpoint, Long startTs, Long endTs) {
        List<UploadParam> redisRes = saveNewestDataServiceImpl.rangeQuery(redisKey, endpoint, startTs, endTs);
        if (redisRes != null) {
            log.info("Fetch data from redis");
            return convertToQueryValues(redisRes);
        } else {
            log.info("Fetch data from database");
            if (redisKey.equals("cpu.used.percent")) {
                return convertToQueryValues(utilizationMapper.getCPUUtilization(endpoint, startTs, endTs));
            } else {
                return convertToQueryValues(utilizationMapper.getMemUtilization(endpoint, startTs, endTs));
            }
        }
    }

    private List<QueryValue> convertToQueryValues(List<UploadParam> uploadParams) {
        List<QueryValue> values = new ArrayList<>();
        for (UploadParam param : uploadParams) {
            values.add(new QueryValue(param.getTimestamp(), param.getValue()));
        }
        return values;
    }

    private void addQueryVOToList(List<QueryValue> values, String metric, List<QueryVO> res, int currentPage) {
        QueryVO q = new QueryVO();
        q.setMetric(metric);
        q.setPageNum((int) Math.ceil(((double) values.size() / PAGE_SIZE)));
        q.setTotalNum(values.size());
        int fromIndex = Math.min(Math.max(values.size() - PAGE_SIZE, 0), (currentPage - 1) * PAGE_SIZE);//防止出现 fromIndex 大于 doc 的 size 这种情况返回最后一页
        int toIndex = Math.min(values.size(), fromIndex + PAGE_SIZE);
        q.setValues(values.subList(fromIndex, toIndex));
        res.add(q);
    }

    @Override
    public List<QueryVO> getUtilization(String endpoint, String metric, Long startTs, Long endTs, int currentPage) {
        List<QueryVO> res = new ArrayList<>();
        if (metric.equals(CPU_USED_PERCENT)) {
            List<QueryValue> values = fetchDataFromRedisOrDatabase(CPU_USED_PERCENT, endpoint, startTs, endTs);
            addQueryVOToList(values, CPU_USED_PERCENT, res, currentPage);
        } else if (metric.equals(MEM_USED_PERCENT)) {
            List<QueryValue> values = fetchDataFromRedisOrDatabase(MEM_USED_PERCENT, endpoint, startTs, endTs);
            addQueryVOToList(values, MEM_USED_PERCENT, res, currentPage);
        } else if (metric.equals("")) {
            List<QueryValue> cpuValues = fetchDataFromRedisOrDatabase(CPU_USED_PERCENT, endpoint, startTs, endTs);
            addQueryVOToList(cpuValues, CPU_USED_PERCENT, res, currentPage);

            List<QueryValue> memValues = fetchDataFromRedisOrDatabase(MEM_USED_PERCENT, endpoint, startTs, endTs);
            addQueryVOToList(memValues, MEM_USED_PERCENT, res, currentPage);
        }
        return res;
    }

    public void setCPUUtilization(UploadParam uploadParam) {
        lock.lock();
        try {
            utilizationMapper.insertCPU(uploadParam);
            saveNewestDataServiceImpl.saveData("cpu.used.percent", uploadParam);
        } finally {
            lock.unlock();
        }
    }

    public void setMemUtilization(UploadParam uploadParam) {
        lock.lock();
        try {
            utilizationMapper.insertMem(uploadParam);
            saveNewestDataServiceImpl.saveData("mem.used.percent", uploadParam);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<String> getEndpoints() {
        return utilizationMapper.getEndpoints();
    }

    @Override
    public List<String> getFile() {
        return utilizationMapper.getFiles();
    }

    @Override
    public void checkEndpoint(String endpoint) {
        lock.lock();
        try {
            if (utilizationMapper.getEndpoint(endpoint) == null) {
                utilizationMapper.insertEndpoint(endpoint);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void checkFile(String file) {
        lock.lock();
        try {
            if (utilizationMapper.getFile(file) == null) {
                utilizationMapper.insertFile(file);
            }
        } finally {
            lock.unlock();
        }
    }
}
