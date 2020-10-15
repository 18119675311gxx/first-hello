package com.beizeng.admin.common.utils.es;

import com.liren.basic.common.page.Page;
import com.liren.basic.common.response.JsonReturn;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description: <h1>esUtilService ES 调用方法</h1>
 * 别问为啥是 Service，问就是 懒！
 * @author:
 **/
@Component
public class EsService {

    //    ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>> 新增

    /**
     * @param idxName 索引名称
     * @param idxSql  索引描述
     * @return
     * @Description: <h2>创建索引</h2>
     * @author:
     */
    public boolean createIndex(String idxName, String idxSql) {
        return EsUtil.createIndex(idxName, idxSql);
    }

    /**
     * @param idxName index
     * @param entity  对象
     * @return
     * @Description: <h2>新增 数据</h2>
     * @author:
     */
    public RestStatus insertOne(String idxName, EsEntity entity) {
        return EsUtil.insertOrUpdateOne(idxName, entity);
    }

    /**
     * @param idxName index
     * @param data    插入列表
     * @return
     * @Description: <h2>批量插入数据</h2>
     * @author:
     */
    public RestStatus insertBatch(String idxName, List<EsEntity> data) {
        return EsUtil.insertBatch(idxName, data);
    }

    //    ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>> 删除

    /**
     * @param idxName
     * @return
     * @Description: <h2>删除索引</h2>
     * @author:
     */
    public boolean deleteIndex(String idxName) {
        return EsUtil.deleteIndex(idxName);
    }

    /**
     * @param idxName
     * @param id
     * @Description: <h2>根据 id 删除</h2>
     * @return: {@link RestStatus}
     * @author:
     */
    public RestStatus deleteById(String idxName, String id) throws IOException {
        return EsUtil.deleteById(idxName, id);
    }

    /**
     * @param idxName
     * @param fieldKey 匹配在给定字段中含有该词条的文档
     * @param fieldVal
     * @Description: <h2>根据文档内容的 key 进行删除</h2>
     * @return: {@link BulkByScrollResponse}
     * @author:
     */
    public long deleteByKey(String idxName, String fieldKey, String fieldVal) {
        return EsUtil.deleteByQuery(idxName, fieldKey, fieldVal);
    }

    /**
     * @param idxName index
     * @param data    待删除列表
     * @return
     * @Description: <h2>批量删除</h2>
     * @author:
     */
    public RestStatus deleteBatch(String idxName, List<Integer> data) {
        return EsUtil.deleteBatch(idxName, data);
    }

    //    ------------------------->>>>>>>>>>>>>>>>>>>>>>>>>> 更新

    /**
     * @param idxName index
     * @param entity  对象
     * @return
     * @Description: <h2>更新 数据</h2>
     * @author:
     */
    public RestStatus UpdateById(String idxName, EsEntity<?> entity) {
        return EsUtil.insertOrUpdateOne(idxName, entity);
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
    public <T> T getDocById(String idxName, String id, Class<T> clazz) throws IOException {
        return EsUtil.getDocById(idxName, id, clazz);
    }

    /**
     * @param idxName
     * @param id
     * @Description: <h2>根据 id 进行查找</h2>
     * @return: {@link Object}
     * @author:
     */
    public Object getDocById(String idxName, String id) throws IOException {
        return EsUtil.getDocById(idxName, id);
    }

    /**
     * @param idxName  index
     * @param fieldKey 检索元素的key
     * @param fieldVal 检索元素的值
     * @param clazz    结果类对象
     * @Description: <h2>条件检索</h2>
     * @return: {@link List}
     * @author:
     */
    public <T> List getDocsByKey(String idxName, boolean real, String fieldKey, String fieldVal, Class<T> clazz) throws IOException {
        return search(idxName, real, fieldKey, fieldVal, clazz);
    }

    public <T> Map getDocsByKey(String idxName, String fieldKey, String fieldVal, Page page, Class<T> clazz) throws IOException {
        return search(idxName, fieldKey, fieldVal, page, clazz);
    }

    public <T> Map getDocsByKey(String idxName, boolean real, String fieldKey, String fieldVal, Page page, Class<T> clazz) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();

        if (page.getRows() * page.getPage() >= 10000) {
            return page.esInfo(JsonReturn.CODE_ERROR);
        }
        EsUtil.builderPage(builder, page);

        EsUtil.builderQuery(builder, real, fieldKey, fieldVal);
        return EsUtil.getSearchResult(EsUtil.builderSearch(idxName, builder), page, clazz);
    }

