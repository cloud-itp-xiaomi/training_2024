package com.collector.cache;

import cn.hutool.json.JSONUtil;
import com.collector.bean.entity.MetircUploadEntity;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class RedisCacheClientTests {
    @Resource
    private CacheClient cacheClient;

    @Test
    @Transactional
    @Rollback(true)
    void saveLatestData() {
        List<MetircUploadEntity> list = new ArrayList<>();
        MetircUploadEntity item = new MetircUploadEntity();
        list.add(item);
        list.forEach(q -> {
                    String value = JSONUtil.toJsonStr(q);
                    if(!StringUtils.isEmpty(value)){
                        cacheClient.saveLatestData("COLLECTOR:KEY", value);
                        System.out.println("success" + ":" + value);
                    }
                }
        );
    }

    @Test
    void getLatestData() {
        List<MetircUploadEntity> list = new ArrayList<>();
        List<Object> latestData = cacheClient.getLatestData("COLLECTOR:KEY");
        latestData.forEach(obj -> {
            MetircUploadEntity bean = JSONUtil.toBean(String.valueOf(obj), MetircUploadEntity.class);
            list.add(bean);
        });

        list.forEach(System.out::println);
    }
}