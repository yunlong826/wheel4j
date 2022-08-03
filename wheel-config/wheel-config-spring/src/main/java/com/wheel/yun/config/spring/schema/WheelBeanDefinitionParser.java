package com.wheel.yun.config.spring.schema;

import com.wheel.yun.config.common.cache.WheelBeanDefinitionCache;
import com.wheel.yun.config.spring.context.ConfigsParamsContext;
import com.wheel.yun.config.spring.processor.XMLConfigPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/5/28 17:31
 */
public class WheelBeanDefinitionParser implements BeanDefinitionParser {
    private final Class<?> beanClass;
    public WheelBeanDefinitionParser(Class<?> beanClass){
        this.beanClass = beanClass;
    }



    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(beanClass);
        genericBeanDefinition.setLazyInit(false);

        this.parse(element,parserContext,genericBeanDefinition);

        return genericBeanDefinition;
    }
    private void parse(Element element,ParserContext parserContext
            ,GenericBeanDefinition beanDefinition){
        List<String> beanNames = ConfigsParamsContext.ClassMapBeanName.get(this.beanClass);
        String beanName = "";
        for(int i = 0;i<beanNames.size();i++){
            beanName+=element.getAttribute(beanNames.get(i));
        }
        if(beanName.length() == 0)
            return;
        List<String> params = ConfigsParamsContext.PARAMS.get(this.beanClass);
        for(String param:params){
            String attribute = element.getAttribute(param);
            if(attribute == null ||attribute.length() == 0)
                continue;
            beanDefinition.getPropertyValues().add(param,attribute);
        }
        // 放入wheel容器内部，后续找相应的容器，直接从该缓存找，即可
        WheelBeanDefinitionCache.putCache(this.beanClass,beanName);
        // 注册到容器中
        parserContext.getRegistry().registerBeanDefinition(beanName,beanDefinition);
    }


}
