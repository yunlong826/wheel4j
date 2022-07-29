package com.wheel.admin.security.auth;

import com.alibaba.fastjson.JSON;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.wrapper.ResultWrapper;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 登录账户时失败的处理
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 20:21
 */
public class WheelAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //返回json数据
        ResultDto result = null;
        if (e instanceof AccountExpiredException) {
            //账号过期
            result = ResultWrapper.fail(ResultEnumCode.USER_ACCOUNT_EXPIRED);
        } else if (e instanceof BadCredentialsException) {
            //密码错误
            result = ResultWrapper.fail(ResultEnumCode.USER_CREDENTIALS_ERROR);
        } else if (e instanceof CredentialsExpiredException) {
            //密码过期
            result = ResultWrapper.fail(ResultEnumCode.USER_CREDENTIALS_EXPIRED);
        } else if (e instanceof DisabledException) {
            //账号不可用
            result = ResultWrapper.fail(ResultEnumCode.USER_ACCOUNT_DISABLE);
        } else if (e instanceof LockedException) {
            //账号锁定
            result = ResultWrapper.fail(ResultEnumCode.USER_ACCOUNT_LOCKED);
        } else if (e instanceof InternalAuthenticationServiceException) {
            //用户不存在
            result = ResultWrapper.fail(ResultEnumCode.USER_ACCOUNT_NOT_EXIST);
        }else{
            //其他错误
            result = ResultWrapper.fail(ResultEnumCode.COMMON_FAIL);
        }
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        // 把Json数据放入到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(result));

    }
}
