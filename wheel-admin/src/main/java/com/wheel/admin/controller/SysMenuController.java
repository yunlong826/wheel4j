package com.wheel.admin.controller;

import com.wheel.admin.controller.vo.RouterVo;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.model.SysMenu;
import com.wheel.admin.service.SysMenuService;
import com.wheel.admin.wrapper.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.zookeeper.util.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TangBaoLiang
 * @date 2022/7/31
 * @email developert163@163.com
 **/
@RestController
@Api(tags = "系统菜单")
@RequestMapping("/menu")
public class SysMenuController {

    @Resource
    private SysMenuService menuService;

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    @ApiOperation("获取所有菜单项")
    public ResultDto<List<RouterVo>> getRouters()
    {
        List<SysMenu> menus = menuService.selectMenuTree();
        return ResultWrapper.success(menuService.buildMenus(menus));
    }
}
