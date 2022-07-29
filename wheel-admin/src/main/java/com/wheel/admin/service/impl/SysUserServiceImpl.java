package com.wheel.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wheel.admin.mapper.SysUserMapper;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 20:07
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}
