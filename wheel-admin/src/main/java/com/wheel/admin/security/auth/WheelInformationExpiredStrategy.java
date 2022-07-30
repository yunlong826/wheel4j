package com.wheel.admin.security.auth;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.wrapper.ResultWrapper;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 旧用户被踢出后的处理逻辑
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/30 18:17
 */
@Component
public class WheelInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        ResultDto result = ResultWrapper.fail(ResultEnumCode.SESSION_SAME_LOGIN);
        event.getResponse().setContentType("text/json;charset=utf-8");
        event.getResponse().getWriter().write(JSON.toJSONString(result));
    }
}
