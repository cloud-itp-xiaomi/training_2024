package com.example.xiaomi1coll.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FileConfig {
    private List<String> files;
    private String log_storage;

    @Override
    public String toString() {
        return "file{" +
                "files='" + files + '\'' +
                ", log_storage='"+log_storage+'\''+
                '}';
    }
}
