package com.wheel.admin.security.auth;

import com.alibaba.fastjson.JSON;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.wrapper.ResultWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 20:45
 */
@Component
public class WheelLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResultDto result = ResultWrapper.success(ResultEnumCode.SUCCESS_logout);
        // 设置TraceId
        if(StringUtils.isEmpty(result.getTraceId())){
            result.setTraceId(MDC.get("X-TraceId"));
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
