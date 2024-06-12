package com.example.mi1.dao.ESGeneral;

import com.example.mi1.dao.ESGeneral.base.sourceBuilderHelper.CompoundQuery;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.geometry.Geometry;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ESGeneralDao {

    void createIndex(String index) throws IOException;

    void createIndex(String index, String mapping) throws IOException;

    void deleteIndex(String index) throws IOException;

    boolean existIndex(String index) throws IOException;

    void putData(Object metadata, String index, String id) throws IOException;

    void putData(String index, List<Map<String, Object>> metadataList) throws IOException;

    void deleteById(String index, String id) throws IOException;

    void deleteById(String index, List<String> ids) throws IOException;

    void deleteByQuery(String index, String fieldName, Object fieldValue) throws IOException;

    boolean updateData(String index, Object metadata, Object id);

    void updateData(String index, List<Map<String, Object>> metadataList) throws IOException;

    boolean existsById(String index, String id) throws IOException;

    Map<String, Object> getDataById(String index, String id);

    List<Map<String, Object>> matchAll(String index);

    List<Map<String, Object>> sortSearchByField(String index, String fieldName, SortOrder orderType, int size);

    List<Map<String, Object>> matchByField(String index, String fieldName, Object targetObj, int size);

    List<Map<String, Object>> matchByField(String index, String[] fieldNames, Object targetObj, int size);

    List<Map<String, Object>> termByField(String index, String fieldName, Object targetObj, int size);

    List<Map<String, Object>> termsByField(String index, String fieldName, Object[] targetObj, int size);

    Map<String, Object> groupByField(String index, String fieldName, int size);

    long count(String index, String fieldName, String fieldValue);

    void distinct(String index, String fieldName, String fieldValue);

    List<Map<String, Object>> compoundQuery(String index, List<CompoundQuery> queries, Integer currentPage, Integer size);

    List<Map<String, Object>> rangeQuery(String index, String fieldName, Object left, Object right, String format, SortBuilder<?> sortBuilder, int size);

    List<Map<String, Object>> geoShapeQuery(String index, String fieldName, Geometry shape, ShapeRelation relation, Integer currentPage, Integer size);

}
