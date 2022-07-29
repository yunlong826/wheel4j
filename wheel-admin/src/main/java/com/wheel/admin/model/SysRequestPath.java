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
 * @date 2022/7/29 19:13
 */
@Data
@Accessors(chain = true)
@TableName("sys_request_path")
@ApiModel(value = "SysRequestPath对象", description = "请求路径表")
public class SysRequestPath {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "请求路径")
    private String url;

    @ApiModelProperty(value = "路径描述")
    private String decription;
}
