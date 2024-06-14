package com.example.demo01.service;

import cn.hutool.json.JSONUtil;
import com.example.demo01.bean.Cpu;
import com.example.demo01.bean.Mem;
import com.example.demo01.bean.MetricVO;
import com.example.demo01.bean.SysServer;
import com.example.demo01.bean.DataItem;
import com.example.demo01.mapper.DataMapper;
import com.example.demo01.utils.IpUtils;
import com.example.demo01.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
//如果类中有任何final或被@NonNull注解标记的字段，Lombok会在类中自动生成一个带参构造函数，参数列表中包含了这些字段
public class CollectService {

    private final DataMapper dataMapper;
    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    //表示任务执行完后，等待10分钟后再次执行，timeUnit = TimeUnit.SECONDS表示时间单位为秒
    public void metricCollect() {
        SysServer sysServer = new SysServer();
        sysServer.copyTo();
        // CPU
        final Cpu cpu = sysServer.getCpu();

        DataItem cpuMetric = new DataItem();
        cpuMetric.setMetricName(Cpu.METRIC_NAME);
        cpuMetric.setCreateId("job");
        cpuMetric.setEndpoint(IpUtils.getHostName());
        cpuMetric.setStep(TimeUnit.MINUTES.toSeconds(10));//把10分钟转为秒，也可以直接固定为60秒
        cpuMetric.setTimestamp(TimeUtils.format(System.currentTimeMillis()));
        cpuMetric.setPercentValue(BigDecimal.valueOf(cpu.getUsed()));

        //放入数据库和redis
        dataMapper.insert(cpuMetric);
        this.syncRedis(Cpu.METRIC_NAME);

        // 内存
        final Mem mem = sysServer.getMem();

        DataItem memMetric = new DataItem();
        memMetric.setMetricName(Mem.METRIC_NAME);
        memMetric.setCreateId("job");
        memMetric.setEndpoint(IpUtils.getHostName());
        memMetric.setStep(TimeUnit.MINUTES.toSeconds(10));
        memMetric.setTimestamp(TimeUtils.format(System.currentTimeMillis()));
        memMetric.setPercentValue(BigDecimal.valueOf(mem.getUsed()));

        dataMapper.insert(memMetric);
        this.syncRedis(Mem.METRIC_NAME);

        log.info("cpu mem collect success");
    }

    public void syncRedis(String metric) {
        // 指标数据同步，根据传入的指标名称metric查询数据库中最新的10条指标数据
        final List<MetricVO> metricVOList = dataMapper.selectByLimit(metric, 10);
        metricVOList.forEach(x -> x.setMetric(metric));
        //使用JSONUtil.toJsonStr(metricVOList)将查询到的指标数据转换为JSON格式的字符串
        //将转换后的JSON字符串保存到Redis缓存中，其中metric作为缓存的键，JSON字符串作为缓存的值
        redisTemplate.opsForValue().set(metric, JSONUtil.toJsonStr(metricVOList));
    }

}
