package com.wheel.admin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * Description: 自定义异常类基类
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 15:33
 */
public class BaseException extends RuntimeException{
    private static final Logger log = LoggerFactory.getLogger(BaseException.class);
    private static final long serialVersionUID = 6528764782020895991L;
    protected String errorCode;
    protected String errorMessage;

    public BaseException() {
    }

    public BaseException(Throwable ex) {
        super(ex);
    }

    public BaseException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public BaseException(String message, Throwable ex) {
        super(message, ex);
        this.errorMessage = message;
    }

    public BaseException(String errorCode, String message, Object... args) {
        super(format(message, args));
        this.errorCode = errorCode;
        this.errorMessage = this.getMessage();
    }

    public BaseException(String errorCode, String message, Throwable ex, Object... args) {
        super(format(message, args), ex);
        this.errorCode = errorCode;
        this.errorMessage = this.getMessage();
    }



    private static String format(String message, Object... args) {
        try {
            return MessageFormat.format(message, args);
        } catch (Exception var3) {
            log.warn("BaseException format exception", var3);
            return message;
        }
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
