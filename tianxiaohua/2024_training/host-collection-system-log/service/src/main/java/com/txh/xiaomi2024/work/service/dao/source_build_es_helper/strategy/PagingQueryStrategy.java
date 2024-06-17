package com.txh.xiaomi2024.work.service.dao.source_build_es_helper.strategy;

import com.txh.xiaomi2024.work.service.dao.source_build_es_helper.SourceBuildContent;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class PagingQueryStrategy implements SourceBuildStrategy{
    @Override
    public void apply(SourceBuildContent content, SearchSourceBuilder sourceBuilder) {
        sourceBuilder.from(content.getPagingQueryInfo().getFrom());
        sourceBuilder.size(content.getPagingQueryInfo().getSize());
    }
}
