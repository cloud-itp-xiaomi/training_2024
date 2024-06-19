package com.cl.server.handler.storage.impl;

import com.cl.server.entity.LogEs;
import com.cl.server.es.LogEsRepository;
import com.cl.server.pojo.DTO.LogInfoDTO;
import com.cl.server.pojo.DTO.LogQueryDTO;
import com.cl.server.pojo.VO.LogInfoVO;
import com.cl.server.enums.StorageTypeEnum;
import com.cl.server.handler.storage.StorageTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Es存储方式
 *
 * @author: tressures
 * @date: 2024/6/13
 */
@Component
@Slf4j
public class EsStorageHandler implements StorageTypeHandler {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private LogEsRepository logEsRepository;

    @Override
    public StorageTypeEnum getHandlerType() { return StorageTypeEnum.ES; }

    @Override
    public void upload(List<LogInfoDTO> logInfoDTOS) {
        List<LogEs> logEsList = new ArrayList<>();
        for(LogInfoDTO logInfoDTO : logInfoDTOS){
            for(String log : logInfoDTO.getLogs()){
                LogEs logEs = new LogEs();
                logEs.setHostname(logInfoDTO.getHostname());
                logEs.setFile(logInfoDTO.getFile());
                logEs.setLog(log);
                logEs.setCreateTime(new Date());
                logEsList.add(logEs);
            }
        }
        logEsRepository.saveAll(logEsList);
    }

    @Override
    public LogInfoVO query(LogQueryDTO logQueryDTO) {
        LogInfoVO logInfoVO = new LogInfoVO();
        logInfoVO.setHostname(logQueryDTO.getHostname());
        logInfoVO.setFile(logQueryDTO.getFile());
        //构建查询参数
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("hostname",logQueryDTO.getHostname()))
                .must(QueryBuilders.termQuery("file",logQueryDTO.getFile()));
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        SearchHits<LogEs> search = elasticsearchRestTemplate.search(nativeSearchQuery,LogEs.class);
        List<SearchHit<LogEs>> searchHits = search.getSearchHits();
        if(CollectionUtils.isEmpty(searchHits)){
            return logInfoVO;
        }
        List<LogEs> logEsList = searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        //按照时间排序
        List<LogEs> reverse = logEsList.stream()
                .sorted(Comparator.comparing(LogEs::getCreateTime))
                .collect(Collectors.toList());
        List<String> logs = reverse.stream()
                .map(LogEs::getLog)
                .collect(Collectors.toList());
        logInfoVO.setLogs(logs);
        return logInfoVO;
    }
}
