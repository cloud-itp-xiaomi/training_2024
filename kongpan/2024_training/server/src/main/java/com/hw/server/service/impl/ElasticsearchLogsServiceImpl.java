package com.hw.server.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hw.server.domain.dto.LogsDTO;
import com.hw.server.domain.dto.Result;
import com.hw.server.domain.po.Logs;
import com.hw.server.mapper.LogsMapper;
import com.hw.server.service.ILogsService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hw.server.constants.EsConstants.INDEX_NAME;
import static com.hw.server.constants.EsConstants.MAPPING_TEMPLATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mrk
 * @since 2024-06-08
 */
@Service
@RequiredArgsConstructor
public class ElasticsearchLogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements ILogsService {

    private final RestHighLevelClient restHighLevelClient;

    @Override
    public Result<?> uploadLogs(List<Logs> logs) {
        try {
            if (!indexExists(INDEX_NAME)) {
                createIndex(INDEX_NAME);
            }
            BulkRequest bulkRequest = new BulkRequest(INDEX_NAME);
            for (Logs log : logs) {
                String jsonStr = JSONUtil.toJsonStr(log);
//                IndexRequest indexRequest = new IndexRequest().id(log.getId().toString());
                IndexRequest indexRequest = new IndexRequest();
                indexRequest.source(jsonStr, XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("EsLogs upload failed");
        }
    }

    @Override
    public Result<?> queryLogs(String hostname, String file) {
        // 1. 根据指定条件进行查询
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("hostname", hostname))
                .must(QueryBuilders.matchQuery("file", file))
        );
        searchRequest.source(searchSourceBuilder);

        // 2. 解析响应结果，并封装成指定数据格式
        List<String> logContents = new ArrayList<>();
        LogsDTO logsDTO = new LogsDTO();
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            searchResponse.getHits().forEach(hit -> {
                String logContent = (String) hit.getSourceAsMap().get("log");
                logContents.add(logContent);
            });
            logsDTO.setHostname(hostname);
            logsDTO.setFile(file);
            logsDTO.setLogContents(logContents);
            return Result.ok(logsDTO);
        } catch (IOException e) {
            return Result.error("Data query failed");
        }
    }

    /**
     * 创建索引库
     * @param indexName 索引库名称
     * @throws IOException
     */
    public void createIndex(String indexName) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 判断索引库是否存在
     * @param indexName 索引库名称
     * @return
     * @throws IOException
     */
    public boolean indexExists(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }
}
