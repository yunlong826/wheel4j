package com.wheel.admin.security.jwt;

import com.wheel.admin.jwt.utils.JwtUtils;
import com.wheel.admin.security.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 用来判断JWT是否有效(只走一遍过滤器)
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 20:47
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityUserService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取当请求头中的token，其实这里多余，完全可以使用HttpServletRequest来获取
        String authToken = request.getHeader("UserToken");

        // 获取到当前用户的account
        String account = JwtUtils.getMemberAccountByJwtToken(request);

        System.out.println("自定义JWT过滤器获得用户名为"+account);

        // 当token中的username不为空时进行验证token是否是有效的token
        if (!account.equals("") && SecurityContextHolder.getContext().getAuthentication() == null) {
            // token中username不为空，并且Context中的认证为空，进行token验证

            // 获取到用户的信息，也就是获取到用户的权限
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(account);

            if (JwtUtils.checkToken(authToken)) {   // 验证当前token是否有效

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //将authentication放入SecurityContextHolder中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // 放行给下个过滤器
        filterChain.doFilter(request, response);

    }
}
