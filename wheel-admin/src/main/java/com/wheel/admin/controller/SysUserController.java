package com.wheel.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wheel.admin.annotation.SystemLogController;
import com.wheel.admin.controller.form.UserAddForm;
import com.wheel.admin.controller.form.UserUpdateForm;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.enums.ResultEnumCode;
import com.wheel.admin.jwt.utils.JwtUtils;
import com.wheel.admin.model.SysUser;
import com.wheel.admin.service.CreateUserService;
import com.wheel.admin.service.SysUserService;
import com.wheel.admin.wrapper.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;

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
    @PostMapping("/deleteUser")
    @SystemLogController(description = "删除用户")
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
     * @return com.wheel.admin.dto.ResultDto
     * @author long_yun
     * @date 2022/7/30 12:01
     * @describe
     */

    @ApiOperation(value = "修改用户")
    @PostMapping("/editUser")
    @SystemLogController(description = "修改用户")
    public ResultDto editUser(@RequestBody UserUpdateForm userUpdateForm,@ApiParam(hidden = true) HttpServletRequest request){
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userUpdateForm, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        sysUser.setUpdateUser(Integer.valueOf(userId));
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        boolean b = sysUserService.updateById(sysUser);
        if(b){
            return new ResultWrapper<>().success(ResultEnumCode.SUCCESS);
        }
        return new ResultWrapper<>().fail(ResultEnumCode.COMMON_FAIL);
    }

}
