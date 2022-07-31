package com.wheel.admin.controller.form;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TangBaoLiang
 * @date 2022/7/30
 * @email developert163@163.com
 **/
@Data
public class UserUpdateForm {
    @ApiModelProperty(value = "用户 id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String password;


    @ApiModelProperty(value = "账号是否可用")
    private Boolean enabled;

    @ApiModelProperty(value = "是否过期")
    private Boolean notExpired;

    @ApiModelProperty(value = "账号是否锁定")
    private Boolean accountNotLocked;

    @ApiModelProperty(value = "证书（密码）是否过期）")
    private Boolean credentialsNotExpired;

}
