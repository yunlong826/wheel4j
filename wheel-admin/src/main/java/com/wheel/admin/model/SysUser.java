package com.wheel.admin.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/29 19:30
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户表")
public class SysUser {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "上一次登录的时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "账号是否可用。默认为1（可用）")
    private Boolean enabled;

    @ApiModelProperty(value = "是否过期。默认为1（没有过期）")
    private Boolean notExpired;

    @ApiModelProperty(value = "账号是否锁定。默认为1（没有锁定）")
    private Boolean accountNotLocked;

    @ApiModelProperty(value = "证书（密码）是否过期。默认为1（没有过期）")
    private Boolean credentialsNotExpired;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer createUser;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;
}
