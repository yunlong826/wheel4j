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
 * @date 2022/7/29 19:27
 */
@Data
@Accessors(chain = true)
@TableName("sys_role_permission_relation")
@ApiModel(value = " SysRolePermissionRelation对象", description = "角色-权限关联关系表")
public class SysRolePermissionRelation {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色Id")
    private String roleId;

    @ApiModelProperty(value = "权限Id")
    private String permissionId;
}
