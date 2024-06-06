package com.winter.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * es的持久层
 * 第一个泛型为实体的类型，第二个泛型为主键的类型
 * */
@Repository
public interface ESLogDataDao extends ElasticsearchRepository<ESLogData, Long> {

    /**
     * 根据条件检索索引库中的内容
     * --> 根据方法名自动生成查询语句
     * */
    List<ESLogData> findByHostnameAndFile(String hostname, String file);
}
