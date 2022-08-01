package com.wheel.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wheel.admin.annotation.SystemLogController;
import com.wheel.admin.dto.ResultDto;
import com.wheel.admin.model.SysLog;
import com.wheel.admin.service.SysLogService;
import com.wheel.admin.wrapper.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 12:59
 */
@RestController
@RequestMapping("/sysLog")
@Api(tags = "用户操作日志展示")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     *
     *
     * @param account 账户
     * @return com.wheel.admin.dto.ResultDto<com.baomidou.mybatisplus.core.metadata.IPage<com.wheel.admin.model.SysLog>>
     * @author long_yun
     * @date 2022/7/30 13:06
     * @describe
     */

    @ApiOperation(value = "展示用户操作日志")
    @PostMapping("/pageSysLogs")
    @SystemLogController(description = "展示用户操作日志")
    public ResultDto<IPage<SysLog>> pageSysLogs(@RequestBody Page page, @RequestParam("account") String account){
        IPage<SysLog> page1 = sysLogService.page(page, new LambdaQueryWrapper<SysLog>().eq(SysLog::getUsername, account));
        return ResultWrapper.success(page1);
    }

}
