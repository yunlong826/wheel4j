package com.wheel.yun.config.common;


import com.wheel.yun.common.config.InterfaceConfig;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/29 14:36
 */
public class ServiceConfig<T> extends InterfaceConfig {

    private String interfaceName;
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getInterface() {
        return interfaceName;
    }

    public void setInterface(String interfaceName) {
        this.interfaceName = interfaceName;
    }

}
