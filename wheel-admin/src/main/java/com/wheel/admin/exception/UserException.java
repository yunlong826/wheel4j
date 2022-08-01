package com.wheel.admin.exception;

/**
 * Description: 关于涉及到用户操作的异常类
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 15:41
 */
public class UserException extends BaseException{
    public UserException(String errorCode, String message, Object... args){
        super(errorCode,message,args);
    }
}
