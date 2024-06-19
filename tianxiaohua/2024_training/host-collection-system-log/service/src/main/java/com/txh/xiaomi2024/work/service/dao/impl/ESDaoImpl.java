package com.txh.xiaomi2024.work.service.dao.impl;

import com.alibaba.fastjson2.JSON;
import com.txh.xiaomi2024.work.service.constant.ESConst;
import com.txh.xiaomi2024.work.service.dao.ESDao;
import com.txh.xiaomi2024.work.service.dao.source_build_es_helper.SourceBuildContent;
import com.txh.xiaomi2024.work.service.dao.source_build_es_helper.SourceBuildParaType;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.txh.xiaomi2024.work.service.dao.source_build_es_helper.ESSearchBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Repository
public class ESDaoImpl extends ESSearchBase implements ESDao {
    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public ESDaoImpl(@Qualifier(value = "my_es_client") RestHighLevelClient restHighLevelClient) {
        super(restHighLevelClient);
        this.restHighLevelClient = restHighLevelClient;
    }
    /**
     * 创建索引
     *
     * @param index 索引名
     */
    @Override
    public void createIndex(String index) throws IOException {
        if (existIndex(index)) {
            log.info("索引" + index + "已经存在！");
            return;
        }
        // 创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        // 执行客户端请求
        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        log.info("创建索引" + index + "成功");
    }

    @Override
    public void createIndex(String index,
                            String mapping) throws IOException {
        if (existIndex(index)) {
            log.info("索引" + index + "已经存在！");
            return;
        }
        // 创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        // 设置 mapping
        request.mapping(mapping, XContentType.JSON);
        // 执行客户端请求
        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        log.info("创建索引" + index + "成功");
    }

    /**
     * 删除索引
     *
     * @param index 索引名
     */
    @Override
    public void deleteIndex(String index) throws IOException {
        if (!existIndex(index)) {
            log.info("索引" + index + "不存在!");
            return;
        }

        //删除索引请求
        DeleteIndexRequest request = new DeleteIndexRequest(index);

        //执行客户端请求
        restHighLevelClient.indices()
                .delete(request, RequestOptions.DEFAULT);

        log.info("删除索引" + index + "成功");
    }

