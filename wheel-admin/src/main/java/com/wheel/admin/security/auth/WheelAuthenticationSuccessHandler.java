package com.wheel.admin.security.auth;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.jwt.utils.JwtUtils;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.service.SysUserService;
import com.wheel.admin.wrapper.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 20:38
 */
public class WheelAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SysUserService sysUserService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新人、更新时间等字段
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("account",userDetails.getUsername()));
        sysUser.setLastLoginTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setUpdateUser(sysUser.getId());
        sysUserService.update(sysUser,new QueryWrapper<SysUser>().eq("id",sysUser.getId()));

        // 根据用户的id和account生成token并返回
        String jwtToken = JwtUtils.getJwtToken(sysUser.getId().toString(), sysUser.getAccount());
        Map<String,String> results = new HashMap<>();
        results.put("token",jwtToken);

        //返回json数据
        ResultDto result = ResultWrapper.success(ResultEnumCode.SUCCESS_login,results);
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        // 把Json数据放入HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(result));

    }
}
