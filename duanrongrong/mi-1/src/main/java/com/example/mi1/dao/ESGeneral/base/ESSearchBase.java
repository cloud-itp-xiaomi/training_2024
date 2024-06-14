package com.example.mi1.dao.ESGeneral.base;

import com.example.mi1.dao.ESGeneral.base.sourceBuilderHelper.SourceBuilderContent;
import com.example.mi1.dao.ESGeneral.base.sourceBuilderHelper.SourceBuilderParaType;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class ESSearchBase {
    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public ESSearchBase(
            @Qualifier(value = "my_es_client") RestHighLevelClient restHighLevelClient
    ) {
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * 根据 SearchSourceBuilder 获取相应的 SearchResponse 相应结果
     * PS 这里不是直接返回查询的结果而是返回响应
     *
     * @param sourceBuilder 搜索源构造器
     * @param index         索引名
     * @return SearchResponse
     */
    public SearchResponse getSearchResponse(String index, SearchSourceBuilder sourceBuilder) {
        // 建立查询对象
        SearchRequest searchRequest = new SearchRequest(index);//传入的是索引名
        searchRequest.source(sourceBuilder);

        // 执行查询
        try {
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将查询得到的 response 对象的结果返回
     *
     * @param response 相应对象
     * @return List<Map < String, Object>>
     */
    public List<Map<String, Object>> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        // 将结果转成Map列表
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            results.add(hit.getSourceAsMap());
        }
        log.info("查询的数据量为 {} ", searchHit.length);
        return results;
    }

    /**
     * 构建 sourceBuilder
     *
     * @param contents sourceBuilder中添加德内容
     * @return 设置了 query、sort、aggregation、from、size 的 SearchSourceBuilder 对象
     */
    public SearchSourceBuilder sourceBuilderInit(SourceBuilderContent... contents) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        int size = Integer.MAX_VALUE;
        boolean isExistPage = false;
        // 主要解决传入不同的 QueryBuilder、AggregationBuilder、SortBuilder、PageInfoQueryInfo 进行汇总，传入多个 QueryBuilder 则以最后为准
        for (SourceBuilderContent content : contents) {
            if (content.getSize() < size) {
                size = content.getSize(); // 找最小的一个
            }
            if (content.getType().equals(SourceBuilderParaType.Query) && content.getQueryBuilder() != null) {
                sourceBuilder.query(content.getQueryBuilder());
            } else if (content.getType().equals(SourceBuilderParaType.Agg) && content.getAggregationBuilder() != null) {
                sourceBuilder.aggregation(content.getAggregationBuilder());
            } else if (content.getType().equals(SourceBuilderParaType.Sort) && content.getSortBuilder() != null) {
                sourceBuilder.sort(content.getSortBuilder());
            } else if (content.getType().equals(SourceBuilderParaType.PagingQuery) && content.getPagingQueryInfo() != null) {
                isExistPage = true;
                sourceBuilder.from(content.getPagingQueryInfo().getFrom());
                sourceBuilder.size(content.getPagingQueryInfo().getSize());
            } else {
                log.error("缺少 builder 或 SourceBuilder 无法匹配 QueryBuilder 、 AggregationBuilder 、 SortBuilder.");
                return null;
            }
        }
        if (!isExistPage) {
            sourceBuilder.size(size);
        }
        return sourceBuilder;
    }

    /**
     * 基础搜索
     *
     * @param index    索引名
     * @param contents 传入 SourceBuilderContent 类型，初始化 SourceBuilder
     * @return SearchResponse
     */
    public SearchResponse basicSearch(String index, SourceBuilderContent... contents) {
        SearchSourceBuilder sourceBuilder = sourceBuilderInit(contents);
        return getSearchResponse(index, sourceBuilder);
    }
}
