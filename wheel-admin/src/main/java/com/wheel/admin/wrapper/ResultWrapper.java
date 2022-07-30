package com.wheel.admin.wrapper;

import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 18:48
 */
public class ResultWrapper<T> {
    public static ResultDto success() {
        return new ResultDto(true);
    }

    public static ResultDto success(ResultEnumCode resultEnum) {
        return new ResultDto(true,resultEnum);
    }

    public static <T> ResultDto<T> success(T data) {
        return new ResultDto(true, data);
    }

    public static <T> ResultDto<T> success(ResultEnumCode resultEnum,T data){
        return new ResultDto<>(true,resultEnum,data);
    }

    public static ResultDto fail() {
        return new ResultDto(false);
    }

    public static ResultDto fail(ResultEnumCode resultEnum) {
        return new ResultDto(false, resultEnum);
    }


}
