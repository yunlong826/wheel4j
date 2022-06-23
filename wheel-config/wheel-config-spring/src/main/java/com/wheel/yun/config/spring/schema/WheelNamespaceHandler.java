package com.wheel.yun.config.spring.schema;


import com.wheel.yun.config.common.ApplicationConfig;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.spring.ReferenceBean;
import com.wheel.yun.config.spring.ServiceBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/28 17:29
 */
public class WheelNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("application", new WheelBeanDefinitionParser(ApplicationConfig.class));
        registerBeanDefinitionParser("registry", new WheelBeanDefinitionParser(RegistryConfig.class));
        registerBeanDefinitionParser("protocol", new WheelBeanDefinitionParser(ProtocolConfig.class));
        registerBeanDefinitionParser("service", new WheelBeanDefinitionParser(ServiceBean.class));
        registerBeanDefinitionParser("reference", new WheelBeanDefinitionParser(ReferenceBean.class));
    }



}
