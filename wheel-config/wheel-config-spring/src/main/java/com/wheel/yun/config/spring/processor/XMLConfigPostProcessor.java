package com.wheel.yun.config.spring.processor;

import com.wheel.yun.config.spring.processor.params.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/8/3 13:53
 */
public  class XMLConfigPostProcessor implements ConfigPostProcessor{
    private List<Class> configSetPostProcessor = new ArrayList<>();

    public XMLConfigPostProcessor(){
        configSetPostProcessor.add(ApplicationParamsPostProcessor.class);
        configSetPostProcessor.add(ProtocolParamsPostProcessor.class);
        configSetPostProcessor.add(ReferenceParamsPostProcessor.class);
        configSetPostProcessor.add(RegistryParamsPostProcessor.class);
        configSetPostProcessor.add(ServiceParamsPostProcessor.class);
    }
    @Override
    public List<Class> getConfigPostProcessorList() {
        this.beforeSetXMLConfig(this.configSetPostProcessor);
        return this.configSetPostProcessor;
    }

    /**
     * 空方法，可以继承该类，添加自定义配置后置处理器
     * @param list
     */
    public void beforeSetXMLConfig(List<Class> list){}
}
