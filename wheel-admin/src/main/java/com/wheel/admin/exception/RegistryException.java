package com.wheel.admin.exception;

/**
 * Description: 注册中心异常类
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 16:03
 */
public class RegistryException extends BaseException{
    public RegistryException(String errorCode, String message, Object... args){
        super(errorCode,message,args);
    }
}
