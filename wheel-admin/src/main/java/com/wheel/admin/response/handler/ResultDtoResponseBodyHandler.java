package com.wheel.admin.response.handler;

import com.wheel.admin.dto.ResultDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * Description: 对response前置增强,注入traceId
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 14:52
 */
@ControllerAdvice
@Order(10)
public class ResultDtoResponseBodyHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (Objects.isNull(returnType)) {
            return false;
        } else if (Objects.isNull(returnType.getMethod())) {
            return false;
        } else {
            Class<?> type = returnType.getMethod().getReturnType();
            if (Objects.isNull(type)) {
                return false;
            } else {
                Boolean flag = ResultDto.class.equals(type);
                return flag;
            }
        }
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 如果response body为ResultDto类型，捕获，加入trcaeId字段
        if(ResultDto.class.isInstance(body)){
            ResultDto resultDto = ResultDto.class.cast(body);
            if(StringUtils.isEmpty(resultDto.getTraceId())){
                resultDto.setTraceId(MDC.get("X-TraceId"));
            }
            return resultDto;
        }else{
            return body;
        }
    }
}
