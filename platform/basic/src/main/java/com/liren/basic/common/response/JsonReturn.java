package com.liren.basic.common.response;

import org.apache.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: <h1>JsonReturn 通用响应</h1>
 * @author:
 **/
public class JsonReturn extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -8026163042755843025L;

    public static final int CODE_NO_LOGIN = -99999;     //  用户未登录
    public static final int CODE_SUCCESS = 1;           //  成功
    public static final int CODE_ERROR = 0;             //  失败
    public static final int CODE_BAD = -1;              //  服务器内部错误

    /**
     * 以下为异常调用！！！
     */
    public JsonReturn() {
        put("code", CODE_SUCCESS);      //  业务状态码
        put("status", CODE_SUCCESS);    //  系统状态码
        put("msg", "请求成功！");
    }

    public static JsonReturn ok() {
        return new JsonReturn();
    }

    public static JsonReturn ok(String msg) {
        JsonReturn r = new JsonReturn();
        r.put("msg", msg);
        return r;
    }

    public static JsonReturn ok(Map<String, Object> map) {
        JsonReturn r = new JsonReturn();
        r.putAll(map);
        return r;
    }

    public static JsonReturn error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static JsonReturn error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static JsonReturn error(int status, String msg) {
        JsonReturn r = new JsonReturn();
        r.put("code", CODE_BAD);
        r.put("status", status);
        r.put("msg", msg);
        return r;
    }

    public static JsonReturn error(int code, int status, String msg) {
        JsonReturn r = new JsonReturn();
        r.put("code", code);
        r.put("status", status);
        r.put("msg", msg);
        return r;
    }

    public static JsonReturn send(Integer code, String msg) {
        JsonReturn r = new JsonReturn();
        r.put("msg", msg);
        r.put("code", code);
        r.put("status", code);
        return r;
    }

    public static JsonReturn send(Integer code, Object data) {
        JsonReturn r = new JsonReturn();
        r.put("code", code);
        r.put("data", data);
        r.put("status", code);
        return r;
    }

    public static JsonReturn send(Integer code, String msg, Object data) {
        JsonReturn r = new JsonReturn();
        r.put("msg", msg);
        r.put("code", code);
        r.put("data", data);
        r.put("status", code);
        return r;
    }


    public JsonReturn put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
