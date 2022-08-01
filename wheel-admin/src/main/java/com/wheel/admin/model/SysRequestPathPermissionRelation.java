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
 * @date 2022/7/29 19:15
 */
@Data
@Accessors(chain = true)
@TableName("sys_request_path_permission_relation")
@ApiModel(value = "SysRequestPathPermissionRelation对象", description = "路径权限关联表")
public class SysRequestPathPermissionRelation {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "请求路径Id")
    private String urlId;

    @ApiModelProperty(value = "权限Id")
    private String permissionId;
}
