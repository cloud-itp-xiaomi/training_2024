package com.lx.server.service;

import com.lx.server.common.StatusCode;
import com.lx.server.dao.RedisDao;
import com.lx.server.mapper.UtilizationMapper;
import com.lx.server.pojo.ResUtilization;
import com.lx.server.pojo.Result;
import com.lx.server.pojo.Utilization;
import com.lx.server.utils.GetBeanUtil;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UtilizationService {

    private UtilizationMapper utilizationMapper = GetBeanUtil.getBean(UtilizationMapper.class);
    private RedisDao redisDao = GetBeanUtil.getBean(RedisDao.class);

    //向数据库中插入数据
    public Result add(Utilization utilization) {
        if(utilization == null ) {
            return new Result(StatusCode.PARAM_EMPTY.getCode(), StatusCode.PARAM_EMPTY.getMsg() , null);
        }
        try {
            utilizationMapper.insert(utilization);
            return new Result(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg(), null);
        }catch(Exception e) {
            return new Result(StatusCode.FAIL.getCode() , e.getMessage() , null);
        }
    }

    /*
    根据指标查询数据
    首先查询redis，不存在则查询数据库
    只有在数据库中被查询过的数据才会被添加至redis
    redis中只存储近十条数据
     */
    public Result queryByMetric(String endpoint, String metric, Long start_ts, Long end_ts){

        List<Utilization> utilizations ;

        //参数不合法直接返回
        if((!metric.equals("cpu.used.percent")) && (!metric.equals("mem.used.percent")) || start_ts > end_ts) {
            return new Result(StatusCode.PARAM_NOT_VALID.getCode(), StatusCode.PARAM_NOT_VALID.getMsg(), null);
        }

        //先在redis中查询
        utilizations = redisDao.queryByMetricRedis( endpoint, metric,  start_ts,  end_ts);

        //查询数据库
        try {
            if (utilizations.size() == 0) {
                utilizations = utilizationMapper.queryByMetric(endpoint, metric, start_ts, end_ts);
            }
        }catch(Exception e){
            e.printStackTrace();
            return new Result(StatusCode.FAIL.getCode(), StatusCode.FAIL.getMsg() , null);
        }

        //添加至redis中
        for(Utilization utilization : utilizations){
            redisDao.addRedis(utilization);
        }
        ResUtilization[] data = getDataByMetric(utilizations, metric);
        return new Result(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg(), data);
    }

    /*
    查询全部指标数据
    首先查询redis，不存在则查询数据库
    只有在数据库中被查询过的数据才会被添加至redis
    redis中只存储近十条数据
     */
    public Result query(String endpoint,  Long start_ts, Long end_ts) {

        List<Utilization> utilizations ;
        if(start_ts > end_ts) {
            return new Result(StatusCode.PARAM_NOT_VALID.getCode(), StatusCode.PARAM_NOT_VALID.getMsg(), null);
        }

        //先在redis中查询
        utilizations = redisDao.queryByRedis( endpoint ,  start_ts ,  end_ts);

        //查询数据库
        try {
            if (utilizations.size() == 0) {
                utilizations = utilizationMapper.query(endpoint,  start_ts, end_ts);
                //添加至redis中
                for(Utilization utilization : utilizations) {
                    redisDao.addRedis(utilization);
                }
            }
        }catch(Exception e) {
            return new Result(StatusCode.FAIL.getCode(), StatusCode.FAIL.getMsg() , null);
        }
        ResUtilization[] data = getData(utilizations);
        return new Result(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg(), data);
    }


    //将指定metric的Utilizaition类型的数组封装成ResUtilization类型数组
    public ResUtilization[] getDataByMetric(List<Utilization> utilizations , String metric) {
        ResUtilization[] data = new ResUtilization[1];
        data[0] = new ResUtilization();
        data[0].setMetric(metric);
        List<Map<String, Object>> value = new ArrayList<>();
        data[0].setValues(value);
        for(Utilization utilization : utilizations) {
            data[0].addValue(utilization);
        }
        return data;
    }

    //将Utilizaition类型的数组封装成ResUtilization类型数组
    public ResUtilization[] getData(List<Utilization> utilizations) {

        ResUtilization[] data = new ResUtilization[2];
        for (int i = 0; i < data.length; i++) {
            data[i] = new ResUtilization();
        }
        data[0].setMetric("cpu.used.percent");
        data[1].setMetric("mem.used.percent");

        List<Map<String, Object>> values0 = new ArrayList<>();
        List<Map<String, Object>> values1 = new ArrayList<>();

        data[0].setValues(values0);
        data[1].setValues(values1);

        for(Utilization utilization : utilizations) {
            if(utilization.getMetric().equals("cpu.used.percent")){
                data[0].addValue(utilization);
            }else if(utilization.getMetric().equals("mem.used.percent")){
                data[1].addValue(utilization);
            }
        }
        return data;
    }
}
