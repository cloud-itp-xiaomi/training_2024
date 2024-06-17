package com.example.mi1.service;

import com.example.mi1.service.impl.ElasticsearchStorage;
import com.example.mi1.service.impl.MySQLStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageFactory {
    private final MySQLStorage mySQLStorage;
    private final ElasticsearchStorage elasticsearchStorage;

    @Autowired
    public StorageFactory(MySQLStorage mySQLStorage, ElasticsearchStorage elasticsearchStorage) {
        this.mySQLStorage = mySQLStorage;
        this.elasticsearchStorage = elasticsearchStorage;
    }

    public Storage getStorage(String storageType) {
        switch (storageType.toLowerCase()) {
            case "mysql":
                return mySQLStorage;
            case "elasticsearch":
                return elasticsearchStorage;
            default:
                throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }
}

