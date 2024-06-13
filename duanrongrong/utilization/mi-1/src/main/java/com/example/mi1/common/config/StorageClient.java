package com.example.mi1.common.config;

import com.example.mi1.service.Storage;
import com.example.mi1.service.StorageFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "log.storage")
public class StorageClient {

    private String type;

    private final StorageFactory storageFactory;

    @Autowired
    public StorageClient(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    @Bean
    public Storage storage() {
        return storageFactory.getStorage(type);
    }
}
