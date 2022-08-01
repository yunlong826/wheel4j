package com.wheel.admin.service;

import com.wheel.admin.model.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author TangBaoLiang
 * @date 2022/8/1
 * @email developert163@163.com
 **/
@SpringBootTest
class CreateUserServiceTest {

    @Resource
    private CreateUserService createUserService;

    @Test
    void addUserByUsername() {
        SysUser sysUser = new SysUser();
        sysUser.setPassword("123456");
        sysUser.setAccount("666666");
        sysUser.setUpdateUser(1);
        sysUser.setCreateUser(1);
        sysUser.setUserName("666666");

        createUserService.addUserByUsername(sysUser, "1");
    }
}