    /**
     * @param idxName  index
     * @param fieldKey 检索元素的key
     * @param fieldVal 检索元素的值
     * @Description: <h2>条件检索</h2>
     * @return: {@link List}
     * @author:
     */
    public <T> List getDocsByKey(String idxName, String fieldKey, String fieldVal) throws IOException {
        return search(idxName, fieldKey, fieldVal);
    }

    /**
     * @param idxName index
     * @param clazz   结果类对象
     * @Description: <h2>获取 全部 列表</h2>
     * 默认只显示 10条
     * @return: {@link List}
     * @author:
     */
    public <T> List getDocList(String idxName, Class<T> clazz) throws IOException {
        return search(idxName, clazz);
    }

    /**
     * @param idxName index
     * @Description: <h2>获取 全部 列表</h2>
     * @return: {@link List}
     * @author:
     */
    public <T> List getDocList(String idxName) throws IOException {
        return search(idxName);
    }

    /**
     * @param idxName
     * @param fieldKey
     * @param fieldVal
     * @param clazz
     * @Description: <h2>查询全部</h2>
     * @return: {@link List}
     * @author:
     */
    public <T> List getDocALL(String idxName, String fieldKey, String fieldVal, Class<T> clazz) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        EsUtil.builderQuery(builder, fieldKey, fieldVal);
        //  构建全量查询
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchResponse response = EsUtil.builderSearch(idxName, builder, scroll);
        return EsUtil.getSearchResult(response, scroll, clazz);
    }

    public <T> List getDocALL(String idxName, Class<T> clazz) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        EsUtil.builderQuery(builder);
        //  构建全量查询
        Scroll scroll = EsUtil.builderScroll();
        SearchResponse response = EsUtil.builderSearch(idxName, builder, scroll);
        return EsUtil.getSearchResult(response, scroll, clazz);
    }

    /**
     * @param idxName
     * @param aggregation
     * @Description: <h2>条件聚合</h2>
     * @return: {@link Aggregations}
     * @author:
     */
    public Aggregations aggregationsSearchUser(String idxName, TermsAggregationBuilder aggregation) throws IOException {
        return EsUtil.aggregationsSearchUser(idxName, aggregation);
    }

//    -----------------------------------------------------------

    /**
     * @param idxName  index
     * @param fieldKey 检索元素的key
     * @param fieldVal 检索元素的值
     * @param clazz    结果类对象
     * @Description: <h2>检索</h2>
     * @return: {@link List}
     * @author:
     */
    public <T> List search(String idxName, String fieldKey, String fieldVal, Class<T> clazz) throws IOException {
        return search(idxName, false, fieldKey, fieldVal, clazz);
    }

    public <T> List search(String idxName, boolean real, String fieldKey, String fieldVal, Class<T> clazz) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        EsUtil.builderQuery(builder, real, fieldKey, fieldVal);
        return EsUtil.getSearchResult(EsUtil.builderSearch(idxName, builder), clazz);
    }

    public List search(String idxName, String fieldKey, String fieldVal) throws IOException {
        return search(idxName, fieldKey, fieldVal, null);
    }

    public <T> List search(String idxName, Class<T> clazz) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        EsUtil.builderQuery(builder);
        return EsUtil.getSearchResult(EsUtil.builderSearch(idxName, builder), clazz);
    }

    public List search(String idxName) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        EsUtil.builderQuery(builder);
        return EsUtil.getSearchResult(EsUtil.builderSearch(idxName, builder));
    }

    /**
     * @Description: <h2>构建分页搜索请求</h2>
     * @return: {@link Page}
     * @author:
     */
    public <T> Map search(String idxName, String fieldKey, String fieldVal, Page page, Class<T> clazz) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (page.getRows() * page.getPage() >= 10000) {
            return page.esInfo(JsonReturn.CODE_ERROR);
        }
        EsUtil.builderPage(builder, page);
        EsUtil.builderQuery(builder, fieldKey, fieldVal);
        return EsUtil.getSearchResult(EsUtil.builderSearch(idxName, builder), page, clazz);
    }

    public <T> Map search(String idxName, Page page) throws IOException {
        return search(idxName, page, null);
    }

    public <T> Map search(String idxName, Page page, Class<T> clazz) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (page.getRows() * page.getPage() >= 10000) {
            return page.esInfo(JsonReturn.CODE_ERROR);
        }
        EsUtil.builderPage(builder, page);
        EsUtil.builderQuery(builder);
        return EsUtil.getSearchResult(EsUtil.builderSearch(idxName, builder), page, clazz);
    }

}
