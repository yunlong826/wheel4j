package com.wheel.yun.config.spring;


import com.wheel.yun.common.config.InterfaceConfig;
import com.wheel.yun.config.common.ReferenceProxy;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.common.cache.WheelBeanDefinitionCache;
import org.springframework.beans.factory.FactoryBean;

import java.util.Map;


/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/7/10 1:20
 */
public class ProxyFactory<T> implements FactoryBean<T> {
    private Class<T> interfaceClass;
    private RegistryConfig registryConfig;
    private InterfaceConfig interfaceConfig;

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public InterfaceConfig getInterfaceConfig() {
        return interfaceConfig;
    }

    public void setInterfaceConfig(InterfaceConfig interfaceConfig) {
        this.interfaceConfig = interfaceConfig;
    }

    @Override
    public T getObject() throws Exception {

        return (T) new ReferenceProxy(interfaceClass, interfaceConfig, registryConfig).getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }
    private InterfaceConfig transform(ReferenceBean ref) {
        InterfaceConfig interfaceConfig = new InterfaceConfig();
        interfaceConfig.setGroup(ref.getGroup());
        interfaceConfig.setVersion(ref.getVersion());
        interfaceConfig.setTimeout(ref.getTimeout());
        interfaceConfig.setFailStrategy(ref.getFailStrategy());
        interfaceConfig.setRetryCount(ref.getRetryCount());
        return interfaceConfig;
    }
}
