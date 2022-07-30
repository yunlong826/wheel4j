package com.wheel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wheel.admin.annotation.SystemLogService;
import com.wheel.admin.mapper.SysRoleMapper;
import com.wheel.admin.mapper.SysRoleUserMapper;
import com.wheel.admin.mapper.SysUserMapper;
import com.wheel.admin.model.SysRole;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.model.SysUserRoleRelation;
import com.wheel.admin.service.SysUserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.acl.NotOwnerException;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 20:07
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @SystemLogService(description = "pageUsersByUserId")
    @Override
    public IPage<SysUser> pageUsersByUserId(IPage<SysUser> condition, String userId) {
        List<SysUserRoleRelation> sysUserRoleRelations = sysRoleUserMapper.selectList(new LambdaQueryWrapper<SysUserRoleRelation>().eq(SysUserRoleRelation::getUserId,userId));

        boolean isAdmin = false;

        for(SysUserRoleRelation sysUserRoleRelation:sysUserRoleRelations){
            Integer roleId = sysUserRoleRelation.getRoleId();

            SysRole sysRole = sysRoleMapper.selectById(roleId);
            if(sysRole.getRoleName().equals("admin")){
                isAdmin = true;
                break;
            }
        }
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(!isAdmin){
            // 普通用户
            lambdaQueryWrapper.eq(SysUser::getCreateUser, Integer.valueOf(userId));
        }
        IPage<SysUser> sysUserIPage = sysUserMapper.selectPage(condition, lambdaQueryWrapper);
        return sysUserIPage;


    }

    @SystemLogService(description = "deleteUserById")
    @Override
    public Integer deleteUserById(String userId) {
        int delete = sysRoleUserMapper.delete(new LambdaQueryWrapper<SysUserRoleRelation>().eq(SysUserRoleRelation::getUserId, userId));
        int delete1 = sysUserMapper.delete(new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, userId));
        return delete == 1&&delete1 == 1?1:0;
    }
}
