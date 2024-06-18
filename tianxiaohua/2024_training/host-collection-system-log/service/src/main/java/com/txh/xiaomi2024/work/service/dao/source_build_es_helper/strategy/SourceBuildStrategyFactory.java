package com.txh.xiaomi2024.work.service.dao.source_build_es_helper.strategy;

import com.txh.xiaomi2024.work.service.dao.source_build_es_helper.SourceBuildParaType;

public class SourceBuildStrategyFactory {
    public static SourceBuildStrategy createStrategy(SourceBuildParaType type) {
        switch (type) {
            case Query: return new QueryStrategy();
            case Agg: return new AggStrategy();
            case Sort: return new SortStrategy();
            case PagingQuery: return new PagingQueryStrategy();
            // 其他类型的策略
            default: return null;
        }
    }
}
