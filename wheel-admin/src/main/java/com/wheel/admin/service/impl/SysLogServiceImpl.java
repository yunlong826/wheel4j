package com.wheel.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wheel.admin.mapper.SysLogMapper;
import com.wheel.admin.model.SysLog;
import com.wheel.admin.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/29 23:07
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void insertSysLog(SysLog sysLog) {
        sysLogMapper.insert(sysLog);
    }
}
