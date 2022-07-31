package com.wheel.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wheel.admin.annotation.SystemLogController;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.dto.SysUserRoleDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.exception.UserException;
import com.wheel.admin.mapper.SysRoleMapper;
import com.wheel.admin.mapper.SysRoleUserMapper;
import com.wheel.admin.model.SysRole;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.model.SysUserRoleRelation;
import com.wheel.admin.service.SysRoleService;
import com.wheel.admin.service.SysRoleUserService;
import com.wheel.admin.service.SysUserService;
import com.wheel.admin.wrapper.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 12:06
 */
@RestController
@RequestMapping("/sysRole")
@Api(tags = "用户权限相关接口")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleUserService sysRoleUserService;



    @ApiOperation(value = "查询用户名与用户角色")
    @GetMapping("/listUserRole")
    @SystemLogController(description = "查询用户名与用户角色")
    public ResultDto<List<SysUserRoleDto>> listUserRole( @RequestParam("userId") String userId){

        return sysRoleService.listUserRole(userId);
    }

    @ApiOperation(value = "绑定角色")
    @PostMapping("/bindRoles")
    @SystemLogController(description = "绑定角色")
    public ResultDto<String> bindRoles(@RequestParam("roleName") String roleName,@RequestParam("account") String account){
        SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, roleName));
        SysUser byId = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, account));
        if(sysRole == null){
            throw new UserException(ResultEnumCode.USER_ROLE_GET_FAIL.getCode()
                                    ,ResultEnumCode.USER_ROLE_GET_FAIL.getMessage());
        }
        SysUserRoleRelation sysUserRoleRelation = new SysUserRoleRelation();

        sysUserRoleRelation.setRoleId(sysRole.getId());
        sysUserRoleRelation.setUserId(byId.getId());
        boolean b = sysRoleUserService.saveOrUpdate(sysUserRoleRelation);
        if(!b){
            throw new UserException(ResultEnumCode.BIND_USER_ROLE_FAIL.getCode()
                                    ,ResultEnumCode.BIND_USER_ROLE_FAIL.getMessage());
        }
        return ResultWrapper.success();


    }
}
