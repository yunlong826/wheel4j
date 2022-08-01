package com.wheel.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wheel.admin.annotation.SystemLogController;
import com.wheel.admin.controller.form.UserAddForm;
import com.wheel.admin.controller.form.UserUpdateForm;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.exception.UserException;
import com.wheel.admin.jwt.utils.JwtUtils;
import com.wheel.admin.model.SysPermission;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.service.CreateUserService;
import com.wheel.admin.service.SysPermissionService;
import com.wheel.admin.service.SysUserService;
import com.wheel.admin.wrapper.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
     * @return com.wheel.admin.wrapper.ResultWrapper<com.baomidou.mybatisplus.core.metadata.IPage<com.wheel.admin.model.SysUser>>
     * @author long_yun
     * @date 2022/7/30 11:07
     * @describe
     */

    @ApiOperation(value = "分页查询用户列表",notes = "如果该用户为管理员，分页显示所有用户，非管理员，显示该用户创建的用户列表")
    @PostMapping("/pageUsers")
    @SystemLogController(description = "分页查询用户列表")
    public ResultDto<IPage<SysUser>> pageUsers(@RequestBody Page page, @ApiParam(hidden = true) HttpServletRequest httpServletRequest){
        String userId = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        IPage<SysUser> sysUserIPage = sysUserService.pageUsersByUserId(page, userId);;
        return ResultWrapper.success(sysUserIPage);
    }

    /**
     *
     *
     * @return com.wheel.admin.dto.ResultDto
     * @author long_yun
     * @date 2022/7/30 11:38
     * @describe
     */

    @ApiOperation(value = "创建用户")
    @PostMapping("/createUser")
    @SystemLogController(description = "创建用户")
    public ResultDto createUser(@RequestBody UserAddForm userAddForm, HttpServletRequest httpServletRequest){
        //details里面可能存放了当前登录用户的详细信息，也可以通过cast后拿到
        String userId = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userAddForm, sysUser);
        ResultDto resultDto = createUserService.addUserByUsername(sysUser, userId);
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
    @DeleteMapping("/deleteUser")
    @SystemLogController(description = "删除用户")
    public ResultDto deletUser(@RequestParam("userId") String userId, HttpServletRequest httpServletRequest){
        String thisUserId = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        if (userId.equals(thisUserId)) {
            throw new UserException(ResultEnumCode.USER_DELETE_FAIL.getCode(), "不能删除自己");
        }
        sysUserService.deleteUserById(userId);
        return new ResultWrapper<>().success(ResultEnumCode.SUCCESS);

    }

    /**
     *
     *
     * @return com.wheel.admin.dto.ResultDto
     * @author long_yun
     * @date 2022/7/30 12:01
     * @describe
     */

    @ApiOperation(value = "修改用户")
    @PutMapping("/editUser")
    @SystemLogController(description = "修改用户")
    public ResultDto editUser(@RequestBody UserUpdateForm userUpdateForm,@ApiParam(hidden = true) HttpServletRequest request){
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userUpdateForm, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        sysUser.setUpdateUser(Integer.valueOf(userId));
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        boolean b = sysUserService.updateById(sysUser);
        if(!b){
            throw new UserException(ResultEnumCode.EDIT_USER_FAIL.getCode()
                                    ,ResultEnumCode.EDIT_USER_FAIL.getMessage());
        }
        return new ResultWrapper<>().success(ResultEnumCode.SUCCESS);


    }

    @ApiOperation(value = "获取当前用户的信息")
    @GetMapping("/userInfo")
    @SystemLogController(description = "当前登录用户获取登录信息")
    public ResultDto getInfo(@ApiParam(hidden = true) HttpServletRequest httpServletRequest) {
        String userId = JwtUtils.getMemberIdByJwtToken(httpServletRequest);
        SysUser sysUser = sysUserService.getById(Integer.valueOf(userId));
        sysUser.setPassword("");

        return ResultWrapper.success(sysUser);
    }

}
