package com.liren.basic.common.page;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: <h1>Page 分页封装结果集</h1>
 * @author:
 **/
@Data
@ToString
public class Page implements Serializable {

    private static final long serialVersionUID = -2349880735307653615L;

    private Integer rows = 10;  //  每页显示记录数
    private Integer total;      //  总页数
    private Long records;       //  总记录数
    private Integer page = 1;   //  当前页

    public Map pageInfo(int code) {
        Map map = new HashMap<>();
        map.put("code", code);
        return map;
    }

    public Map pageInfo(Integer code, List data) {
        Map map = new HashMap<>();
        PageInfo<List> info = new PageInfo<>(data);
        map.put("records", info.getTotal());
        map.put("total", info.getPages());
        map.put("page", this.page);
        map.put("rows", this.rows);
        map.put("code", code);
        map.put("data", data);
        return map;
    }

    public Map esInfo(int code) {
        Map map = new HashMap<>();
        map.put("code", code);
        return map;
    }

    public Map esInfo(Integer code, List data) {
        Map map = new HashMap<>();
        map.put("records", this.records);
        map.put("total", this.total);
        map.put("page", this.page);
        map.put("rows", this.rows);
        map.put("code", code);
        map.put("data", data);
        return map;
    }

    public Map pageInfo(Integer code, String msg) {
        Map map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    public Map pageInfo(Integer code, String msg, List data) {
        Map map = new HashMap<>();
        PageInfo<List> info = new PageInfo<>(data);
        map.put("records", info.getTotal());
        map.put("total", info.getPages());
        map.put("page", this.page);
        map.put("rows", this.rows);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }
}
