package com.example.hostmonitor.utils;

import com.example.hostmonitor.pojo.HostResourceUsageEntity;
import com.example.hostmonitor.pojo.QueryData;
import com.example.hostmonitor.pojo.UploadData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: WangYF
 * @Date: 2024/05/31
 * @Description: 处理转换数据的静态方法
 */
public class DataTransUtils {

    /**
     * @Description: 输入转换为数据库标准Bean
     */
    public static HostResourceUsageEntity dataConvertToEntity(UploadData data){
        HostResourceUsageEntity usageEntity = new HostResourceUsageEntity();
        usageEntity.setMetric(data.getMetric());
        usageEntity.setEndpoint(data.getEndpoint());
        usageEntity.setTimestamp(data.getTimestamp());
        usageEntity.setValue(data.getValue());
        return usageEntity;
    }

    /**
     * @Description: 数据库Bean排序分类转换为输出
     */
    public static List<QueryData> convertToQueryData(List<HostResourceUsageEntity> entities){
        return entities.stream()
                .collect(Collectors.groupingBy(HostResourceUsageEntity::getMetric))
                .entrySet().stream()
                .map(entry -> {
                    String metric = entry.getKey();
                    List<HostResourceUsageEntity> groupedEntities = entry.getValue();
                    List<QueryData.TimeWithValue> timeWithValueList = groupedEntities.stream()
                            .map(entity -> new QueryData.TimeWithValue(entity.getTimestamp(), entity.getValue()))
                            .collect(Collectors.toList());
                    return new QueryData(metric, timeWithValueList);
                })
                .collect(Collectors.toList());
    }

}
