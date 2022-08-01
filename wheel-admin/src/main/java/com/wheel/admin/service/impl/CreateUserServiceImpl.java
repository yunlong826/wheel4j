package com.wheel.admin.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wheel.admin.annotation.SystemLogService;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.exception.UserException;
import com.wheel.admin.model.SysUser;
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
    private SysUserService sysUserService;

    @SystemLogService(description = "addUserByUsername")
    @Override
    public ResultDto addUserByUsername(SysUser userRegister, String createUserId){

        SysUser newuser = sysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getAccount, userRegister.getAccount()));
        if (newuser != null){
            // 用户已经存在，抛出相关异常信息
            throw new UserException(ResultEnumCode.USER_ACCOUNT_ALREADY_EXIST.getCode()
                                    ,ResultEnumCode.USER_ACCOUNT_ALREADY_EXIST.getMessage());
        }
        //新用户密码采用BCryptPasswordEncoder()格式存入数据库
        userRegister.setPassword(new BCryptPasswordEncoder().encode(userRegister.getPassword()));
        //设置用户状态可用，没有锁定
        userRegister.setEnabled(true);
        userRegister.setCreateUser(Integer.valueOf(createUserId));
        userRegister.setCreateTime(LocalDateTime.now());
        userRegister.setAccountNotLocked(true);
        userRegister.setNotExpired(true);
        userRegister.setUpdateTime(LocalDateTime.now());
        //执行用户注册
        boolean adduser = sysUserService.saveOrUpdate(userRegister);
        if(!adduser){
            // 用户注册失败
            throw new UserException(ResultEnumCode.CREATE_USER_FAIL.getCode()
                                    ,ResultEnumCode.CREATE_USER_FAIL.getMessage());
        }
        return ResultWrapper.success(ResultEnumCode.SUCCESS_CREATE_USER);

    }


}
