package com.wheel.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wheel.admin.model.SysLog;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/7/29 23:06
 */
public interface SysLogService extends IService<SysLog> {

    /**
     *
     *
     * @param sysLog 
     * @return void 
     * @author long_yun
     * @date 2022/7/29 23:08 
     * @describe
     */
    
    void insertSysLog(SysLog sysLog);
}
