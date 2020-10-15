package com.liren.basic.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CodeSatus {

    /** 系统错误类 1~ */

    /**
     * 数据错误类 2~
     */
    DUPLICATEKEY_ERROR_CODE(201, "数据已存在"),
    RESOURCE_NOT_EXIST(202, "资源不存在");

    /**
     * 产品线编码
     */
    private Integer code;
    /**
     * 产品线描述
     */
    private String msg;


    public static CodeSatus of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
