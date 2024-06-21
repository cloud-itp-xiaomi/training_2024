package com.example.mi1.dao.ESGeneral.base.sourceBuilderHelper;

import lombok.Data;
import org.elasticsearch.index.query.QueryBuilder;

@Data
public class CompoundQuery {
    private CompoundType type;
    private QueryBuilder builder;
    private int minimumShouldMatch;

    public CompoundQuery() {
        this.minimumShouldMatch = Integer.MIN_VALUE;
    }

    public CompoundQuery(CompoundType type, QueryBuilder builder) {
        this.type = type;
        this.builder = builder;
        this.minimumShouldMatch = Integer.MIN_VALUE;
    }

    public CompoundQuery(CompoundType type, QueryBuilder builder, int minimumShouldMatch) {
        this.type = type;
        this.builder = builder;
        this.minimumShouldMatch = minimumShouldMatch;
    }

}
