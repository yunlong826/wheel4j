package com.wheel.admin.security.auth;

import com.alibaba.fastjson.JSON;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.exception.UserException;
import com.wheel.admin.wrapper.ResultWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 用户未登录处理
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/30 18:11
 */
@Component
public class WheelAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ResultDto result = ResultWrapper.fail(ResultEnumCode.USER_NOT_LOGIN);
        // 设置TraceId
        if(StringUtils.isEmpty(result.getTraceId())){
            result.setTraceId(MDC.get("X-TraceId"));
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));

    }
}
