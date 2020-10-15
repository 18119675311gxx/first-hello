package com.beizeng.admin.common.basic.exception;

import com.liren.basic.common.exception.RRException;
import com.liren.basic.common.response.CodeSatus;
import com.liren.basic.common.response.JsonReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @description: <h1>RRExceptionHandler 异常处理器</h1>
 * @author:
 **/
@Slf4j
@RestControllerAdvice
public class RRExceptionHandler {

    /**
     * 全局捕捉异常
     */
    @ExceptionHandler(Exception.class)
    public JsonReturn handleException(Exception e) {
        log.error(e.getMessage(), e);
        if (e instanceof ArrayIndexOutOfBoundsException) {
            return JsonReturn.error("越界异常！");
        }
        if (e instanceof ArithmeticException) {
            return JsonReturn.error("运算异常！");
        }
        return JsonReturn.error();
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = RRException.class)
    public JsonReturn handleRRException(RRException e) {
        log.error(e.getMessage(), e);
        return JsonReturn.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public JsonReturn handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return JsonReturn.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonReturn HttpRequestMethodNotSupportedException(Exception e) {
        log.error(e.getMessage(), e);
        return JsonReturn.error(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public JsonReturn handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return JsonReturn.error(CodeSatus.DUPLICATEKEY_ERROR_CODE.getCode(),CodeSatus.DUPLICATEKEY_ERROR_CODE.getMsg());
    }

}
