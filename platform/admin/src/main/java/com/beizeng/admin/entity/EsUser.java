package com.beizeng.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description: <h1>EsUser es 注解实体类</h1>
 * @author:
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EsUser implements Serializable {

    private static final long serialVersionUID = 4105708570013237939L;

    private Integer userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 城市
     */
    private String city;

    /**
     * 地址
     */
//    @Field(type = FieldType.Text, analyzer = EsConst.IK_SMART, searchAnalyzer = EsConst.IK_SMART)
    private String address;

}
