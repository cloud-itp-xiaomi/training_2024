package org.example.mysqltest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author txh
 */
@Data //包含get、set和toString方法
@Configuration
@ConfigurationProperties(prefix = "spring.datasource") // 扫描配置文件的数据源配置
public class MysqlConnectConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
