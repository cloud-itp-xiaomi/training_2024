package com.winter.es;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 创建es的实体类
 * */
@Document(indexName = "mi_log")  //mi_log为索引库对应的名称
public class ESLogData {
    @Id  // 标识文档的唯一标识符
    private Long id;

    @Field(type = FieldType.Text)  //定义字段的类型
    private String hostname;

    @Field(type = FieldType.Text)
    private String file;

    @Field(type = FieldType.Text)
    private String logs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "ESLogData{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", file='" + file + '\'' +
                ", logs='" + logs + '\'' +
                '}';
    }
}
