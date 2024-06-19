package com.hw.server.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mrk
 * @create 2024-06-08-21:51
 */
@Data
@Configuration
public class RestClientConfig {

    @Value("${spring.elasticsearch.uris}")
    private String uris;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(HttpHost.create(uris))
        );
    }
}
