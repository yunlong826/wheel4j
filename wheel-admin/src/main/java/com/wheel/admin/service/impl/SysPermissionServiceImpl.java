package com.wheel.admin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wheel.admin.mapper.SysPermissionMapper;
import com.wheel.admin.model.SysPermission;
import com.wheel.admin.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 19:44
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper,SysPermission> implements SysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> getUserRolesByUserId(String userId) {
        return sysPermissionMapper.getUserRolesByUserId(userId);
    }

    @Override
    public List<SysPermission> selectListByPath(String requestUrl) {
        return sysPermissionMapper.selectListByPath(requestUrl);
    }
}
