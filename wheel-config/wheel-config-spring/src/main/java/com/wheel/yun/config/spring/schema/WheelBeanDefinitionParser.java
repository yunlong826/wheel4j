package com.wheel.yun.config.spring.schema;



import com.wheel.yun.common.config.InterfaceConfig;
import com.wheel.yun.config.common.ApplicationConfig;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.common.cache.WheelBeanDefinitionCache;
import com.wheel.yun.config.spring.ReferenceBean;
import com.wheel.yun.config.spring.ServiceBean;
import com.wheel.yun.config.spring.util.WheelBeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
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
        if(beanClass.equals(ApplicationConfig.class)){
            genericBeanDefinition.getPropertyValues().add("name",element.getAttribute("name"));
            parserContext.getRegistry().registerBeanDefinition(ApplicationConfig.class.getName(),genericBeanDefinition);
        }else if(beanClass.equals(RegistryConfig.class)){
            genericBeanDefinition.getPropertyValues().add("id",element.getAttribute("id"));
            genericBeanDefinition.getPropertyValues().add("address",element.getAttribute("address"));
            if(WheelBeanUtils.ADDRESS.length() == 0){
                WheelBeanUtils.ADDRESS = element.getAttribute("address");
            }
            WheelBeanDefinitionCache.putCache(RegistryConfig.class, element.getAttribute("id"));

            parserContext.getRegistry().registerBeanDefinition(element.getAttribute("id"),genericBeanDefinition);
        }else if(beanClass.equals(ProtocolConfig.class)){
            genericBeanDefinition.getPropertyValues().add("protocol",element.getAttribute("protocol"));
            genericBeanDefinition.getPropertyValues().add("port",element.getAttribute("port"));

            WheelBeanDefinitionCache.putCache(ProtocolConfig.class,element.getAttribute("protocol"));

            parserContext.getRegistry().registerBeanDefinition(element.getAttribute("protocol"),genericBeanDefinition);
        }else if(beanClass.equals(ServiceBean.class)){
            if(element.getAttribute("interface") == null || element.getAttribute("interface").length() == 0)
                return genericBeanDefinition;
            genericBeanDefinition.getPropertyValues().add("interface",element.getAttribute("interface"));
            genericBeanDefinition.getPropertyValues().add("version",element.getAttribute("version"));
            genericBeanDefinition.getPropertyValues().add("ref",element.getAttribute("ref"));
            genericBeanDefinition.getPropertyValues().add("group",element.getAttribute("group"));
            genericBeanDefinition.getPropertyValues().add("timeout",element.getAttribute("timeout"));
            genericBeanDefinition.getPropertyValues().add("failStrategy",element.getAttribute("failStrategy"));
            genericBeanDefinition.getPropertyValues().add("retryCount",element.getAttribute("retryCount"));

            WheelBeanDefinitionCache.putCache(ServiceBean.class,element.getAttribute("interface"));

            parserContext.getRegistry().registerBeanDefinition(element.getAttribute("interface"),genericBeanDefinition);
        }else if(beanClass.equals(ReferenceBean.class)){
            System.out.println(element.getAttribute("id"));

            String id = element.getAttribute("id")+element.getAttribute("interface")+element.getAttribute("version")+
                    element.getAttribute("group");
            if(id == null || id.length() == 0)
                return genericBeanDefinition;
            genericBeanDefinition.getPropertyValues().add("id",element.getAttribute("id"));
            genericBeanDefinition.getPropertyValues().add("interface",element.getAttribute("interface"));
            genericBeanDefinition.getPropertyValues().add("version",element.getAttribute("version"));
            genericBeanDefinition.getPropertyValues().add("group",element.getAttribute("group"));
            genericBeanDefinition.getPropertyValues().add("timeout",element.getAttribute("timeout"));
            genericBeanDefinition.getPropertyValues().add("failStrategy",element.getAttribute("failStrategy"));
            genericBeanDefinition.getPropertyValues().add("retryCount",element.getAttribute("retryCount"));


            WheelBeanDefinitionCache.putCache(ReferenceBean.class,id);
            parserContext.getRegistry().registerBeanDefinition(id,genericBeanDefinition);
        }else{
            throw new IllegalArgumentException("error<--------------->error");
        }
        return genericBeanDefinition;
    }
}
