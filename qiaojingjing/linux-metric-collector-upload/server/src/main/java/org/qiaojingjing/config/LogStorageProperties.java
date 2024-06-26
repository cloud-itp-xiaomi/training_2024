package org.qiaojingjing.config;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "storage")
@NacosPropertySource(dataId = "config-log.yaml", autoRefreshed = true)
@RefreshScope
@Component
public class LogStorageProperties {
    private String type;
}