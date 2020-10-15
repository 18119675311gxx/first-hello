package com.beizeng.admin.common.utils.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.liren.basic.common.page.Page;
import com.liren.basic.common.response.JsonReturn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.client.RequestOptions.DEFAULT;

/**
 * @description: <h1>elasticsearchUtil elasticsearch 操作类</h1>
 * 有抽取更好的 Operations、template、jest等方式，考虑到使用水平，遂放弃！
 * 并基于种种的考虑，暂不抽取为接口和抽象类。仅作EsDemo使用
 * @author:
 **/
@Slf4j
@Component
public class EsUtil {

    private static RestHighLevelClient restHighLevelClient;

    public EsUtil(RestHighLevelClient restHighLevelClient) {
        EsUtil.restHighLevelClient = restHighLevelClient;
    }

    /**
     * @param idxName 索引名称
     * @param idxSql  索引描述
     * @Description: <h2>创建索引</h2>
     * @return: {@link boolean}
     * @author:
     */
    public static boolean createIndex(String idxName, String idxSql) {
        try {
            if (!indexExist(idxName)) {
                log.info(" idxName= {} 已经存在,idxSql= {}", idxName, idxSql);
                return false;
            }
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSetting(request);
            request.mapping(idxSql, XContentType.JSON);
//            request.settings() 手工指定Setting
            CreateIndexResponse res = restHighLevelClient.indices().create(request, DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
            log.info("createIndex is SUCCESS!!,idxName= {}, ,idxSql= {}", idxName, idxSql);
            return res.isAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("createIndex is FAIL!!,idxName= {}, ,idxSql= {}", idxName, idxSql);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param idxName index名
     * @return boolean
     * @Description: <h2>断某个index是否存在</h2>
     * @author:
     */
    public static boolean indexExist(String idxName) throws Exception {
        GetIndexRequest request = new GetIndexRequest(idxName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        return restHighLevelClient.indices().exists(request, DEFAULT);
    }

    /**
     * @param idxName index名
     * @return boolean
     * @Description: <h2>断某个index是否存在</h2>
     * @author:
     */
    public static boolean isExistsIndex(String idxName) throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(idxName), DEFAULT);
    }

    /**
     * @param request
     * @Description: <h2>设置分片</h2>
     * @author:
     */
    public static void buildSetting(CreateIndexRequest request) {
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));
    }

