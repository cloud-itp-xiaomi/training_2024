package org.qiaojingjing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "log.storage")
public class LogStorageProperties {
    private String type;
}
