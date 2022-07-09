package com.wheel.yun.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/31 14:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterfaceConfig {

    private String group="";

    private String version="";

    private String timeout="40000";

    private String failStrategy="";

    private String retryCount="3";
}
