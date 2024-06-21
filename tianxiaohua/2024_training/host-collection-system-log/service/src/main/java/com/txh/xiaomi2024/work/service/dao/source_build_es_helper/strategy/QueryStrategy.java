package com.txh.xiaomi2024.work.service.dao.source_build_es_helper.strategy;

import com.txh.xiaomi2024.work.service.dao.source_build_es_helper.SourceBuildContent;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class QueryStrategy implements SourceBuildStrategy {
    @Override
    public void apply(SourceBuildContent content, SearchSourceBuilder sourceBuilder) {
        if (content.getQueryBuilder() != null) {
            sourceBuilder.query(content.getQueryBuilder());
        }
    }
}
