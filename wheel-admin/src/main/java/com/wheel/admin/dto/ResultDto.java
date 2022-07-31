package com.wheel.admin.dto;

import com.wheel.admin.enums.ResultEnumCode;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 18:42
 */
@Data
public class ResultDto<T> implements Serializable {
    private Boolean success;
    private String errorCode;
    private String errorMsg;
    private T data;
    protected String traceId;
    protected String url;

    public ResultDto() {
    }

    // 成功或者失败都能走这个
    public ResultDto(boolean success) {
        this.success = success;
        this.errorMsg = success ? ResultEnumCode.SUCCESS.getMessage() : ResultEnumCode.COMMON_FAIL.getMessage();
        this.errorCode = success ? ResultEnumCode.SUCCESS.getCode() : ResultEnumCode.COMMON_FAIL.getCode();
    }

    // 成功或者失败都能走这个，并且可以传一个枚举来改变默认枚举的值
    public ResultDto(boolean success, ResultEnumCode resultEnum) {
        this.success = success;
        // 传来的枚举为null就用默认的，不为null就用传来的枚举
        this.errorCode = success ? (resultEnum==null?ResultEnumCode.SUCCESS.getCode():resultEnum.getCode()) : (resultEnum == null ? ResultEnumCode.COMMON_FAIL.getCode() : resultEnum.getCode());
        this.errorMsg = success ? (resultEnum==null?ResultEnumCode.SUCCESS.getMessage():resultEnum.getMessage()): (resultEnum == null ? ResultEnumCode.COMMON_FAIL.getMessage() : resultEnum.getMessage());
    }

    // 成功或者失败都能用
    // 用户可以传一个任意对象过来，用默认的成功或者失败的枚举
    public ResultDto(boolean success, T data) {
        this.success = success;
        this.errorCode = success ? ResultEnumCode.SUCCESS.getCode() : ResultEnumCode.COMMON_FAIL.getCode();
        this.errorMsg = success ? ResultEnumCode.SUCCESS.getMessage() : ResultEnumCode.COMMON_FAIL.getMessage();
        this.data = data;
    }

    // 成功或者失败都能用
    // 用户可以传一个任意对象和自定义枚举过来
    public ResultDto(boolean success, ResultEnumCode resultEnum, T data) {
        this.success = success;
        this.errorCode = success ? (resultEnum==null ? ResultEnumCode.SUCCESS.getCode() : resultEnum.getCode()): (resultEnum == null ? ResultEnumCode.COMMON_FAIL.getCode() : resultEnum.getCode());
        this.errorMsg = success ? (resultEnum==null ? ResultEnumCode.SUCCESS.getMessage() : resultEnum.getMessage()) : (resultEnum == null ? ResultEnumCode.COMMON_FAIL.getMessage() : resultEnum.getMessage());
        this.data = data;
    }
    public ResultDto(boolean success,String errorCode,String errorMsg){
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
