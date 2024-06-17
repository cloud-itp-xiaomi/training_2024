package com.txh.xiaomi2024.work.service.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ESConfig {
    private String ip;
    private Integer port;

    @Bean(name = "my_es_client")
    public RestHighLevelClient resHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        // 在这里配置你的elasticsearch的情况
                        new HttpHost(ip, port, "http")
                )
        );
    }
}
