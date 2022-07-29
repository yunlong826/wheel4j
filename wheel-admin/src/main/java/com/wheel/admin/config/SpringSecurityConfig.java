package com.wheel.admin.config;

import com.wheel.admin.security.SecurityUserService;
import com.wheel.admin.security.auth.WheelAbstractSecurityInterceptor;
import com.wheel.admin.security.auth.WheelAccessDecisionManager;
import com.wheel.admin.security.auth.WheelAccessDeniedHandler;
import com.wheel.admin.security.auth.WheelFilterInvocationSecurityMetadataSource;
import com.wheel.admin.security.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * Description: spring security 配置
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 21:07
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private WheelAbstractSecurityInterceptor wheelAbstractSecurityInterceptor;

    @Autowired
    private WheelAccessDecisionManager wheelAccessDecisionManager;

    @Autowired
    private WheelFilterInvocationSecurityMetadataSource wheelFilterInvocationSecurityMetadataSource;

    @Autowired
    private WheelAccessDeniedHandler WheelAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 对请求进行鉴权的配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and().csrf().disable();

        http.authorizeRequests().
                withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(wheelAccessDecisionManager);//访问决策管理器
                        o.setSecurityMetadataSource(wheelFilterInvocationSecurityMetadataSource);//安全元数据源
                        return o;
                    }
                });

        http.authorizeRequests()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(WheelAccessDeniedHandler)
                .and()
                .formLogin()  // 登录
                .permitAll()  //允许所有用户
                .successHandler(authenticationSuccessHandler)  //登录成功处理逻辑
                .failureHandler(authenticationFailureHandler)  //登录失败处理逻辑
                .and()
                .logout()      // 退出
                .permitAll()   //允许所有用户
                .logoutSuccessHandler(logoutSuccessHandler)  //退出成功处理逻辑
                .deleteCookies("JSESSIONID")   //登出之后删除cookie
                .and()
                .sessionManagement()    //会话管理
                .maximumSessions(1)     //同一账号同时登录最大用户数
                .expiredSessionStrategy(sessionInformationExpiredStrategy);

        http.addFilterBefore(wheelAbstractSecurityInterceptor, FilterSecurityInterceptor.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserService);
    }

    /**
     * 默认开启密码加密，前端传入的密码Security会在加密后和数据库中的密文进行比对，一致的话就登录成功
     * 所以必须提供一个加密对象，供security加密前端明文密码使用
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