    /**
     * @param idxName index
     * @param entity  对象
     * @return
     * @Description: <h2>新增 / 更新 数据</h2>
     * @author:
     */
    public static RestStatus insertOrUpdateOne(String idxName, EsEntity entity) {
        IndexRequest request = new IndexRequest(idxName)
                .id(entity.getId())
                .source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        try {
            RestStatus status = restHighLevelClient.index(request, DEFAULT).status();
            log.info("insertOrUpdateOne -- idxName={}, EsEntity={}, RestStatus={}", idxName, JSON.toJSONString(entity), "OK");
            return status;
//            return indexResponse.status().equals(RestStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param idxName 表名称
     * @param data    插入列表
     * @return
     * @Description: <h2>批量插入数据</h2>
     * @author:
     */
    public static RestStatus insertBatch(String idxName, List<EsEntity> data) {
        BulkRequest request = new BulkRequest();
        data.forEach(item -> request.add(new IndexRequest(idxName).id(item.getId())
                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));
        try {
            return restHighLevelClient.bulk(request, DEFAULT).status();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param idxName
     * @return
     * @Description: <h2>删除索引</h2>
     * @author:
     */
    public static boolean deleteIndex(String idxName) {
        try {
            if (!indexExist(idxName)) {
                log.error(" idxName={} 已经存在", idxName);
                return false;
            }
            AcknowledgedResponse result = restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), DEFAULT);
            log.info("deleteIndex -- idxName={}, result={}", idxName, JSON.toJSONString(result.isAcknowledged()));
            return result.isAcknowledged();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param idxName
     * @param id
     * @Description: <h2>根据 id 删除</h2>
     * @return: {@link RestStatus}
     * @author:
     */
    public static RestStatus deleteById(String idxName, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(idxName, id);
        RestStatus status = restHighLevelClient.delete(deleteRequest, DEFAULT).status();
        log.info("deleteById -- idxName={}, id= {}, status={}", idxName, id, "OK");
        return status;
    }

    /**
     * @param idxName
     * @param fieldKey
     * @param fieldVal
     * @Description: <h2>根据条件 删除文档</h2>
     * @return: {@link long}
     * @author:
     */
    public static long deleteByQuery(String idxName, String fieldKey, String fieldVal) {

        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(new TermQueryBuilder(fieldKey, fieldVal));
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            long total = restHighLevelClient.deleteByQuery(request, DEFAULT).getTotal();
            //  fieldName key
            log.info("deleteByQuery -- idxName={}, fieldKey= {}, fieldVal= {}, status= {}", idxName, fieldKey, fieldVal, total);
            return total;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param idxName index
     * @param idList  待删除列表
     * @return
     * @Description: <h2>批量删除</h2>
     * @author:
     */
    public static <T> RestStatus deleteBatch(String idxName, Collection<?> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(idxName, item.toString())));
        try {
            return restHighLevelClient.bulk(request, DEFAULT).status();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>> 查找

    /**
     * @param idxName
     * @param id
     * @param clazz
     * @Description: <h2>根据 id 进行查找</h2>
     * @return: {@link T}
     * @author:
     */
    public static <T> T getDocById(String idxName, String id, Class<T> clazz) throws IOException {
        GetResponse response = getResponse(idxName, id);
        if (response.isExists()) {
            log.info("getDocById -- idxName={}, id= {}, status={}", idxName, id, "OK");
            return JSONObject.parseObject(response.getSourceAsString(), clazz);
        } else {
            log.error("没有找到该 id 的文档");
        }
        return null;
    }

    /**
     * @Description: <h2>根据 id 进行查找</h2>
     * @return: {@link Object}
     * @author:
     */
    public static Object getDocById(String idxName, String id) throws IOException {
        GetResponse response = getResponse(idxName, id);
        if (response.isExists()) {
            log.info("getDocById -- idxName={}, id= {}, status={}", idxName, id, "OK");
            return JSONObject.parseObject(response.getSourceAsString());
        } else {
            log.error("没有找到该 id 的文档");
        }
        return null;
    }

    private static GetResponse getResponse(String idxName, String id) throws IOException {
        GetRequest getRequest = new GetRequest(idxName, id);
        return restHighLevelClient.get(getRequest, DEFAULT);
    }

    /**
     * @param idxName
     * @param aggregation
     * @Description: <h2>条件聚合</h2>
     * @return: {@link Aggregations}
     * @author:
     */
    public static Aggregations aggregationsSearchUser(String idxName, TermsAggregationBuilder aggregation) throws IOException {
        SearchRequest request = new SearchRequest(idxName);
        SearchSourceBuilder builder = new SearchSourceBuilder().aggregation(aggregation);
        request.source(builder);
        SearchResponse searchResponse = restHighLevelClient.search(request, DEFAULT);
        return searchResponse.getAggregations();
    }

    //    -------------------------------------------------------------------------

    /**
     * 前方高能预警，非战斗人员 请迅速撤离！！！
     * 开始构建 DSL 搜索。
     */

    /**
     * @param builder
     * @param real     true 分词；false 不分词；默认为 true
     * @param fieldKey
     * @param fieldVal
     * @Description: <h2>追加查询条件，构造查询引擎</h2>
     * @author:
     */
    public static void builderQuery(SearchSourceBuilder builder, boolean real, String fieldKey, String fieldVal) {
        if (StringUtils.isBlank(fieldKey) || StringUtils.isBlank(fieldVal)) {
            builderQuery(builder);
            return;
        }
        if (real) {
            //  会分词处理
            builder.query(QueryBuilders.matchQuery(fieldKey, fieldVal));
        } else {
            builder.query(QueryBuilders.termsQuery(fieldKey, fieldVal));
        }
    }

    public static void builderQuery(SearchSourceBuilder builder, String fieldKey, String fieldVal) {
        builderQuery(builder, true, fieldKey, fieldVal);
    }

    public static void builderQuery(SearchSourceBuilder builder) {
        builder.query(QueryBuilders.matchAllQuery());
    }

    /**
     * @param builder
     * @param includes 包含的字段   AA,BB,CC
     * @param excludes 不包含的字段
     * @Description: <h2>字段过滤器</h2>
     * eg: AA,BB,CC,DD,EE,FF  -> 结果：AA,BB,CC
     * @return: {@link SearchSourceBuilder}
     * @author:
     */
    public static SearchSourceBuilder builderfetch(SearchSourceBuilder builder, @Nullable String[] includes, @Nullable String[] excludes) {
        builder.fetchSource(includes, excludes);
        return builder;
    }

    /**
     * @param builder
     * @param orderKey  排序字段
     * @param orderRule 排序方式，仅限：ASC,DESC(不区分大小写)
     * @Description: <h2>设置排序</h2>
     * @return: {@link SearchSourceBuilder}
     * @author:
     */
    public static SearchSourceBuilder builderSort(SearchSourceBuilder builder, String orderKey, String orderRule) {
        builder.sort(orderKey, SortOrder.fromString(orderRule));
        return builder;
    }

    /**
     * @param builder
     * @param lineTag  高亮标签：<tag></tag>、<h1></h1>
     * @param fieldKey 高亮的字段
     * @Description: <h2>设置高亮</h2>
     * @return: {@link SearchSourceBuilder}
     * @author:
     */
    public static SearchSourceBuilder builderHighlight(SearchSourceBuilder builder, String lineTag, String fieldKey) {
        HighlightBuilder highlight = new HighlightBuilder();
        highlight.preTags("<" + lineTag + ">");
        highlight.postTags("</" + lineTag + ">");
        highlight.fields().add(new HighlightBuilder.Field(fieldKey));
        builder.highlighter(highlight);
        return builder;
    }

    /**
     * @Description: <h2>构建游标</h2>
     * @return: {@link Scroll}
     * @author:
     */
    public static Scroll builderScroll() {
        return new Scroll(TimeValue.timeValueMinutes(1L));
    }

    /**
     * @param builder
     * @param page
     * @Description: <h2>设置分页参数规则</h2>
     * @author:
     */
    public static void builderPage(SearchSourceBuilder builder, Page page) {
        int from = (page.getPage() - 1) * page.getRows();
        builder.from(from);
        builder.size(page.getRows());
    }

    /**
     * @Description: <h2>构建搜索请求，设置数据源</h2>
     * 发起查询
     * @return: {@link SearchResponse}
     * @author:
     */
    public static SearchResponse builderSearch(String idxName, SearchSourceBuilder builder) throws IOException {
        SearchRequest request = new SearchRequest(idxName);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        request.source(builder);
        return restHighLevelClient.search(request, DEFAULT);
    }

    public static SearchResponse builderSearch(String idxName, SearchSourceBuilder builder, Scroll scroll) throws IOException {
        SearchRequest request = new SearchRequest(idxName);
        builder.size(10000);
        request.scroll(scroll);
        request.source(builder);
        return restHighLevelClient.search(request, DEFAULT);
    }

    /**
     * @param response
     * @param page     是否分页
     * @param clazz    序列化实体
     * @Description: <h2>获取查询结果</h2>
     * @return: {@link Map}
     * @author:
     */
    public static <T> Map getSearchResult(SearchResponse response, Page page, Class<T> clazz) {
        SearchHits searchHits = response.getHits();
        //  分页请求的总数是否一致
        page.setRecords(searchHits.getTotalHits().value);
        page.setTotal(Math.toIntExact((page.getRecords() + page.getRows() - 1) / page.getRows()));
        List list = HitList(searchHits, clazz);
        return page.esInfo(JsonReturn.CODE_SUCCESS, list);
    }

    public static <T> Map getSearchResult(SearchResponse response, Page page) {
        return getSearchResult(response, page, null);
    }

    public static <T> List getSearchResult(SearchResponse response, Class<T> clazz) {
        return HitList(response.getHits(), clazz);
    }

    public static <T> List getSearchResult(SearchResponse response) {
        return HitList(response.getHits(), null);
    }

    public static <T> List getSearchResult(SearchResponse response, Scroll scroll, Class<T> clazz) {
        String scrollId = response.getScrollId();
        List list = getSearchResult(response, clazz);
        SearchHit[] searchHits = response.getHits().getHits();
        while (searchHits != null && searchHits.length > 0) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(scroll);
            try {
                response = restHighLevelClient.scroll(scrollRequest, DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            scrollId = response.getScrollId();
            searchHits = response.getHits().getHits();
            list.addAll(getSearchResult(response, clazz));
        }

        ClearScrollRequest clear = new ClearScrollRequest();
        clear.addScrollId(scrollId);
        try {
            restHighLevelClient.clearScroll(clear, DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param searchHits
     * @param clazz
     * @Description: <h2>处理返回结果集</h2>
     * @return: {@link List}
     * @author:
     */
    public static <T> List HitList(SearchHits searchHits, Class<T> clazz) {
        SearchHit[] hits = searchHits.getHits();
        List list = new ArrayList<>(hits.length);
        if (clazz == null) {
            for (SearchHit hit : hits) {
                list.add(hit.getSourceAsString());
            }
        } else {
            Gson gson = new Gson();
            for (SearchHit hit : hits) {
                list.add(gson.fromJson(hit.getSourceAsString(), clazz));
            }
        }
        return list;
    }

}
