package com.wheel.admin.response.handler;

import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.exception.BaseException;
import com.wheel.admin.exception.RegistryException;
import com.wheel.admin.exception.UserException;
import com.wheel.admin.utils.TraceIdUtils;
import com.wheel.admin.wrapper.ResultWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: 全局异常处理
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 16:33
 */
@ControllerAdvice
@Order(0)
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(){}

    @ExceptionHandler({UserException.class})
    @ResponseBody
    public ResultDto<String> handlerUserException(HttpServletRequest request, UserException e){
        return handle(request,e);
    }

    @ExceptionHandler({RegistryException.class})
    @ResponseBody
    public ResultDto<String> handlerRegistryException(HttpServletRequest request,RegistryException e){
        return handle(request,e);
    }



    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResultDto<String> handlerException(HttpServletRequest request, Exception e) {
        ResultDto<String> result = this.handleExceptionResult(request, e, ResultEnumCode.COMMON_FAIL);
        return result;
    }

    private ResultDto<String> handleExceptionResult(HttpServletRequest request, Exception e, ResultEnumCode commonFail) {
        return this.handleExceptionResult(request, e, commonFail.getCode(), commonFail.getMessage());

    }

    private ResultDto<String> handleExceptionResult(HttpServletRequest request, Exception e, String code, String message) {
        ResultDto fail = ResultWrapper.fail(code, message);
        fail.setUrl(request.getRequestURL().toString());
        fail.setTraceId(TraceIdUtils.getTraceId());
        log.error("Exception occurred",e);
        return fail;
    }

    private ResultDto<String> handle(HttpServletRequest request, BaseException e){
        String errorCode = e.getErrorCode() == null ? ResultEnumCode.COMMON_FAIL.getCode() : e.getErrorCode();
        String errorMsg = e.getErrorMessage() == null ? ResultEnumCode.COMMON_FAIL.getMessage() : e.getErrorMessage();
        ResultDto fail = ResultWrapper.fail(errorCode, errorMsg);
        fail.setUrl(request.getRequestURL().toString());
        fail.setTraceId(TraceIdUtils.getTraceId());
        if(StringUtils.isEmpty(errorCode)){
            log.error("{} occurred",e);
        }else{
            log.info("{} occurred",e);
        }
        return fail;
    }
}
