package com.wheel.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.dto.SysUserRoleDto;
import com.wheel.admin.model.SysRole;
import com.wheel.admin.model.SysUser;

import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 12:12
 */
public interface SysRoleService extends IService<SysRole> {
    ResultDto<List<SysUserRoleDto>> listUserRole(String userId);
}
