package org.example.utilization;

import org.example.rest.DataSender;
import org.example.rest.RestConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class UtilizationConfig {
    private final RestConfig restConfig;

    public UtilizationConfig(RestConfig restConfig) {
        this.restConfig = restConfig;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CPUUtilizationReader cpuUtilizationReader() throws IOException {
        return new CPUUtilizationReader(restConfig.getCpuPath());
    }

    @Bean
    public MemUtilizationReader memUtilizationReader() throws IOException {
        return new MemUtilizationReader(restConfig.getMemoryPath());
    }

    @Bean
    public HostInfoProvider hostInfoProvider() {
        return new HostInfoProvider();
    }

    @Bean
    public DataSender dataSender(RestTemplate restTemplate) {
        return new DataSender(restTemplate, restConfig);
    }

    @Bean
    public Utilization utilization(CPUUtilizationReader cpuReader, MemUtilizationReader memReader,
                                   HostInfoProvider hostInfoProvider, DataSender dataSender) {
        return new Utilization(cpuReader, memReader, hostInfoProvider, dataSender);
    }
}

