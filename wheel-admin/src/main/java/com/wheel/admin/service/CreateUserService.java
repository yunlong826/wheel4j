package com.wheel.admin.service;


import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.model.SysUser;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/29 23:51
 */
public interface CreateUserService{
    ResultDto addUserByUsername(SysUser userRegister, String roleId, String createUserId);
}
