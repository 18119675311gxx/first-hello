package com.beizeng.admin.controller;

import com.alibaba.fastjson.JSON;

import com.beizeng.admin.common.sys.annotion.IgnToken;
import com.beizeng.admin.common.utils.es.EsEntity;
import com.beizeng.admin.common.utils.es.EsService;
import com.beizeng.admin.entity.EsUser;
import com.beizeng.admin.entity.dto.EsUserDTO;
import com.liren.basic.common.page.Page;
import com.liren.basic.common.response.JsonReturn;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: <h1>EsController Es 控制层</h1>
 **/
@Slf4j
@RestController
@RequestMapping("/es")
public class EsController {

    private final EsService esService;

    public static final String INDEX_NAME = "esuser-index";

    public EsController(EsService esService) {
        this.esService = esService;
    }

    @IgnToken
    @GetMapping("/t3")
    public JsonReturn t3AddOne() {
        EsUser esUser = new EsUser(1, "张三", "1", 18, "北京", "北京市海淀区丹棱街5号");
        EsEntity<?> entity = new EsEntity<>(String.valueOf(esUser.getUserId()), esUser);
        RestStatus putOne = esService.insertOne(INDEX_NAME, entity);
        System.out.println("【3】t3AddOne：" + putOne);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", putOne);
    }

    @IgnToken
    @GetMapping("/t4")
    public JsonReturn t4AddBatch() {
        List<EsUser> books = new ArrayList<>();
        books.add(new EsUser(2, "张三", "1", 19, "上海", "上海市浦东新区陆家嘴保时捷大厦"));
        books.add(new EsUser(3, "李四", "1", 20, "广州", "广东省广州市海珠区新港中路397号微信总部"));
        books.add(new EsUser(4, "张三", "1", 21, "深圳", "深圳市龙岗区华为基地"));
        books.add(new EsUser(5, "张三", "1", 22, "杭州", "浙江省杭州市余杭区阿里总部"));
        books.add(new EsUser(6, "李四", "1", 23, "青岛", "青岛银川西路67号青岛国际动漫游戏产业园"));
        List<EsEntity> list = new ArrayList<>();
        books.forEach(item -> list.add(
                new EsEntity<>(item.getUserId() + "", item))
        );
        RestStatus putBatch = esService.insertBatch(INDEX_NAME, list);
        System.out.println("【4】t4AddBatch：" + putBatch);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", putBatch);
    }

    @IgnToken
    @GetMapping("/t5")
    public JsonReturn t5deleteById() throws Exception {
        RestStatus deleteById = esService.deleteById(INDEX_NAME, "2");
        System.out.println("【5】t5deleteById：" + deleteById);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", deleteById);
    }

    @IgnToken
    @GetMapping("/t6")
    public JsonReturn t6deleteByKey() {
        long delNo = esService.deleteByKey(INDEX_NAME, "userId", "3");
        System.out.println("【6】t6deleteByKey：" + delNo);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", delNo);
    }

    @IgnToken
    @GetMapping("/t7")
    public JsonReturn t7deleteBatch() {
        List<Integer> ids = new ArrayList<>();
        ids.add(4);
        ids.add(5);
        RestStatus deleteBatch = esService.deleteBatch(INDEX_NAME, ids);
        System.out.println("【7】t7deleteBatch：" + deleteBatch);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", deleteBatch);
    }

    @IgnToken
    @GetMapping("/t8")
    public JsonReturn t8UpdateById() {
        EsUser esUser = new EsUser(7, "张三", "1", 24, "郑州", "河南省郑州市富士康");
        EsEntity<?> entity = new EsEntity<>(String.valueOf(esUser.getUserId()), esUser);
        RestStatus updateOne = esService.UpdateById(INDEX_NAME, entity);
        System.out.println("【8】t8UpdateById：" + updateOne);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", updateOne);
    }

    @IgnToken
    @GetMapping("/t9")
    public JsonReturn t9getById() throws IOException {
        EsUser esUser = esService.getDocById(INDEX_NAME, "1", EsUser.class);
        System.out.println("【9】t9getById：" + esUser);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", esUser);
    }

    @IgnToken
    @GetMapping("/t10")
    public Map t10getByKey(String fieldKey, String fieldVal, Page page) throws IOException {
        Map map = esService.getDocsByKey(INDEX_NAME, fieldKey, fieldVal, page, EsUser.class);
        return map;
    }

    @IgnToken
    @GetMapping("/t11")
    public JsonReturn t11GetDocList() throws IOException {
        System.out.println("【11】t11GetAll");
        List<EsUser> list = esService.getDocList(INDEX_NAME, EsUser.class);
        System.out.println("↓↓↓findAll");
        list.forEach(System.out::println);
        list.forEach(ltem -> System.out.println("ltem = " + ltem.getCity()));
        System.out.println("↑↑↑findAll");
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", list);
    }

    @IgnToken
    @GetMapping("/t13")
    public JsonReturn t13GetAll() throws IOException {
        System.out.println("【14】t14GetAll");
        //  条件查询全部
        List<EsUser> list = esService.getDocALL(INDEX_NAME, "address", "上海青岛", EsUser.class);
        System.out.println("↓↓↓findAll");
        list.forEach(System.out::println);
        System.out.println("↑↑↑findAll" + list.size());

        //  查询全部
        List<EsUser> list1 = esService.getDocALL(INDEX_NAME, EsUser.class);
        list1.forEach(System.out::println);
        System.out.println("↑↑↑findAll" + list1.size());
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", list1);
    }

    @IgnToken
    @GetMapping("/t12")
    public JsonReturn t12GetGather() throws IOException {
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_city")
                .field("city")
                .subAggregation(AggregationBuilders
                        .avg("average_age")
                        .field("age"));
        Aggregations aggregations = esService.aggregationsSearchUser(INDEX_NAME, aggregation);
        Terms byCityAggregation = aggregations.get("by_city");
        List<EsUserDTO> userCityList = new ArrayList<>();
        for (Terms.Bucket buck : byCityAggregation.getBuckets()) {
            EsUserDTO userCityDTO = new EsUserDTO();
            userCityDTO.setCity(buck.getKeyAsString());
            userCityDTO.setCount(buck.getDocCount());
            // 获取子聚合
            Avg averageBalance = buck.getAggregations().get("average_age");
            userCityDTO.setAvgAge(averageBalance.getValue());
            userCityList.add(userCityDTO);
        }

        System.out.println("userCityList = " + JSON.toJSONString(userCityList));
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "请求成功", userCityList);
    }

}
