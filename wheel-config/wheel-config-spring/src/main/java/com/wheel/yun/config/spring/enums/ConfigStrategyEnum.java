package com.wheel.yun.config.spring.enums;


import com.wheel.yun.config.common.*;
import com.wheel.yun.config.spring.ReferenceBean;
import com.wheel.yun.config.spring.ServiceBean;

public enum ConfigStrategyEnum {
    WheelApplication("application", ApplicationConfig.class),
    WheelProtocol("protocol", ProtocolConfig.class),
    WheelReference("reference", ReferenceBean.class),
    WheelRegistry("registry", RegistryConfig.class),
    WheelService("service", ServiceBean.class);

    private String configString;
    private Class configClass;

    ConfigStrategyEnum(String configString,Class configClass){
        this.configString = configString;
        this.configClass = configClass;
    }

    public Class getConfigClass() {
        return configClass;
    }

    public String getConfigString() {
        return configString;
    }

}
