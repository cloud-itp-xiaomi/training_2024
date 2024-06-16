package com.server.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.server.entity.Metric;
import com.server.mapper.MetricMapper;


@Service
public class RevicerService {

	@Autowired
	private MetricMapper mMapper;
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;

	@Transactional(rollbackFor = Exception.class)
	public void saveMetric(String res) {
		
		JSONArray jArray = (JSONArray) JSONArray.parse(res);
		
		for (int i = 0;jArray!=null && i < jArray.size(); i++) {
			JSONObject jObj = (JSONObject) jArray.get(i);
			String endpoint = jObj.getString("endpoint");
			String metric = jObj.getString("metric");
			Double value = jObj.getDouble("value");
			Long timestamp = jObj.getLong("timestamp");
			Metric dmetric = new Metric();
			dmetric.setEndpoint(endpoint);
			dmetric.setMetric(metric);
			dmetric.setStep(60);
			dmetric.setValue(value);
			dmetric.setTimestamp(timestamp);
			mMapper.insert(dmetric);
		}
		//
		List<Metric> list = mMapper.getMetric();
		this.saveMetricsToRedisList(list);
	}
	
	public void saveMetricsToRedisList(List<Metric> metrics) {
		String listKey = "metricList";
        redisTemplate.delete(listKey); // 删除旧的列表
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        for (Metric metric : metrics) {
            listOps.rightPush(listKey, metric);
        }
    }

	@Transactional(rollbackFor = Exception.class)
	public List<Metric> getMetric(String endpoint,String metric,Integer start,Integer end) {
		
		QueryWrapper<Metric> wrapper = new QueryWrapper<Metric>();
		//查询某台集器
		wrapper.eq("endpoint", endpoint);
		//不为空才添加本条件
		if(!StringUtils.isEmpty(metric)) {
			wrapper.eq("metric", metric);
		}
//		long lstart = Long.valueOf(start)*1000;
//		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String startTime = f.format(lstart);
//		
//		long lend = Long.valueOf(end)*1000;
//		f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String endTime = f.format(lend);
		//查询特定时间段的数据
		wrapper.le("timestamp",end);
		wrapper.ge("timestamp",start);
		return mMapper.selectList(wrapper);
	}
}
