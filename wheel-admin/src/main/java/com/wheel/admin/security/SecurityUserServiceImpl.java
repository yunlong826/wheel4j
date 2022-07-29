package com.wheel.admin.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wheel.admin.model.SysPermission;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.service.SysPermissionService;
import com.wheel.admin.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 18:59
 */
@Service
public class SecurityUserServiceImpl implements SecurityUserService {

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired
    private SysUserService sysUserService;


    /**
     * 根据用户名查找数据库，判断是否存在这个用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 用户名必须是唯一的，不允许重复
        SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("account",username));

        if(StringUtils.isEmpty(sysUser)){
            throw new UsernameNotFoundException("根据用户名找不到该用户的信息！");
        }

        List<SysPermission> sysPermissions = sysPermissionService.getUserRolesByUserId(String.valueOf(sysUser.getId()));
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        sysPermissions.stream().forEach(sysPermission -> {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getPermissionCode());
            grantedAuthorities.add(grantedAuthority);
        });

        User user = new User(sysUser.getAccount(), sysUser.getPassword(), sysUser.getEnabled(), sysUser.getNotExpired(), sysUser.getCredentialsNotExpired(), sysUser.getAccountNotLocked(), grantedAuthorities);
        return user;
    }
}
