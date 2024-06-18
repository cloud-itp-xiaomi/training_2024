package com.txh.xiaomi2024.work.service.dao.source_build_es_helper;

import lombok.Getter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import javax.validation.constraints.NotNull;

@Getter
public class SourceBuildContent {
    @NotNull
    private final SourceBuildParaType type; // 指示在 sourceBuilder 中添加的内容类型
    @NotNull
    private final Integer size; // 搜索返回条数，最大 10000 PS 进行分页后应该以分页中设置的 size 为准

    private final QueryBuilder queryBuilder; // 查询构造器
    private final AggregationBuilder aggregationBuilder; // 聚合构造器
    private final SortBuilder<?> sortBuilder; // 排序
    private final PagingQueryInfo pagingQueryInfo; //分页搜索


    public SourceBuildContent(SourceBuildParaType type, QueryBuilder queryBuilder, int size) {
        this.type = type;
        this.size = size;
        this.queryBuilder = queryBuilder;
        this.aggregationBuilder = null;
        this.sortBuilder = null;
        this.pagingQueryInfo = null;
    }

    public SourceBuildContent(SourceBuildParaType type, AggregationBuilder aggregationBuilder, int size) {
        this.type = type;
        this.size = size;
        this.queryBuilder = null;
        this.aggregationBuilder = aggregationBuilder;
        this.sortBuilder = null;
        this.pagingQueryInfo = null;
    }

    public SourceBuildContent(SourceBuildParaType type, SortBuilder<?> sortBuilder, int size) {
        this.type = type;
        this.size = size;
        this.queryBuilder = null;
        this.aggregationBuilder = null;
        this.sortBuilder = sortBuilder;
        this.pagingQueryInfo = null;
    }

    public SourceBuildContent(SourceBuildParaType type, PagingQueryInfo pagingQueryInfo) {
        this.type = type;
        this.size = Integer.MAX_VALUE;
        this.queryBuilder = null;
        this.aggregationBuilder = null;
        this.sortBuilder = null;
        this.pagingQueryInfo = pagingQueryInfo;
    }
}
