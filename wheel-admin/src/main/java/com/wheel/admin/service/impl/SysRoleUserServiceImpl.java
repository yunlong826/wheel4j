package com.wheel.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wheel.admin.mapper.SysRoleUserMapper;
import com.wheel.admin.model.SysUserRoleRelation;
import com.wheel.admin.service.SysRoleUserService;
import org.springframework.stereotype.Service;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 12:52
 */
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysUserRoleRelation> implements SysRoleUserService {
}
