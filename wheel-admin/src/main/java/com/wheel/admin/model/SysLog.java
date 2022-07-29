package com.wheel.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/29 23:00
 */
@Data
@Accessors(chain = true)
@TableName("sys_log")
@ApiModel(value = "SysLog对象", description = "用户操作日志表")
public class SysLog {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "请求接口")
    private String uri;

    @ApiModelProperty(value = "请求方式")
    private String method;

    @ApiModelProperty(value = "方法描述")
    private String MethodDescribe;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "浏览器类型")
    private String browser;
}
