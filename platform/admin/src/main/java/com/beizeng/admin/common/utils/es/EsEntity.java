package com.beizeng.admin.common.utils.es;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * @description: <h1>ElasticEntity es 文档实体</h1>
 **/
@Data
public class EsEntity<T> {

    /**
     * 主键标识，用户ES持久化，必须 显式的指定 id。
     */
    private String id;

    /**
     * JSON对象，实际存储数据
     */
    private T data;

    public EsEntity(String id, T data) {
        this.id = (StringUtils.isBlank(id) ? UUID.randomUUID().toString() : id);
        this.data = data;
    }
}
