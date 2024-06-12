package com.xiaomi.work1.mapper;


import com.xiaomi.work1.bean.Metric;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClassName: MetricMapper
 * Package: com.xiaomi.work1.mapper
 * Description:
 *
 * @Author WangYang
 * @Create 2024/5/24 22:14
 * @Version 1.0
 */

public interface MetricMapper {


   public void saveMetrics(Metric metric);

   List<Metric> queryAll(String endpoint, String metric, Long startTs, Long endTs);

}
