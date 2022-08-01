package com.wheel.admin.security.auth;

import com.alibaba.fastjson.JSON;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.wrapper.ResultWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 没有权限的处理器
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 20:18
 */
@Component
public class WheelAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResultDto noPermission = ResultWrapper.fail(ResultEnumCode.NO_PERMISSION);
        // 设置TraceId
        if(StringUtils.isEmpty(noPermission.getTraceId())){
            noPermission.setTraceId(MDC.get("X-TraceId"));
        }
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        // 把Json数据放到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(noPermission));

    }
}
