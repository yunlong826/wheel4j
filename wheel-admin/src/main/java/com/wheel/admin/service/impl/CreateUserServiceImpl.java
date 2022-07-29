package com.wheel.admin.service.impl;


import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.mapper.SysRoleUserMapper;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.model.SysUserRoleRelation;
import com.wheel.admin.security.SecurityUserService;
import com.wheel.admin.service.CreateUserService;
import com.wheel.admin.service.SysUserService;
import com.wheel.admin.wrapper.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 创建用户服务类
 * @date 2022/7/29 23:52
 */
@Component
public class CreateUserServiceImpl implements CreateUserService {

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public ResultDto addUserByUsername(SysUser userRegister, String roleId, String createUserId){
        SysUser newuser = (SysUser) securityUserService.loadUserByUsername(userRegister.getAccount());
        if (newuser != null){
            return ResultWrapper.fail(ResultEnumCode.USER_ACCOUNT_ALREADY_EXIST);
        }else {
            //新用户密码采用BCryptPasswordEncoder()格式存入数据库
            userRegister.setPassword(new BCryptPasswordEncoder().encode(userRegister.getPassword()));
            //设置用户状态可用，没有锁定
            userRegister.setEnabled(true);
            userRegister.setCreateUser(Integer.valueOf(createUserId));
            userRegister.setCreateTime(LocalDateTime.now());
            userRegister.setAccountNotLocked(true);
            userRegister.setNotExpired(true);
            //执行用户注册
            boolean adduser = sysUserService.saveOrUpdate(newuser);
            //用户成功注册后，添加用户角色
            if(adduser){
                SysUser getuser = (SysUser) securityUserService.loadUserByUsername(userRegister.getAccount());
                SysUserRoleRelation sysUserRoleRelation = new SysUserRoleRelation();
                sysUserRoleRelation.setUserId(getuser.getId());
                sysUserRoleRelation.setRoleId(Integer.valueOf(roleId));
                int addrole = sysRoleUserMapper.insert(sysUserRoleRelation);
                if (addrole > 0){
                    return ResultWrapper.success(ResultEnumCode.SUCCESS_CREATE_USER);
                }else{
                    return ResultWrapper.fail(ResultEnumCode.CREATE_USERROLE_FAIL);
                }
            }else {
                return ResultWrapper.fail(ResultEnumCode.CREATE_USER_FAIL);
            }
        }
    }


}
