package com.liren.basic.common.exception;

import lombok.Data;

/**
 * @description: <h1>RRException 自定义异常类</h1>
 * RRExceptionHandler
 * @author:
 **/
@Data
public class RRException extends RuntimeException {

    private static final long serialVersionUID = -4240745458143306946L;

    private int code;           //  业务状态码
    private String msg;
    private int status = 500;   //  系统状态码

    public RRException(String msg) {
        super(msg);
    }

    public RRException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public RRException(int status, String msg) {
        super(msg);
        this.msg = msg;
        this.status = status;
    }

    public RRException(int code, int status, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
        this.status = status;
    }

    public RRException(int status, String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.status = status;
    }

    public RRException(int code, int status, String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
        this.status = status;
    }

}
