package com.wheel.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.service.CreateUserService;
import com.wheel.admin.service.SysUserService;
import com.wheel.admin.wrapper.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 10:57
 */
@RestController
@RequestMapping("/sysUser")
@Api(tags = "用户相关信息接口")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CreateUserService createUserService;

    /**
     *
     *
     * @param page    分页查询条件
     * @param userId  用户Id
     * @return com.wheel.admin.wrapper.ResultWrapper<com.baomidou.mybatisplus.core.metadata.IPage<com.wheel.admin.model.SysUser>>
     * @author long_yun
     * @date 2022/7/30 11:07
     * @describe
     */

    @ApiOperation(value = "分页查询用户列表",notes = "如果该用户为管理员，分页显示所有用户，非管理员，显示该用户创建的用户列表")
    @GetMapping("/pageUsers")
    public ResultDto<IPage<SysUser>> pageUsers(@RequestBody IPage<SysUser> page, String userId){

        IPage<SysUser> sysUserIPage = sysUserService.pageUsersByUserId(page, userId);;
        return ResultWrapper.success(sysUserIPage);
    }

    /**
     *
     *
     * @param sysUser
     * @param createUserId
     * @return com.wheel.admin.dto.ResultDto
     * @author long_yun
     * @date 2022/7/30 11:38
     * @describe
     */

    @ApiOperation(value = "创建用户")
    @PostMapping("/createUser")
    public ResultDto createUser(@RequestBody SysUser sysUser,@RequestParam("userId") String createUserId){
        ResultDto resultDto = createUserService.addUserByUsername(sysUser, createUserId);
        return resultDto;
    }

    /**
     *
     *
     * @param userId 
     * @return com.wheel.admin.dto.ResultDto 
     * @author long_yun
     * @date 2022/7/30 11:53 
     * @describe
     */
    
    @ApiOperation(value = "删除用户")
    @PostMapping("/deleteUser")
    public ResultDto deletUser(@RequestParam("userId") String userId){
        Integer integer = sysUserService.deleteUserById(userId);
        if(integer == 1){
            return new ResultWrapper<>().success(ResultEnumCode.SUCCESS);
        }
        return new ResultWrapper<>().fail(ResultEnumCode.COMMON_FAIL);
    }

    /**
     *
     *
     * @param sysUser
     * @return com.wheel.admin.dto.ResultDto
     * @author long_yun
     * @date 2022/7/30 12:01
     * @describe
     */

    @ApiOperation(value = "修改用户")
    @PostMapping("/editUser")
    public ResultDto editUser(@RequestBody SysUser sysUser){
        boolean b = sysUserService.updateById(sysUser);
        if(b){
            return new ResultWrapper<>().success(ResultEnumCode.SUCCESS);
        }
        return new ResultWrapper<>().fail(ResultEnumCode.COMMON_FAIL);
    }

}
