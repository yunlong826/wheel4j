package com.wheel.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 19:38
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_role_relation")
@ApiModel(value = "SysUserRoleRelation对象", description = "用户角色关联关系表")
public class SysUserRoleRelation {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户Id")
    private Integer userId;

    @ApiModelProperty(value = "权限Id")
    private Integer roleId;
}
