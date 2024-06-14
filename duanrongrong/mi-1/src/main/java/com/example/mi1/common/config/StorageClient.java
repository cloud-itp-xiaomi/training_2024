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
import org.springframework.context.annotation.Scope;

@Data
@Configuration
@ConfigurationProperties(prefix = "log.storage")
public class StorageClient {
    private String type;
    private final StorageFactory storageFactory;
    private Storage currentStorage;

    @Autowired
    public StorageClient(StorageFactory storageFactory,  @Value("${log.storage.type}") String type){
        this.storageFactory = storageFactory;
        this.currentStorage = storageFactory.getStorage(type); // 初始化存储
    }

    @Bean
    @Scope("prototype")
    public Storage storage() {
        return currentStorage;
    }

    public void setType(String type) {
        this.type = type;
        this.currentStorage = storageFactory.getStorage(type);
    }

    public String getType() {
        return type;
    }
}

