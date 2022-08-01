package com.wheel.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wheel.admin.model.SysPermission;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {
    List<SysPermission> getUserRolesByUserId(String userId);

    List<SysPermission> selectListByPath(String requestUrl);
}
