package com.beizeng.admin.entity.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @description: <h1>EsUserDTO 城市聚合DTO</h1>
 **/
@Data
@ToString
public class EsUserDTO {
    /**
     * 城市
     */
    private String city;
    /**
     * 用户数
     */
    private Long count;
    /**
     * 平均年龄
     */
    private Double avgAge;

}