    /**
     * 判断索引是否存在
     *
     * @param index 索引名
     */
    @Override
    public boolean existIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return restHighLevelClient.indices()
                .exists(request, RequestOptions.DEFAULT);
    }


    /**
     * 插入一条元数据
     *
     * @param metadata 元数据对象
     * @param index    插入的索引
     * @param id       元数据对象的id
     */
    @Override
    public void putData(Object metadata,
                        String index,
                        String id) throws IOException {
        // 判断元数据是否存在
        if (existsById(index, id)) {
            log.info("索引" + index + "中已存在元数据" + id);
            return;
        }

        // 创建请求
        IndexRequest request = new IndexRequest(index);
        // 设置请求参数
        request.id(id);
        request.timeout("1s");
        request.source(JSON.toJSONString(metadata), XContentType.JSON);
        // 立即更新文档
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        // 客户端发送请求
        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        log.info("添加数据成功, 索引为" + index + ", id为" + indexResponse.getId());
    }

    /**
     * 批量插入元数据
     *
     * @param index        插入的索引
     * @param metadataList 元数据列表，每个列表项是一个Map{"id":XXX, "metadata":Object}
     */
    @Override
    public void putData(String index,
                        List<Map<String, Object>> metadataList) throws IOException {

        //创建批量请求
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, Object> m : metadataList) {
            IndexRequest request = new IndexRequest(index).id((String) m.get("id")).timeout("1s");
            request.source(JSON.toJSONString(m.get("metadata")), XContentType.JSON);
            bulkRequest.add(request);
        }
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        // 客户端发送请求
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        log.info("成功批量插入元数据");
    }


    /**
     * 根据id删除元数据
     *
     * @param index 待删除元数据所在索引
     * @param id    待删除元数据的id
     */
    @Override
    public void deleteById(String index,
                           String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index, id);
        // 立即更新文档
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        DeleteResponse deleteResponse = restHighLevelClient.delete(
                request,
                RequestOptions.DEFAULT);
        log.info("删除数据成功, 索引为" + index + ", id为" + deleteResponse.getId());
    }

    /**
     * 批量删除元数据
     *
     * @param index 待删除元数据所在索引
     * @param ids   元数据id列表
     */
    @Override
    public void deleteById(String index,
                           List<String> ids) throws IOException {
        //创建批量请求
        BulkRequest bulkRequest = new BulkRequest();
        for (String id : ids) {
            DeleteRequest request = new DeleteRequest(
                    index,
                    id);
            bulkRequest.add(request);
        }

        // 客户端发送请求
        BulkResponse bulkResponse = restHighLevelClient.bulk(
                bulkRequest,
                RequestOptions.DEFAULT);

        log.info("成功批量删除元数据");
    }

    /**
     * 条件删除元数据
     *
     * @param index      待查询删除的索引
     * @param fieldName  查询字段
     * @param fieldValue 查询字段的值
     */
    @Override
    public void deleteByQuery(String index,
                              String fieldName,
                              Object fieldValue) throws IOException {
        // 设置条件删除的索引
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        // 设置匹配查询条件
        request.setQuery(new TermQueryBuilder(
                fieldName,
                fieldValue));
        // 刷新索引
        request.setRefresh(true);
        // 执行删除
        BulkByScrollResponse response = restHighLevelClient.deleteByQuery(
                request,
                RequestOptions.DEFAULT);

        log.info("条件删除完成，索引为" + index + "，删除字段：" + fieldName + "，删除字段值" + fieldValue + "，共删除" + response.getStatus().getDeleted() + "条");
    }


    /**
     * 根据id更新元数据
     *
     * @param metadata 元数据对象
     * @param index    待更新元数据所在索引
     * @param id       待更新元数据的id
     */
    @Override
    public boolean updateData(String index,
                              Object metadata,
                              Object id) {
        try {
            // 判断元数据是否存在
            if (!existsById(index, (String) id)) {
                log.info("索引" + index + "中不存在元数据" + id);
            }
            UpdateRequest updateRequest = new UpdateRequest(
                    index,
                    (String) id);
            updateRequest.timeout("1s");
            updateRequest.doc(
                    JSON.toJSONString(metadata),
                    XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(
                    updateRequest,
                    RequestOptions.DEFAULT);
            log.info("更新数据成功, 索引为" + index + ", id为" + updateResponse.getId());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 批量更新元数据
     *
     * @param index        更新元数据的索引
     * @param metadataList 元数据列表，每个列表项是一个Map{"id":XXX, "metadata":Object}
     */
    @Override
    public void updateData(String index,
                           List<Map<String, Object>> metadataList) throws IOException {

        //创建批量请求
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, Object> m : metadataList) {
            UpdateRequest request = new UpdateRequest(
                    index,
                    (String) m.get("id"))
                    .timeout("1s");
            request.doc(
                    JSON.toJSONString(m.get("metadata")),
                    XContentType.JSON);
            bulkRequest.add(request);
        }

        // 客户端发送请求
        BulkResponse bulkResponse = restHighLevelClient.bulk(
                bulkRequest,
                RequestOptions.DEFAULT);

        log.info("成功批量更新元数据");
    }

    /**
     * 通过ID判断元数据是否存在
     *
     * @param index 索引
     * @param id    数据ID
     */
    @Override
    public boolean existsById(String index,
                              String id) throws IOException {
        GetRequest request = new GetRequest(
                index,
                id);
        // 不获取返回的_source的上下文
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        return restHighLevelClient.exists(
                request,
                RequestOptions.DEFAULT);
    }

    /**
     * 根据ID获取元数据信息
     *
     * @param index 元数据所在索引
     * @param id    元数据id
     * @return Map 以Map的形式返回元数据，map的键为元数据的属性名，值为元数据的值
     */
    @Override
    public Map<String, Object> getDataById(String index,
                                           String id) {
        // 查询索引是否存在
        try {
            if (!existIndex(index)) {
                log.info("索引" + index + "不存在!");
                return null;
            }
            GetRequest request = new GetRequest(
                    index,
                    id);
            GetResponse response = restHighLevelClient.get(
                    request,
                    RequestOptions.DEFAULT);
            Map<String, Object> map = response.getSource();
            log.info("根据id查询数据成功, 索引为" + index + ", id为" + response.getId());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回 index 中所有数据
     *
     * @param index 索引名
     * @return 所有数据
     */
    @Override
    public List<Map<String, Object>> matchAll(String index) {
        List<Map<String, Object>> list = new ArrayList<>();
        SourceBuildContent queryContent = new SourceBuildContent(
                SourceBuildParaType.Query,
                QueryBuilders.matchAllQuery(),
                ESConst.ES_MAX_RESULT_WINDOW);
        SearchResponse search = basicSearch(
                index,
                queryContent);
        assert search != null;
        return getSearchResult(search);
    }

    /**
     * 按单一字段进行排序后的搜索结果
     *
     * @param index     索引名
     * @param fieldName 字段名 PS 对应字段不能进行分词，需要是 keyword 类型
     * @param orderType 类型：ASC 或 DESC 分别为 顺序 和 倒序
     * @param size      返回的搜索结果的 size
     * @return List<Map < String, Object>>
     */
    @Override
    public List<Map<String, Object>> sortSearchByField(String index,
                                                       String fieldName,
                                                       SortOrder orderType,
                                                       int size) {
        FieldSortBuilder fieldSortBuilder = new FieldSortBuilder(fieldName).order(orderType);
        SourceBuildContent content = new SourceBuildContent(
                SourceBuildParaType.Sort,
                fieldSortBuilder,
                size);
        SearchResponse search = basicSearch(
                index,
                content);
        assert search != null;
        return getSearchResult(search);
    }

    /**
     * 单字段模糊匹配：只要 targetStr 在该字段中就成功
     */
    @Override
    public List<Map<String, Object>> matchByField(String index,
                                                  String fieldName,
                                                  Object targetObj,
                                                  int size) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(
                fieldName,
                targetObj);
        SourceBuildContent content = new SourceBuildContent(
                SourceBuildParaType.Query,
                matchQueryBuilder,
                size);
        SearchResponse search = basicSearch(
                index,
                content);
        assert search != null;
        return getSearchResult(search);
    }

    /**
     * 多字段中进行模糊匹配：只要指定的多个字段中存在目标字段就成功
     */
    @Override
    public List<Map<String, Object>> matchByField(String index,
                                                  String[] fieldNames,
                                                  Object targetObj,
                                                  int size) {
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(
                targetObj,
                fieldNames);
        SourceBuildContent content = new SourceBuildContent(
                SourceBuildParaType.Query,
                multiMatchQueryBuilder,
                size);
        SearchResponse search = basicSearch(
                index,
                content);
        assert search != null;
        return getSearchResult(search);
    }

    /**
     * 对应字段中进行精确查找 PS 对应字段不能进行分词，需要是 keyword 类型
     */
    @Override
    public List<Map<String, Object>> termByField(String index,
                                                 String fieldName,
                                                 Object targetObj,
                                                 int size) {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(
                fieldName,
                targetObj);
        SourceBuildContent content = new SourceBuildContent(
                SourceBuildParaType.Query,
                termQueryBuilder,
                size);
        SearchResponse search = basicSearch(
                index,
                content);
        assert search != null;
        return getSearchResult(search);
    }

    /**
     * 单一字段中进行多个搜索词的精确查找
     */
    @Override
    public List<Map<String, Object>> termsByField(String index,
                                                  String fieldName,
                                                  Object[] targetObj,
                                                  int size) {
        TermsQueryBuilder builder = QueryBuilders.termsQuery(
                fieldName,
                targetObj);
        SourceBuildContent content = new SourceBuildContent(
                SourceBuildParaType.Query,
                builder,
                size);
        SearchResponse search = basicSearch(
                index,
                content);
        assert search != null;
        return getSearchResult(search);

    }

    /**
     * 根据字段名进行整个索引的 groupBy 操作
     */
    @Override
    public Map<String, Object> groupByField(String index,
                                            String fieldName,
                                            int size) {
        // 聚合构造器
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders
                .terms(fieldName)       // 取别名
                .field(fieldName)       // 得带 .keyword 不然会报错
                .order(BucketOrder.count(false))
                .size(Integer.MAX_VALUE);

        // 聚合构造器的内容
        SourceBuildContent aggBuilderContent = new SourceBuildContent(
                SourceBuildParaType.Agg,
                aggregationBuilder,
                size);
        // 查询构造器的内容
        SourceBuildContent queryBuilderContent = new SourceBuildContent(
                SourceBuildParaType.Query,
                QueryBuilders.matchAllQuery(),
                size);

        // 进行查询
        SearchResponse search = basicSearch(index,
                aggBuilderContent,
                queryBuilderContent);
        assert search != null;
        Terms terms = search.getAggregations()
                .get(fieldName); //上边AggregationBuilders中 取得别名
        Map<String, Object> result = new HashMap<>();
        for (Terms.Bucket term : terms.getBuckets()) {
            result.put(
                    term.getKeyAsString(),
                    term.getDocCount());
        }
        return result;

    }

    /**
     * count 操作
     */
    @Override
    public long count(String index,
                      String fieldName,
                      String fieldValue) {
        TermsQueryBuilder builder = QueryBuilders.termsQuery(
                fieldName,
                fieldValue);
        SourceBuildContent content = new SourceBuildContent(
                SourceBuildParaType.Query,
                builder,
                ESConst.ES_MAX_RESULT_WINDOW);
        SearchSourceBuilder searchSourceBuilder = sourceBuilderInit(content);
        CountRequest countRequest = new CountRequest(index);
        assert searchSourceBuilder != null;
        countRequest.source(searchSourceBuilder);

        CountResponse countResponse;
        try {
            countResponse = restHighLevelClient.count(
                    countRequest,
                    RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException e) {
            log.error("[EsClientConfig.countDocumentSize][error][fail to count document size,param is {}]", countRequest);
        }
        return 0;
    }

    /**
     * 去重操作
     * TODO
     */
    @Override
    public void distinct(String index, String fieldName, String fieldValue) {

    }
}
