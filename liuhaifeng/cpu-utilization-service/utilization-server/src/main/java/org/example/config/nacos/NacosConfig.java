package org.example.config.nacos;

import org.example.pojo.entity.LogConfigEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * NacosConfig 组装配置中心DataId
 *
 * @author liuhaifeng
 * @date 2024/06/15/18:13
 */
@Configuration
public class NacosConfig {
    @Bean
    public Map<String, Class> nacosConfigLocalCacheInfoMap() {
        // key:dataId, value:对应数据类型
        Map<String, Class> map = new HashMap<>();
        map.put("logConfig", LogConfigEntity.class);
        return map;
    }
}
