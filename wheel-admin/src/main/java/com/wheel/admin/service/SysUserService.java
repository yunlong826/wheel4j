package com.wheel.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wheel.admin.model.SysUser;

public interface SysUserService extends IService<SysUser> {
    IPage<SysUser> pageUsersByUserId(IPage<SysUser> condition,String userId);

    Integer deleteUserById(String userId);
}
