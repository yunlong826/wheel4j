package com.wheel.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wheel.admin.model.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> getUserRolesByUserId(@Param("userId") String userId);

    List<SysPermission> selectListByPath(String requestUrl);
}
