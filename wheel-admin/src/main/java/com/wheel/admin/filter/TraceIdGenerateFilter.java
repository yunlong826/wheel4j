package com.wheel.admin.filter;

import com.wheel.admin.utils.TraceIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: traceId生成过滤器
 * 关于MDC实现全链路调用日志跟踪相关知识,可参考:
 * https://blog.csdn.net/weixin_70730532/article/details/125183057
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/31 14:43
 */
@Order(-2147483648)
public class TraceIdGenerateFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(TraceIdGenerateFilter.class);

    public TraceIdGenerateFilter(){

    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceId = TraceIdUtils.genTraceIdWithSW();
        MDC.put("X-TraceId", traceId);
        MDC.put("X-SpanId", traceId);
        MDC.put("X-Service-Type", "CONTROLLER");
        MDC.put("X-Service-Name", request.getRequestURI());
        TraceIdUtils.setSpanId(traceId);
        log.debug("Generate global traceId ：{}", traceId);
        MDC.put("domain", this.getDomain(request));
        filterChain.doFilter(request, response);
        TraceIdUtils.clear();
        MDC.clear();
    }

    private String getDomain(HttpServletRequest request) {
        String domain = request.getServerName();
        log.debug("the domain name {}", domain);
        return domain;
    }
}
