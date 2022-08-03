package com.wheel.yun.config.spring.schema;


import com.wheel.yun.common.utils.Pair;
import com.wheel.yun.config.spring.context.RegistryXMLConfigsContext;
import com.wheel.yun.config.spring.processor.XMLConfigPostProcessor;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/5/28 17:29
 */
public class WheelNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        // 初始化配置参数相关的后置处理器
        this.instanceConstructor();
        // 注册beanDefinition转化器
        this.process();
    }

    private void process(){
        List<Pair<String, Class>> configsContext = RegistryXMLConfigsContext.getConfigsContext();
        this.beforeRegisterParser(configsContext);
        for(int i = 0;i<configsContext.size();i++){
            this.registerBeanDefinitionParser(configsContext.get(i).getLeft()
                    ,new WheelBeanDefinitionParser(configsContext.get(i).getRight()));
        }
    }

    /**
     * 可以自定义对configsContext添加自定义的xml配置类
     */
    public void beforeRegisterParser(List<Pair<String,Class>> configsContext){};

    private void instanceConstructor(){
        XMLConfigPostProcessor xmlConfigPostProcessor = new XMLConfigPostProcessor();
        List<Class> configPostProcessorList = xmlConfigPostProcessor.getConfigPostProcessorList();
        for(int i = 0;i<configPostProcessorList.size();i++){
            try {
                // 相关参数类后置处理器必须是空参数构造器进行初始化
                Constructor constructor = configPostProcessorList.get(i).getConstructor();
                // 进行该类空参数的构造方法
                constructor.newInstance();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }




}
