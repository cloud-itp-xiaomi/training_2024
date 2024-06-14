package com.example.mi1.dao.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ESConfig {
    private String ip;
    private Integer port;

    @Bean(name = "my_es_client")
    public RestHighLevelClient resHighLevelClient() {
        if (ip == null || port == null) {
            throw new IllegalArgumentException("Elasticsearch IP and Port must not be null");
        }
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(ip, port, "http")
                )
        );
    }
}
