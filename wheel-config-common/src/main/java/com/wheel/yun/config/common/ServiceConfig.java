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
    private Integer weight = 0;
    private String loadbalance = "consistentHash";

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

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
