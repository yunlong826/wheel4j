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
 * @date 2022/7/29 19:07
 */
@Data
@Accessors(chain = true)
@TableName("sys_permission")
@ApiModel(value = " SysPermission对象", description = "用户权限表")
public class SysPermission {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限code")
    private String permissionCode;

    @ApiModelProperty(value = "权限名")
    private String permissionName;
}
