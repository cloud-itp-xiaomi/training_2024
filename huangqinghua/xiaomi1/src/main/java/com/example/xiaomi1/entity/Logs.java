package com.example.xiaomi1.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Logs {
    private String hostname;
    private String file;
    private List<String> logs;

    @Override
    public String toString() {
        return "Logs{" +
                "hostname='" + hostname + '\'' +
                ", file='"+file+'\''+
                ", log='"+logs.toString()+'\''+
                '}';
    }
}
