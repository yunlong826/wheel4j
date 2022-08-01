package com.wheel.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wheel.admin.model.SysUserRoleRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 0:06
 */
@Mapper
public interface SysRoleUserMapper extends BaseMapper<SysUserRoleRelation> {
}
