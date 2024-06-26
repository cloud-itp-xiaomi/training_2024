package org.qiaojingjing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "local.storage")
public class LocalFileParentPath {
    private String parentPath;
}
