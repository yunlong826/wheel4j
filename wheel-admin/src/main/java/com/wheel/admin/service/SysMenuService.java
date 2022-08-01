package com.wheel.admin.service;

import com.wheel.admin.controller.vo.RouterVo;
import com.wheel.admin.model.SysMenu;

import java.util.List;

/**
 * @author TangBaoLiang
 * @date 2022/7/31
 * @email developert163@163.com
 **/
public interface SysMenuService {
    public List<SysMenu> selectMenuTree();
    public List<RouterVo> buildMenus(List<SysMenu> menus);
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);
}
