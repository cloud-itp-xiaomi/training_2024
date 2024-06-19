package com.cl.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;

/**
 * LogEs 实体类（启动时自动创建索引以及映射）
 *
 * @author: tressures
 * @date: 2024/6/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "log_index")
public class LogEs {

    /**
     * 主键id
     */
    @Field(type = FieldType.Text)
    @Id
    private String id;
    /**
     * 主机名称
     */
    @Field(type = FieldType.Keyword)
    private String hostname;
    /**
     * 文件路径
     */
    @Field(type = FieldType.Keyword)
    private String file;
    /**
     * 单条日志
     */
    @Field(type = FieldType.Text)
    private String log;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date,index = false)
    private Date createTime;
}
