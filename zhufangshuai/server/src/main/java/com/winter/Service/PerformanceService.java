package com.winter.Service;

import com.winter.domain.Performance;
import com.winter.mapper.PerformanceMapper;
import com.winter.req.QueryPerformanceReq;
import com.winter.utils.SnowUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceService {

    @Resource
    private PerformanceMapper performanceMapper;

    /**
     * 查询所有的数据
     * */
    public List<Performance> findAll() throws Exception{
        return performanceMapper.findAll();
    }

    /**
     * 添加数据
     * */
    public void add(Performance performance) throws Exception{
        performance.setId(SnowUtil.getSnowflakeNextIdStr());  //生成主键
        performanceMapper.add(performance);
    }

    /**
     * 条件查询
     * */
    public List<Performance> findByCondition(QueryPerformanceReq req) throws Exception{
        return performanceMapper.findByCondition(req);
    }
}
