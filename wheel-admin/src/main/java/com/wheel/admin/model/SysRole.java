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
 * @date 2022/7/29 19:20
 */
@Data
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value = "SysRole对象", description = "用户角色表")
public class SysRole {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色值")
    private String roleCode;

    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "角色说明")
    private String roleDescription;
}
