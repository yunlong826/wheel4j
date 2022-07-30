package com.wheel.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wheel.admin.annotation.SystemLogService;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.dto.SysUserRoleDto;
import com.wheel.admin.mapper.SysRoleMapper;
import com.wheel.admin.mapper.SysRoleUserMapper;
import com.wheel.admin.model.SysRole;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.model.SysUserRoleRelation;
import com.wheel.admin.service.SysRoleService;
import com.wheel.admin.service.SysUserService;
import com.wheel.admin.wrapper.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 12:13
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysUserService sysUserService;

    @SystemLogService(description = "listUserRole")
    @Override
    public ResultDto<List<SysUserRoleDto>> listUserRole(String userId) {
        List<SysUserRoleRelation> sysUserRoleRelations = sysRoleUserMapper.selectList(new LambdaQueryWrapper<SysUserRoleRelation>().eq(SysUserRoleRelation::getUserId,userId));
        List<Integer> ids = new ArrayList<>();
        for(SysUserRoleRelation sysUserRoleRelation:sysUserRoleRelations){
            ids.add(sysUserRoleRelation.getRoleId());
        }
        List<SysRole> sysRoles = sysRoleMapper.selectBatchIds(ids);
        SysUser byId = sysUserService.getById(Integer.valueOf(userId));
        List<SysUserRoleDto> sysUserRoleDtos = new ArrayList<>();
        for(int i = 0;i < sysRoles.size();i++){
            sysUserRoleDtos.add(new SysUserRoleDto(sysRoles.get(i).getRoleName(),byId.getAccount()));
        }
        return new ResultWrapper<>().success(sysUserRoleDtos);
    }
}
